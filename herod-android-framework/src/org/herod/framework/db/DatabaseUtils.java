/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.framework.db;

import java.util.List;

import android.content.ContentValues;

/**
 * 
 * 
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 * 
 */
public class DatabaseUtils {

	public static ContentValues toContentValues(Object bean,
			List<String> columns) {
		ContentValues values = new ContentValues();
		BeanWrapper beanWrapper = new BeanWrapper(bean);
		for (String column : columns) {
			put(values, beanWrapper, column);
		}
		return values;
	}

	private static void put(ContentValues values, BeanWrapper beanWrapper,
			String column) {
		String propertyName = ColumnNameUtils.toPropertyName(column);
		Object value = beanWrapper.getPropertyValue(propertyName);
		if (value == null) {
			return;
		} else if (value instanceof Integer
				|| value.getClass().equals(int.class)) {
			values.put(column, (Integer) value);
		} else if (value instanceof Long || value.getClass().equals(long.class)) {
			values.put(column, (Long) value);
		} else if (value instanceof Short
				|| value.getClass().equals(short.class)) {
			values.put(column, (Short) value);
		} else if (value instanceof Double
				|| value.getClass().equals(double.class)) {
			values.put(column, (Double) value);
		} else if (value instanceof Float
				|| value.getClass().equals(float.class)) {
			values.put(column, (Float) value);
		} else {
			values.put(column, value.toString());
		}
	}
}
