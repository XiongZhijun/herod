/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved.
 */
package org.herod.order.common;

import static org.herod.framework.Constants.MINUS;

/**
 * 
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 */
public class SerialNumberUtils {

	public static String buildOrderSerialNumber(String transactionSN,
			long shopId) {
		return join(transactionSN, shopId);
	}

	public static String buildOrderItemSerialNumber(String orderSN, long goodsId) {
		return join(orderSN, goodsId);
	}

	private static String join(Object... args) {
		StringBuilder sb = new StringBuilder();
		for (Object arg : args) {
			if (sb.length() > 0) {
				sb.append(MINUS);
			}
			sb.append(arg);
		}
		return sb.toString();
	}
}
