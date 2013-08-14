/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved.
 */
package org.herod.order.common;

/**
 * 
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 */
public class OrderContext {

	private static String imageServerHost;
	private static int imageServerPort;

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
