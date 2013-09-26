/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved.
 */
package org.herod.order.common;

import android.content.Context;

/**
 * 
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 */
public class OrderContext {
	private static String restServerHost;
	private static int restServerPort;
	private static String imageServerHost;
	private static int imageServerPort;

	public static void init(Context context) {
		restServerHost = context.getString(R.string.RestServerHost);
		restServerPort = Integer.parseInt(context
				.getString(R.string.RestServerPort));
		imageServerHost = context.getString(R.string.ImageServerHost);
		imageServerPort = Integer.parseInt(context
				.getString(R.string.ImageServerPort));

		OrderContext.setImageServerHost(imageServerHost);
		OrderContext.setImageServerPort(imageServerPort);
	}

	public static String getRestServerHost() {
		return restServerHost;
	}

	public static void setRestServerHost(String restServerHost) {
		OrderContext.restServerHost = restServerHost;
	}

	public static int getRestServerPort() {
		return restServerPort;
	}

	public static void setRestServerPort(int restServerPort) {
		OrderContext.restServerPort = restServerPort;
	}

	public static String getImageServerHost() {
		return imageServerHost;
	}

	public static void setImageServerHost(String imageServerHost) {
		OrderContext.imageServerHost = imageServerHost;
	}

	public static int getImageServerPort() {
		return imageServerPort;
	}

	public static void setImageServerPort(int imageServerPort) {
		OrderContext.imageServerPort = imageServerPort;
	}

}
