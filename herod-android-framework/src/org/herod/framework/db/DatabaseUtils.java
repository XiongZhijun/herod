/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.framework.db;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.herod.framework.bean.BeanWrapper;
import org.herod.framework.bean.BeanWrapper.ValueGetter;
import org.herod.framework.utils.BeanUtils;
import org.herod.framework.utils.StringUtils;

import android.content.ContentValues;
import android.database.Cursor;

/**
 * 
 * 
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 * 
 */
public class DatabaseUtils {
	private static SimpleDateFormat dateFormat = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	public static String format(Date date) {
		return dateFormat.format(date);
	}

	public static Date parse(String date) {
		if (StringUtils.isBlank(date)) {
			return null;
		}
		try {
			return dateFormat.parse(date);
		} catch (ParseException e) {
			return null;
		}
	}

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

	public static <T> List<T> toList(Cursor cursor, int begin, int end,
			Class<T> clazz) {
		boolean toBegin = false;
		if (begin < 0) {
			toBegin = cursor.moveToFirst();
		} else {
			toBegin = cursor.moveToPosition(begin);
		}
		if (!toBegin) {
			return Collections.emptyList();
		}
		if (end < 0) {
			end = cursor.getCount();
		}
		List<T> results = new ArrayList<T>();
		do {
			results.add(toBean(clazz, cursor));
		} while ((cursor.moveToNext() && cursor.getPosition() < end));
		return results;
	}

	public static <T> T toBean(Class<T> clazz, Cursor cursor) {
		T bean = BeanUtils.createInstance(clazz);
		BeanWrapper beanWrapper = new BeanWrapper(bean);
		for (String column : cursor.getColumnNames()) {
			ValueGetter valueGetter = new CursorValueGetter(cursor, column);
			String propertyName = ColumnNameUtils.toPropertyName(column);
			beanWrapper.setPropertyValue(propertyName, valueGetter);
		}
		return bean;
	}

}
