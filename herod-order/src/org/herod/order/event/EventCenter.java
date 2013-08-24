/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.order.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.mina.common.IoSession;
import org.herod.common.das.HerodBeanPropertyRowMapper;
import org.herod.common.das.HerodJdbcTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;

/**
 * 
 * 
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 * 
 */
public class EventCenter {

	private final Map<Long, IoSession> workerSessionMap = new HashMap<Long, IoSession>();
	@Autowired
	private HerodJdbcTemplate herodJdbcTemplate;
	private ScheduledExecutorService scheduledExecutorService = Executors
			.newScheduledThreadPool(4);
	private ExecutorService executorService = scheduledExecutorService;
	/** 单位：{@link TimeUnit#SECONDS} */
	private long initialDelay = 30;
	private long period = 30;

	public void start() {
		scheduledExecutorService.scheduleAtFixedRate(
				new EventScheduledService(), initialDelay, period,
				TimeUnit.SECONDS);
	}

	public IoSession regist(long workerId, IoSession session) {
		return workerSessionMap.put(workerId, session);
	}

	public IoSession unregist(long workerId) {
		return workerSessionMap.remove(workerId);
	}

	public IoSession unregist(IoSession session) {
		long workerId = findWorkerId(session);
		return unregist(workerId);
	}

	private long findWorkerId(IoSession session) {
		long workerId = 0;
		for (Entry<Long, IoSession> entry : workerSessionMap.entrySet()) {
			if (entry.getValue().equals(session)) {
				workerId = entry.getKey();
			}
		}
		return workerId;
	}

	public void setHerodJdbcTemplate(HerodJdbcTemplate herodJdbcTemplate) {
		this.herodJdbcTemplate = herodJdbcTemplate;
	}

	public void setExecutorService(ExecutorService executorService) {
		this.executorService = executorService;
	}

	public void setScheduledExecutorService(
			ScheduledExecutorService scheduledExecutorService) {
		this.scheduledExecutorService = scheduledExecutorService;
	}

	public void setInitialDelay(long initialDelay) {
		this.initialDelay = initialDelay;
	}

	public void setPeriod(long period) {
		this.period = period;
	}

	class EventScheduledService implements Runnable {

		@Override
		public void run() {
			RowMapper<WorkerTaskCounts> rm = new HerodBeanPropertyRowMapper<WorkerTaskCounts>(
					WorkerTaskCounts.class);
			List<WorkerTaskCounts> records = herodJdbcTemplate
					.query("SELECT DELIVERY_WORKER_ID WORKER_ID, COUNT(CASE WHEN STATUS = 'ACCEPTTED' THEN 1 END ) ACCEPTTED, COUNT(CASE WHEN STATUS = 'SUBMITTED' THEN 1 END ) SUBMITTED FROM ZRH_ORDER WHERE STATUS = 'ACCEPTTED' OR STATUS = 'SUBMITTED' GROUP BY DELIVERY_WORKER_ID",
							rm);
			List<Callable<Object>> tasks = new ArrayList<Callable<Object>>();
			for (Entry<Long, IoSession> entry : workerSessionMap.entrySet()) {
				long workerId = entry.getKey();
				IoSession session = entry.getValue();
				for (WorkerTaskCounts record : records) {
					if (workerId == record.workerId) {
						HeartbeatServer heartbeatServer = new HeartbeatServer(
								session, record.acceptted, record.submitted);
						tasks.add(heartbeatServer);
						break;
					}
				}
			}
			try {
				executorService.invokeAll(tasks);
			} catch (InterruptedException e) {
				_log.warn("send event to client failed.", e);
			}
		}

	}

	public static class WorkerTaskCounts {
		long workerId;
		int acceptted;
		int submitted;

		public long getWorkerId() {
			return workerId;
		}

		public void setWorkerId(long workerId) {
			this.workerId = workerId;
		}

		public int getAcceptted() {
			return acceptted;
		}

		public void setAcceptted(int acceptted) {
			this.acceptted = acceptted;
		}

		public int getSubmitted() {
			return submitted;
		}

		public void setSubmitted(int submitted) {
			this.submitted = submitted;
		}
	}

	private static final Log _log = LogFactory.getLog(EventCenter.class);
}
