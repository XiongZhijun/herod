/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved.
 */
package org.herod.worker.phone;

import org.herod.framework.utils.StringUtils;
import org.herod.order.common.OrderContext;
import org.herod.worker.phone.model.Token;
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
	private static final String WORKER_ID = "WORKER_ID";
	private static final String TOKEN_STRING = "TOKEN_STRING";
	private static WorkerService workerService;
	private static SharedPreferences defaultSharedPreferences;
	private static Token token;

	public static void init(Context context) {
		workerService = new RestWorkerService(context);
		defaultSharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(context.getApplicationContext());
		OrderContext.init(context);
	}

	public static WorkerService getWorkerService() {
		return workerService;
	}

	public static void setLoginToken(Token token) {
		WorkerContext.token = token;
		long workerId = 0;
		String tokenString = StringUtils.EMPTY;
		if (token != null) {
			workerId = token.getWorkerId();
			tokenString = token.getTokenString();
		}
		Editor editor = defaultSharedPreferences.edit();
		editor.putLong(WORKER_ID, workerId);
		editor.putString(TOKEN_STRING, tokenString);
		editor.commit();
	}

	public static Token getLoginToken() {
		if (token != null || defaultSharedPreferences == null) {
			return token;
		}
		String tokenString = defaultSharedPreferences.getString(TOKEN_STRING,
				StringUtils.EMPTY);
		long workerId = defaultSharedPreferences.getLong(WORKER_ID, 0);
		if (workerId > 0 && StringUtils.isNotBlank(tokenString)) {
			token = new Token(tokenString, workerId);
		}
		return token;
	}

	public static boolean isInLogin() {
		return getLoginToken() != null;
	}

	public static String getLoginTokenString() {
		Token token = getLoginToken();
		return token != null ? token.getTokenString() : null;
	}

	public static long getWorkerId() {
		Token token = getLoginToken();
		return token != null ? token.getWorkerId() : 0;
	}

}
