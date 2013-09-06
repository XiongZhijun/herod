/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved.
 */
package org.herod.order.event;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.herod.event.Event;
import org.herod.event.EventCodes;
import org.herod.event.EventFields;
import org.herod.order.order.OrderCenter;
import org.herod.order.order.OrderCenter.WorkerOrdersCount;
import org.herod.order.service.LoginService;
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
	@Autowired
	private LoginService loginService;
	private ExecutorService executorService = Executors.newFixedThreadPool(2);

	@Override
	public void run() {
		List<Callable<Object>> tasks = new ArrayList<Callable<Object>>();
		for (long workerId : eventCenter.getAllConnectedWorkers()) {
			long workerAgentId = loginService.getWorkerAgentId(workerId);
			WorkerOrdersCount ordersCount = orderEventCenter.getOrdersCount(
					workerAgentId, workerId);
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
		private WorkerOrdersCount ordersCount;

		public HeartbeatCallable(long workerId, WorkerOrdersCount ordersCount) {
			super();
			this.workerId = workerId;
			this.ordersCount = ordersCount;
		}

		@Override
		public Object call() throws Exception {
			int acceptted = ordersCount.acceptted;
			int submitted = ordersCount.submitted;
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
