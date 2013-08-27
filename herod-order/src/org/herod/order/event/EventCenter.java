/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.order.event;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.mina.common.IoSession;
import org.herod.event.Event;

/**
 * 
 * 
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 * 
 */
public class EventCenter {

	private final Map<Long, IoSession> workerSessionMap = new HashMap<Long, IoSession>();

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
		if (session != null && session.isConnected()) {
			session.write(event);
		}
	}

}
