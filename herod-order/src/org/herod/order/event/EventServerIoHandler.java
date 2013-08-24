/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.order.event;

import org.apache.mina.common.IdleStatus;
import org.apache.mina.common.IoHandlerAdapter;
import org.apache.mina.common.IoSession;
import org.herod.event.Event;
import org.herod.event.EventCodes;
import org.herod.event.EventFields;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 
 * 
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 * 
 */
public class EventServerIoHandler extends IoHandlerAdapter {
	@Autowired
	private EventCenter eventCenter;

	@Override
	public void sessionCreated(IoSession session) throws Exception {
		super.sessionCreated(session);
	}

	@Override
	public void sessionClosed(IoSession session) throws Exception {
		super.sessionClosed(session);
		eventCenter.unregist(session);
	}

	@Override
	public void sessionIdle(IoSession session, IdleStatus status)
			throws Exception {
		IoSession oldSession = eventCenter.unregist(session);
		closeSession(oldSession);
	}

	@Override
	public void exceptionCaught(IoSession session, Throwable cause)
			throws Exception {
		IoSession oldSession = eventCenter.unregist(session);
		closeSession(oldSession);
	}

	@Override
	public void messageReceived(IoSession session, Object message)
			throws Exception {
		if (!(message instanceof Event)) {
			return;
		}
		Event event = (Event) message;
		if (EventCodes.REGIST_COMMAND.equals(event.getCode())) {
			long workerId = event.getLong(EventFields.ID);
			IoSession oldSession = eventCenter.regist(workerId, session);
			if (oldSession != session)
				closeSession(oldSession);
		}
	}

	private void closeSession(IoSession oldSession) {
		if (oldSession != null && oldSession.isConnected()) {
			oldSession.close();
		}
	}

	public void setEventCenter(EventCenter eventCenter) {
		this.eventCenter = eventCenter;
	}

}
