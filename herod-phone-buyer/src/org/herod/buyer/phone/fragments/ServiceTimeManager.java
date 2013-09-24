/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved.
 */
package org.herod.buyer.phone.fragments;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.herod.buyer.phone.Constants;
import org.herod.buyer.phone.R;
import org.herod.framework.MapWrapper;
import org.herod.framework.ViewFindable.ViewWrapper;
import org.herod.framework.utils.StringUtils;
import org.herod.framework.utils.TextViewUtils;
import org.herod.framework.utils.ViewUtils;
import org.herod.order.common.model.ShopStatus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.util.CollectionUtils;

import android.util.Log;
import android.view.View;

/**
 * 
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 */
public class ServiceTimeManager {
	private static final int[] SERVICE_TIME_VIEWS = new int[] {
			R.id.serviceTime1, R.id.serviceTime2, R.id.serviceTime3,
			R.id.serviceTimeTitle };
	private static final int[] SERVICE_TIME_IDS = new int[] {
			R.id.serviceTime1, R.id.serviceTime2, R.id.serviceTime3 };
	private static final String TAG = ServiceTimeManager.class.getSimpleName();
	private static DateFormat dateFormat = new SimpleDateFormat("HH:mm");
	private List<ServiceTime> serviceTimes = new ArrayList<ServiceTime>();
	private ShopStatus shopStatus;

	public ServiceTimeManager(MapWrapper<String, Object> shop) {
		this(shop.getString(Constants.SERVICE_TIMES), shop.getEnum(
				ShopStatus.class, Constants.STATUS));
	}

	private ServiceTimeManager(String serviceTimes, ShopStatus shopStatus) {
		this.shopStatus = shopStatus;
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
			Log.w(TAG, e.getMessage());
		}
	}

	public boolean isNotInServiceNow() {
		return !isInServiceNow();
	}

	public boolean isInServiceNow() {
		if (this.shopStatus != ShopStatus.OPEN) {
			return false;
		}
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

	public void updateServiceTimeViews(View panel) {
		ViewWrapper viewFindable = new ViewWrapper(panel);
		int serviceTimeVisible = View.VISIBLE;
		int shopNotOpenVisible = View.VISIBLE;

		if (this.shopStatus == ShopStatus.OPEN) {
			shopNotOpenVisible = View.GONE;
		} else {
			serviceTimeVisible = View.GONE;
		}

		ViewUtils.setVisibility(viewFindable, serviceTimeVisible,
				SERVICE_TIME_VIEWS);
		ViewUtils.setVisibility(viewFindable, shopNotOpenVisible,
				R.id.shopNotOpenLabel);
		if (shopNotOpenVisible == View.VISIBLE) {
			return;
		}
		for (int i = 0; i < SERVICE_TIME_IDS.length && i < serviceTimes.size(); i++) {
			TextViewUtils.setText(viewFindable, SERVICE_TIME_IDS[i],
					serviceTimes.get(i));
		}
	}

	private class ServiceTime {
		Date startTime;
		Date endTime;
		String start;
		String end;

		private ServiceTime(JSONObject jsonObject) throws JSONException,
				ParseException {
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
