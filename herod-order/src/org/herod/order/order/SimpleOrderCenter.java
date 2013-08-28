/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved.
 */
package org.herod.order.order;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
	private Map<Long, Set<OrderInfo>> workerOrdersMap = new HashMap<Long, Set<OrderInfo>>();
	@Autowired
	private HerodJdbcTemplate herodJdbcTemplate;
	@Autowired
	private EventCenter eventCenter;

	@Override
	public void init() {
		writeLock.lock();
		try {
			workerOrdersMap.clear();
			RowMapper<OrderInfo> rm = new HerodBeanPropertyRowMapper<OrderInfo>(
					OrderInfo.class);
			List<OrderInfo> orderInfos = herodJdbcTemplate
					.query("SELECT SERIAL_NUMBER SN, STATUS, DELIVERY_WORKER_ID WORKER_ID FROM ZRH_ORDER WHERE STATUS = 'Acceptted' OR STATUS = 'Submitted' OR STATUS = 'Rejected'",
							rm);
			for (OrderInfo info : orderInfos) {
				getOrderInfos(info.workerId).add(info);
			}
		} finally {
			writeLock.unlock();
		}
	}

	@Override
	public void submitOrder(long workerId, String orderSN) {
		addToWorkerSet(workerId, orderSN, OrderStatus.Submitted);
	}

	@Override
	public void acceptOrder(long workerId, String orderSN) {
		addToWorkerSet(workerId, orderSN, OrderStatus.Acceptted);
	}

	@Override
	public void rejectOrder(long workerId, String orderSN) {
		addToWorkerSet(workerId, orderSN, OrderStatus.Rejected);
	}

	@Override
	public void cancelOrder(long workerId, String orderSN) {
		removeFromWorkerSet(workerId, orderSN, OrderStatus.Cancelled);
	}

	@Override
	public void completeOrder(long workerId, String orderSN) {
		removeFromWorkerSet(workerId, orderSN, OrderStatus.Completed);
	}

	@Override
	public Map<OrderStatus, Integer> getOrdersCount(long workerId) {
		Map<OrderStatus, Integer> results = new HashMap<OrderStatus, Integer>();
		readLock.lock();
		try {
			Set<OrderInfo> orderInfos = getOrderInfos(workerId);
			for (OrderInfo info : orderInfos) {
				OrderStatus status = info.status;
				if (!results.containsKey(status)) {
					results.put(status, 1);
				} else {
					int count = results.get(status);
					results.put(status, count + 1);
				}
			}
		} finally {
			readLock.unlock();
		}
		return results;
	}

	private void removeFromWorkerSet(long workerId, String orderSN,
			OrderStatus status) {
		writeLock.lock();
		try {
			getOrderInfos(workerId).remove(new OrderInfo(orderSN));
			Event event = buildEvent(workerId, status);
			eventCenter.sendEvent(workerId, event);
		} finally {
			writeLock.unlock();
		}
	}

	private void addToWorkerSet(long workerId, String orderSN,
			OrderStatus status) {
		writeLock.lock();
		try {
			OrderInfo orderInfo = new OrderInfo(orderSN, status);
			Set<OrderInfo> orderInfos = getOrderInfos(workerId);
			orderInfos.remove(orderInfo);
			orderInfos.add(orderInfo);
		} finally {
			writeLock.unlock();
		}
		Event event = buildEvent(workerId, status);
		eventCenter.sendEvent(workerId, event);
	}

	private Event buildEvent(long workerId, OrderStatus status) {
		Event event = new Event();
		event.setCode(getEventCode(status));
		Map<OrderStatus, Integer> ordersCountMap = getOrdersCount(workerId);
		event.put(EventFields.ACCEPTTED_COUNT,
				ordersCountMap.get(OrderStatus.Acceptted));
		event.put(EventFields.SUBMITTED_COUNT,
				ordersCountMap.get(OrderStatus.Submitted));
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

	protected Set<OrderInfo> getOrderInfos(long workerId) {
		Set<OrderInfo> set = workerOrdersMap.get(workerId);
		if (set == null) {
			set = new HashSet<OrderInfo>();
			workerOrdersMap.put(workerId, set);
		}
		return set;
	}

	public void setHerodJdbcTemplate(HerodJdbcTemplate herodJdbcTemplate) {
		this.herodJdbcTemplate = herodJdbcTemplate;
	}

	public void setEventCenter(EventCenter eventCenter) {
		this.eventCenter = eventCenter;
	}

	public static class OrderInfo {
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

		public OrderInfo(String sn, OrderStatus status) {
			super();
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
