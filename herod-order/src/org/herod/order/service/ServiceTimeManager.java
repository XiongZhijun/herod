/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved.
 */
package org.herod.order.service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.util.CollectionUtils;

/**
 * 
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 */
public class ServiceTimeManager {
	private static Log _log = LogFactory.getLog(ServiceTimeManager.class);
	private static DateFormat dateFormat = new SimpleDateFormat("HH:mm");
	private List<ServiceTime> serviceTimes = new ArrayList<ServiceTime>();

	public ServiceTimeManager(String serviceTimes) {
		if (StringUtils.isBlank(serviceTimes)) {
			return;
		}
		JSONArray jsonArray;
		try {
			jsonArray = new JSONArray(serviceTimes);
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				if (jsonObject.has("start") && jsonObject.has("end")) {
					this.serviceTimes.add(new ServiceTime(jsonObject));
				}
			}
		} catch (Exception e) {
			_log.warn("parse service times : " + serviceTimes + " failed!", e);
		}
	}

	public boolean isInServiceNow() {
		Date now = new Date();
		if (CollectionUtils.isEmpty(serviceTimes)) {
			return true;
		}
		for (ServiceTime serviceTime : serviceTimes) {
			if (serviceTime.isInService(now)) {
				return true;
			}
		}
		return false;
	}

	private class ServiceTime {
		Date startTime;
		Date endTime;
		String start;
		String end;

		private ServiceTime(JSONObject jsonObject) throws ParseException {
			start = jsonObject.getString("start");
			end = jsonObject.getString("end");
			this.startTime = dateFormat.parse(start);
			this.endTime = dateFormat.parse(end);
		}

		private ServiceTime(Date startTime, Date endTime) {
			super();
			this.startTime = startTime;
			this.endTime = endTime;
		}

		private boolean isInService(Date date) {
			int startTimes = toTimes(startTime);
			int endTimes = toTimes(endTime);
			int dateTimes = toTimes(date);
			if (startTimes == endTimes) {
				return true;
			} else if (startTimes < endTimes) {
				return dateTimes >= startTimes && dateTimes <= endTimes;
			} else {
				return dateTimes >= startTimes || dateTimes <= endTimes;
			}
		}

		private int toTimes(Date date) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			return calendar.get(Calendar.HOUR_OF_DAY) * 60
					+ calendar.get(Calendar.MINUTE);
		}

		@Override
		public String toString() {
			return start + " - " + end;
		}
	}
}
