/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved.
 */
package org.herod.worker.phone;

import org.herod.worker.phone.model.Token;
import org.herod.worker.phone.rest.RestWorkerService;

import android.content.Context;

/**
 * 
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 */
public abstract class WorkerContext {
	private static WorkerService workerService;
	private static String restServerHost;
	private static int restServerPort;
	private static String restUserName;
	private static String restPassword;
	private static Token loginToken;

	public static void init(Context context) {
		workerService = new RestWorkerService(context);
		restServerHost = context.getString(R.string.RestServerHost);
		restServerPort = Integer.parseInt(context
				.getString(R.string.RestServerPort));
		restUserName = context.getString(R.string.RestUserName);
		restPassword = context.getString(R.string.RestPassword);
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

	public static String getRestUserName() {
		return restUserName;
	}

	public static String getRestPassword() {
		return restPassword;
	}

	public static void setLoginToken(Token token) {
		WorkerContext.loginToken = token;
	}

	public static Token getLoginToken() {
		return loginToken;
	}

}
