/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.framework.tools;

import static android.telephony.TelephonyManager.NETWORK_TYPE_CDMA;
import static android.telephony.TelephonyManager.NETWORK_TYPE_EDGE;
import static android.telephony.TelephonyManager.NETWORK_TYPE_EVDO_0;
import static android.telephony.TelephonyManager.NETWORK_TYPE_EVDO_A;
import static android.telephony.TelephonyManager.NETWORK_TYPE_EVDO_B;
import static android.telephony.TelephonyManager.NETWORK_TYPE_GPRS;
import static android.telephony.TelephonyManager.NETWORK_TYPE_HSDPA;
import static android.telephony.TelephonyManager.NETWORK_TYPE_LTE;
import static android.telephony.TelephonyManager.NETWORK_TYPE_UMTS;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;

/**
 * 
 * 
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 * 
 */
public class NetworkStatusTools {

	public static NetworkConnectInfo getNetworkConnectInfo(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();

		NetworkConnectInfo networkConnectInfo = new NetworkConnectInfo();
		networkConnectInfo.available = activeNetInfo != null
				&& activeNetInfo.isAvailable();
		if (!networkConnectInfo.available) {
			return networkConnectInfo;
		}
		NetworkInfo wifiNetInfo = connectivityManager
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if (wifiNetInfo != null && wifiNetInfo.getState() == State.CONNECTED) {
			networkConnectInfo.connectType = ConnectType.Wifi;
			return networkConnectInfo;
		}

		NetworkInfo mobNetInfo = connectivityManager
				.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

		if (mobNetInfo == null || mobNetInfo.getState() == State.CONNECTED) {
			return networkConnectInfo;
		}
		networkConnectInfo.connectType = getMobileConnectType(mobNetInfo);
		return networkConnectInfo;

	}

	private static ConnectType getMobileConnectType(NetworkInfo mobNetInfo) {
		switch (mobNetInfo.getSubtype()) {
		case NETWORK_TYPE_LTE:
			return ConnectType._4G;
		case NETWORK_TYPE_EVDO_0:
		case NETWORK_TYPE_EVDO_A:
		case NETWORK_TYPE_EVDO_B:
		case NETWORK_TYPE_HSDPA:
		case NETWORK_TYPE_UMTS:
			return ConnectType._3G;
		case NETWORK_TYPE_GPRS:
		case NETWORK_TYPE_EDGE:
		case NETWORK_TYPE_CDMA:
			return ConnectType._2G;
		default:
			return ConnectType.Other;
		}
	}

	public static class NetworkConnectInfo {
		public boolean available = false;
		public ConnectType connectType = ConnectType.Other;

	}

	public static enum ConnectType {
		Wifi, _2G, _3G, _4G, Other
	}
}
