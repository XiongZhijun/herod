/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.event;

import org.herod.communication.common.ByteFrame;
import org.herod.communication.common.simple.HeadTailHandler;
import org.herod.event.EventUtils;

/**
 * 
 * 
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 * 
 */
public class EventHeadTailHandler extends HeadTailHandler {

	private static final byte[] HEAD = new byte[] { 0x23, 0x23 };
	private static final byte[] TAIL = new byte[] { 0x0d, 0x0a };

	public EventHeadTailHandler() {
		super(HEAD, TAIL);
	}

	protected ByteFrame decodeFrame(byte[] frameBytes) {
		return EventUtils.parse(frameBytes);
	}
}
