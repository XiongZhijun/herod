/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.order.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.wink.common.annotations.Workspace;

/**
 * 
 * 
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 * 
 */
@Workspace(collectionTitle = "Serial Number Builder", workspaceTitle = "Order System")
public class SimpleOrderSerailNumberService implements OrderSerailNumberService {
	private DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
	private Lock lock = new ReentrantLock();
	private Map<String, Integer> timeMap = new HashMap<String, Integer>();

	@Override
	public String buildTransactionSerialNumber() {
		lock.lock();
		try {
			String timeString = dateFormat.format(new Date());
			int sequence = 1;
			if (timeMap.containsKey(timeString)) {
				sequence = timeMap.get(timeString);
			} else if (timeMap.size() > 100) {
				timeMap.clear();
			}
			String sn = timeString + "-" + sequence;
			sequence++;
			timeMap.put(timeString, sequence);
			return sn;
		} finally {
			lock.unlock();
		}
	}

}
