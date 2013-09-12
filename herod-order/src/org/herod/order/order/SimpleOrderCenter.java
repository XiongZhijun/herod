/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved.
 */
package org.herod.order.order;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.herod.common.das.HerodBeanPropertyRowMapper;
import org.herod.common.das.HerodJdbcTemplate;
import org.herod.event.Event;
import org.herod.event.EventCodes;
import org.herod.event.EventFields;
import org.herod.order.event.EventCenter;
import org.herod.order.model.OrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;

/**
 * 
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 */
public class SimpleOrderCenter implements OrderCenter {

	private ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
	private Lock readLock = readWriteLock.readLock();
	private Lock writeLock = readWriteLock.writeLock();
	private Set<OrderInfo> workerOrders = new HashSet<OrderInfo>();
	@Autowired
	private HerodJdbcTemplate herodJdbcTemplate;
	@Autowired
	private EventCenter eventCenter;

	@Override
	public void init() {
		writeLock.lock();
		try {
			workerOrders.clear();
			RowMapper<OrderInfo> rm = new HerodBeanPropertyRowMapper<OrderInfo>(
					OrderInfo.class);
			List<OrderInfo> orderInfos = herodJdbcTemplate
					.query("SELECT SERIAL_NUMBER SN, STATUS,AGENT_ID, DELIVERY_WORKER_ID WORKER_ID FROM ZRH_ORDER WHERE STATUS = 'Acceptted' OR STATUS = 'Submitted' OR STATUS = 'Rejected'",
							rm);
			this.workerOrders.addAll(orderInfos);
		} finally {
			writeLock.unlock();
		}
	}

	@Override
	public void submitOrder(long agentId, long workerId, String orderSN) {
		addToWorkerSet(agentId, workerId, orderSN, OrderStatus.Submitted);
	}

	@Override
	public void acceptOrder(long agentId, long workerId, String orderSN) {
		addToWorkerSet(agentId, workerId, orderSN, OrderStatus.Acceptted);
	}

	@Override
	public void rejectOrder(long agentId, long workerId, String orderSN) {
		addToWorkerSet(agentId, workerId, orderSN, OrderStatus.Rejected);
	}

	@Override
	public void cancelOrder(long agentId, long workerId, String orderSN) {
		removeFromWorkerSet(agentId, workerId, orderSN, OrderStatus.Cancelled);
	}

	@Override
	public void completeOrder(long agentId, long workerId, String orderSN) {
		removeFromWorkerSet(agentId, workerId, orderSN, OrderStatus.Completed);
	}

	@Override
	public WorkerOrdersCount getOrdersCount(long agentId, long workerId) {
		WorkerOrdersCount counts = new WorkerOrdersCount();
		readLock.lock();
		try {
			for (OrderInfo info : workerOrders) {
				if (info.agentId != agentId) {
					continue;
				}
				switch (info.getStatus()) {
				case Submitted:
				case Rejected:
					counts.submitted++;
					break;
				case Acceptted:
					if (info.workerId == workerId) {
						counts.acceptted++;
					}
					break;
				default:
					break;
				}
			}
		} finally {
			readLock.unlock();
		}
		return counts;
	}

	private void removeFromWorkerSet(long agentId, long workerId,
			String orderSN, OrderStatus status) {
		writeLock.lock();
		try {
			workerOrders.remove(new OrderInfo(orderSN));
			Event event = buildEvent(agentId, workerId, status);
			eventCenter.sendEvent(workerId, event);
		} finally {
			writeLock.unlock();
		}
	}

	private void addToWorkerSet(long agentId, long workerId, String orderSN,
			OrderStatus status) {
		writeLock.lock();
		try {
			OrderInfo orderInfo = new OrderInfo(agentId, workerId, orderSN,
					status);
			workerOrders.remove(orderInfo);
			workerOrders.add(orderInfo);
		} finally {
			writeLock.unlock();
		}
		Event event = buildEvent(agentId, workerId, status);
		if (workerId > 0) {
			eventCenter.sendEvent(workerId, event);
		} else {
			eventCenter.sendEventByAgent(agentId, event);
		}
	}

	private Event buildEvent(long agentId, long workerId, OrderStatus status) {
		Event event = new Event();
		event.setCode(getEventCode(status));
		WorkerOrdersCount ordersCount = getOrdersCount(agentId, workerId);
		event.put(EventFields.ACCEPTTED_COUNT, ordersCount.acceptted);
		event.put(EventFields.SUBMITTED_COUNT, ordersCount.submitted);
		return event;
	}

	private String getEventCode(OrderStatus status) {
		switch (status) {
		case Submitted:
			return EventCodes.SUBMIT_COMMAND;
		case Acceptted:
			return EventCodes.ACCEPT_COMMAND;
		case Completed:
			return EventCodes.COMPLETE_COMMAND;
		case Cancelled:
			return EventCodes.CANCEL_COMMAND;
		case Rejected:
			return EventCodes.REJECT_COMMAND;
		default:
			return EventCodes.SUBMIT_COMMAND;
		}
	}

	public void setHerodJdbcTemplate(HerodJdbcTemplate herodJdbcTemplate) {
		this.herodJdbcTemplate = herodJdbcTemplate;
	}

	public void setEventCenter(EventCenter eventCenter) {
		this.eventCenter = eventCenter;
	}

	public static class OrderInfo {
		private long agentId;
		private long workerId;
		private String sn;
		private OrderStatus status;

		public OrderInfo() {
			super();
		}

		public OrderInfo(String sn) {
			super();
			this.sn = sn;
		}

		public OrderInfo(long agentId, long workerId, String sn,
				OrderStatus status) {
			super();
			this.agentId = agentId;
			this.workerId = workerId;
			this.sn = sn;
			this.status = status;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((sn == null) ? 0 : sn.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			OrderInfo other = (OrderInfo) obj;
			if (sn == null) {
				if (other.sn != null)
					return false;
			} else if (!sn.equals(other.sn))
				return false;
			return true;
		}

		public long getAgentId() {
			return agentId;
		}

		public void setAgentId(long agentId) {
			this.agentId = agentId;
		}

		public long getWorkerId() {
			return workerId;
		}

		public void setWorkerId(long workerId) {
			this.workerId = workerId;
		}

		public String getSn() {
			return sn;
		}

		public void setSn(String sn) {
			this.sn = sn;
		}

		public OrderStatus getStatus() {
			return status;
		}

		public void setStatus(OrderStatus status) {
			this.status = status;
		}

	}

}
