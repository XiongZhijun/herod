/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved.
 */
package org.herod.order.event;

import static org.herod.order.model.OrderStatus.Acceptted;
import static org.herod.order.model.OrderStatus.Submitted;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.herod.event.Event;
import org.herod.event.EventCodes;
import org.herod.event.EventFields;
import org.herod.order.model.OrderStatus;
import org.herod.order.order.OrderCenter;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 */
public class HeartbeatScheduledService implements Runnable {
	@Autowired
	private EventCenter eventCenter;
	@Autowired
	private OrderCenter orderEventCenter;
	private ExecutorService executorService = Executors.newFixedThreadPool(2);

	@Override
	public void run() {
		List<Callable<Object>> tasks = new ArrayList<Callable<Object>>();
		for (long workerId : eventCenter.getAllConnectedWorkers()) {
			Map<OrderStatus, Integer> ordersCount = orderEventCenter
					.getOrdersCount(workerId);
			HeartbeatCallable heartbeatServer = new HeartbeatCallable(workerId,
					ordersCount);
			tasks.add(heartbeatServer);
		}
		try {
			executorService.invokeAll(tasks);
		} catch (InterruptedException e) {
			_log.warn("send event to client failed.", e);
		}
	}

	public void setEventCenter(EventCenter eventCenter) {
		this.eventCenter = eventCenter;
	}

	public void setOrderEventCenter(OrderCenter orderEventCenter) {
		this.orderEventCenter = orderEventCenter;
	}

	public void setExecutorService(ExecutorService executorService) {
		this.executorService = executorService;
	}

	class HeartbeatCallable implements Callable<Object> {
		private long workerId;
		private Map<OrderStatus, Integer> ordersCount;

		public HeartbeatCallable(long workerId,
				Map<OrderStatus, Integer> ordersCount) {
			super();
			this.workerId = workerId;
			this.ordersCount = ordersCount;
		}

		@Override
		public Object call() throws Exception {
			int acceptted = ordersCount.containsKey(Acceptted) ? ordersCount
					.get(Acceptted) : 0;
			int submitted = ordersCount.containsKey(Submitted) ? ordersCount
					.get(Submitted) : 0;
			Event event = new Event();
			event.setCode(EventCodes.HEARTBEAT_COMMAND);
			event.put(EventFields.ACCEPTTED_COUNT, acceptted);
			event.put(EventFields.SUBMITTED_COUNT, submitted);
			eventCenter.sendEvent(workerId, event);
			return null;
		}

	}

	private static final Log _log = LogFactory
			.getLog(HeartbeatScheduledService.class);
}
