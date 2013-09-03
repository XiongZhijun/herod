/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.worker.phone.event;

import static org.herod.worker.phone.Constants.EVENT;

import org.herod.android.communication.TcpClientService;
import org.herod.communication.common.ByteCacheVisitor;
import org.herod.communication.common.ByteFrame;
import org.herod.communication.common.FrameEncoder;
import org.herod.event.Event;
import org.herod.event.EventCodes;
import org.herod.event.EventFields;
import org.herod.event.EventHeadTailHandler;
import org.herod.worker.phone.WorkerContext;

import android.content.Intent;

/**
 * 
 * 
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 * 
 */
public class EventClientService extends TcpClientService {
	private static final String DEFAULT_RING = "ding.wav";

	private EventHeadTailHandler eventHeadTailHandler = new EventHeadTailHandler();

	@Override
	protected ByteCacheVisitor getByteCacheVisitor() {
		return eventHeadTailHandler;
	}

	@Override
	protected FrameEncoder getFrameEncoder() {
		return eventHeadTailHandler;
	}

	public void registToServer() {
		if (!WorkerContext.isInLogin()) {
			return;
		}
		Event event = new Event();
		event.setCode(EventCodes.REGIST_COMMAND);
		event.put(EventFields.ID, WorkerContext.getWorkerId());
		sendMessageCertainly(event);
	}

	@Override
	protected void onReceiveMessage(ByteFrame byteFrame) {
		if (!(byteFrame instanceof Event)) {
			return;
		}
		Event event = (Event) byteFrame;
		Intent intent = new Intent(EventActionUtils.getEventAction(event));
		intent.putExtra(EVENT, event);
		sendBroadcast(intent);
		if (EventCodes.SUBMIT_COMMAND.equals(event.getCode())
				|| EventCodes.REJECT_COMMAND.equals(event.getCode())) {
			MediaPlayerService.playMedia(this, DEFAULT_RING);
			;
		}
	}
}
