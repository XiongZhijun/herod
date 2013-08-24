/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.order.event;

import java.util.concurrent.Callable;

import org.apache.mina.common.IoSession;
import org.herod.event.Event;
import org.herod.event.EventCodes;

/**
 * 
 * 
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 * 
 */
public class HeartbeatServer implements Callable<Object> {
	private IoSession session;
	private int acceptted;
	private int submitted;

	public HeartbeatServer(IoSession session, int acceptted, int submitted) {
		super();
		this.session = session;
		this.acceptted = acceptted;
		this.submitted = submitted;
	}

	@Override
	public Object call() throws Exception {
		if (session == null || session.isClosing() || !session.isConnected()) {
			return null;
		}
		Event event = new Event();
		event.setCode(EventCodes.HEARTBEAT_COMMAND);
		event.put("a", acceptted);
		event.put("s", submitted);
		session.write(event);
		return null;
	}

}
