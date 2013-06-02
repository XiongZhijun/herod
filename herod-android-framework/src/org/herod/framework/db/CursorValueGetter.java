/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.framework.db;

import java.lang.reflect.Field;
import java.util.Date;

import org.codehaus.jackson.map.deser.EnumResolver;
import org.herod.framework.bean.BeanWrapper.ValueGetter;

import android.database.Cursor;

/**
 * 
 * 
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 * 
 */
public class CursorValueGetter implements ValueGetter {

	private Cursor cursor;
	private String columnName;

	public CursorValueGetter(Cursor cursor, String columnName) {
		super();
		this.cursor = cursor;
		this.columnName = columnName;
	}

	@Override
	public Object getValue(Field field) {
		Class<?> fieldType = field.getType();
		Object value = null;
		int columnIndex = cursor.getColumnIndex(columnName);
		if (fieldType.equals(Long.class) || fieldType.equals(long.class)) {
			value = cursor.getLong(columnIndex);
		} else if (fieldType.equals(Short.class)
				|| fieldType.equals(short.class)) {
			value = cursor.getShort(columnIndex);
		} else if (fieldType.equals(Integer.class)
				|| fieldType.equals(int.class)) {
			value = cursor.getInt(columnIndex);
		} else if (fieldType.equals(Double.class)
				|| fieldType.equals(double.class)) {
			value = cursor.getDouble(columnIndex);
		} else if (fieldType.equals(Float.class)
				|| fieldType.equals(float.class)) {
			value = cursor.getFloat(columnIndex);
		} else if (fieldType.equals(Boolean.class)
				|| fieldType.equals(boolean.class)) {
			String str = cursor.getString(columnIndex);
			value = Boolean.parseBoolean(str);
		} else if (fieldType.equals(Date.class)) {
			String str = cursor.getString(columnIndex);
			value = DatabaseUtils.parse(str);
		} else if (fieldType.equals(String.class)) {
			value = cursor.getString(columnIndex);
		} else if (Enum.class.isAssignableFrom(fieldType)) {
			return EnumResolver.constructUnsafeUsingToString(fieldType)
					.findEnum(cursor.getString(columnIndex));
		}
		return value;
	}

}
