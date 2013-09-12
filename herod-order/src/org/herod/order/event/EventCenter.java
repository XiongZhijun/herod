/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.order.event;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.apache.mina.common.IoSession;
import org.herod.event.Event;
import org.herod.order.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 
 * 
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 * 
 */
public class EventCenter {

	private final Map<Long, IoSession> workerSessionMap = new HashMap<Long, IoSession>();
	private Executor executor = Executors.newSingleThreadExecutor();
	@Autowired
	private LoginService loginService;

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

	public Set<Long> getAllConnectedWorkers() {
		return workerSessionMap.keySet();
	}

	public void sendEvent(long workerId, Event event) {
		IoSession session = workerSessionMap.get(workerId);
		if (session != null) {
			executor.execute(new EventSender(session, event));
		}
	}

	public void sendEventByAgent(long agentId, Event event) {
		for (Entry<Long, IoSession> entry : workerSessionMap.entrySet()) {
			long workerId = entry.getKey();
			long workerAgentId = loginService.getWorkerAgentId(workerId);
			if (workerAgentId == agentId) {
				sendEvent(workerId, event);
			}
		}
	}

	class EventSender implements Runnable {

		private IoSession session;
		private Event event;

		public EventSender(IoSession session, Event event) {
			super();
			this.session = session;
			this.event = event;
		}

		@Override
		public void run() {
			if (session != null && session.isConnected()) {
				session.write(event);
			}
		}

	}

}
