/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved.
 */
package org.herod.worker.phone;

import org.herod.framework.utils.StringUtils;
import org.herod.order.common.OrderContext;
import org.herod.worker.phone.rest.RestWorkerService;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

/**
 * 
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 */
public abstract class WorkerContext {
	private static final String TOKEN = "TOKEN";
	private static WorkerService workerService;
	private static String restServerHost;
	private static int restServerPort;
	private static String imageServerHost;
	private static int imageServerPort;
	private static String loginTokenString;
	private static SharedPreferences defaultSharedPreferences;

	public static void init(Context context) {
		workerService = new RestWorkerService(context);
		restServerHost = context.getString(R.string.RestServerHost);
		restServerPort = Integer.parseInt(context
				.getString(R.string.RestServerPort));
		imageServerHost = context.getString(R.string.ImageServerHost);
		imageServerPort = Integer.parseInt(context
				.getString(R.string.ImageServerPort));
		defaultSharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(context.getApplicationContext());

		OrderContext.setImageServerHost(imageServerHost);
		OrderContext.setImageServerPort(imageServerPort);
	}

	public static WorkerService getWorkerService() {
		return workerService;
	}

	public static String getRestServerHost() {
		return restServerHost;
	}

	public static int getRestServerPort() {
		return restServerPort;
	}

	public static String getImageServerHost() {
		return imageServerHost;
	}

	public static int getImageServerPort() {
		return imageServerPort;
	}

	public static void setLoginToken(String token) {
		WorkerContext.loginTokenString = token;
		Editor editor = defaultSharedPreferences.edit();
		editor.putString(TOKEN, token);
		editor.commit();
	}

	public static String getLoginTokenString() {
		if (StringUtils.isBlank(loginTokenString)) {
			loginTokenString = defaultSharedPreferences.getString(TOKEN,
					StringUtils.EMPTY);
		}
		return loginTokenString;
	}

	public static boolean isInLogin() {
		return StringUtils.isNotBlank(getLoginTokenString());
	}

}
