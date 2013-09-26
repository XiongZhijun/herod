/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved.
 */
package org.herod.worker.phone.event;

import static org.herod.event.EventCodes.ACCEPT_COMMAND;
import static org.herod.event.EventCodes.CANCEL_COMMAND;
import static org.herod.event.EventCodes.COMPLETE_COMMAND;
import static org.herod.event.EventCodes.HEARTBEAT_COMMAND;
import static org.herod.event.EventCodes.REJECT_COMMAND;
import static org.herod.event.EventCodes.SUBMIT_COMMAND;
import static org.herod.event.EventCodes.UPDATE_COMMAND;
import static org.herod.framework.Constants.UNDERLINE;

import org.herod.event.Event;

import android.content.IntentFilter;

/**
 * 
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 */
public class EventActionUtils {

	public static String getEventAction(Event event) {
		return getEventAction(event.getCode());
	}

	public static String getEventAction(String eventCode) {
		StringBuilder action = new StringBuilder();
		action.append(Event.class.getName()).append(UNDERLINE)
				.append(eventCode);
		return action.toString();
	}

	public static IntentFilter createEventIntentFilter() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(getEventAction(HEARTBEAT_COMMAND));
		filter.addAction(getEventAction(SUBMIT_COMMAND));
		filter.addAction(getEventAction(ACCEPT_COMMAND));
		filter.addAction(getEventAction(COMPLETE_COMMAND));
		filter.addAction(getEventAction(CANCEL_COMMAND));
		filter.addAction(getEventAction(REJECT_COMMAND));
		filter.addAction(getEventAction(UPDATE_COMMAND));
		return filter;
	}
}
