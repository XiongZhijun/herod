/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved.
 */
package org.herod.worker.phone.event;

import org.herod.event.Event;

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
		action.append(Event.class.getName()).append("_").append(eventCode);
		return action.toString();
	}
}
