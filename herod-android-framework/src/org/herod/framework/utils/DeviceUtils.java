/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved.
 */
package org.herod.framework.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;

/**
 * 
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 */
public abstract class DeviceUtils {

	/**
	 * 
	 */
	private static final String PHONE_NUMBER = "PHONE_NUMBER";
	private static String imei;
	private static String phoneNumber;

	public static String getImei(Context context) {
		if (StringUtils.isNotBlank(imei)) {
			return imei;
		}
		TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		imei = tm.getDeviceId();
		return imei;
	}

	public static String getPhoneNumber(Context context) {
		if (StringUtils.isNotBlank(phoneNumber)) {
			return phoneNumber;
		}
		phoneNumber = PreferenceManager.getDefaultSharedPreferences(context)
				.getString(PHONE_NUMBER, "");
		if (StringUtils.isNotBlank(phoneNumber)) {
			return phoneNumber;
		}
		TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		phoneNumber = tm.getLine1Number();
		return phoneNumber;
	}

	public static void setPhoneNumber(Context context, String phoneNumber) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		Editor editor = preferences.edit();
		editor.putString(PHONE_NUMBER, phoneNumber);
		editor.commit();
		DeviceUtils.phoneNumber = phoneNumber;
	}
}
