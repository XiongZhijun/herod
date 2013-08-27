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
import org.herod.order.model.Order;
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
	public void submitOrder(Order order) {
		addToWorkerSet(order.getWorkerId(), order.getSerialNumber(),
				OrderStatus.Submitted);
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
		removeFromWorkerSet(workerId, orderSN);
	}

	@Override
	public void completeOrder(long workerId, String orderSN) {
		removeFromWorkerSet(workerId, orderSN);
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

	private void removeFromWorkerSet(long workerId, String orderSN) {
		writeLock.lock();
		try {
			getOrderInfos(workerId).remove(new OrderInfo(orderSN));
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
