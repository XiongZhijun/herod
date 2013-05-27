/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.buyer.phone;

import java.util.Map;

/**
 * 
 * 
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 * 
 */
public class MapUtils {

	public static double getDouble(Map<String, Object> map, String key) {
		Object value = map.get(key);
		if (value == null) {
			return 0.0d;
		}
		if (value instanceof Double || value.getClass().equals(double.class)) {
			return (Double) value;
		}
		return Double.parseDouble(value.toString());
	}

	public static int getInt(Map<String, Object> map, String key) {
		Object value = map.get(key);
		if (value == null) {
			return 0;
		}
		if (value instanceof Integer || value.getClass().equals(int.class)) {
			return (Integer) value;
		}
		return Integer.parseInt(value.toString());
	}

	public static String getString(Map<String, Object> map, String key) {
		Object value = map.get(key);
		if (value == null) {
			return null;
		}
		if (value instanceof String) {
			return (String) value;
		}
		return value.toString();
	}

}
