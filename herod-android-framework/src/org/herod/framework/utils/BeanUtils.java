/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.framework.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.herod.framework.AndroidException;

import android.util.Log;

/**
 * Pojo反射的工具类。
 * 
 * @author Xiong Zhijun
 * 
 */
public abstract class BeanUtils {

	/**
	 * 设置字段的值。
	 * 
	 * @param target
	 *            待设置的对象
	 * @param field
	 *            待设置的对象字段
	 * @param value
	 *            值
	 */
	public static void setFieldValue(Object target, Field field, Object value) {
		try {
			field.setAccessible(true);
			field.set(target, value);
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
			throw new AndroidException("set field value failed. class is "
					+ target.getClass() + ", field is " + field.getName(), e);
		}
	}

	/**
	 * 读取字段的值
	 * 
	 * @param target
	 *            待读取的对象
	 * @param fieldName
	 *            字段的名称
	 * @return 字段的值
	 */
	public static Object getFieldValue(Object target, String fieldName) {
		if (target == null) {
			return null;
		}
		if (fieldName.contains(".")) {
			String[] splits = fieldName.split("\\.");
			Object value = target;
			for (int i = 0; i < splits.length; i++) {
				value = getFieldValue(value, splits[i]);
				if (value == null) {
					return null;
				}
			}
			return value;
		}
		Field field = ClassUtils.findField(target.getClass(), fieldName);
		if (field == null) {
			Log.w(BeanUtils.class.getName(), "Do not find field : " + fieldName);
			return null;
		}
		return getFieldValue(target, field);
	}

	public static Object getFieldValue(Object target, Field field) {
		try {
			field.setAccessible(true);
			return field.get(target);
		} catch (RuntimeException e) {
			String msg = "Get field " + field.getName() + " value from "
					+ target + " failed.";
			Log.w(BeanUtils.class.getName(), msg, e);
			throw e;
		} catch (Exception e) {
			String msg = "Get field " + field.getName() + " value from "
					+ target + " failed.";
			Log.w(BeanUtils.class.getName(), msg, e);
			throw new AndroidException(msg, e);
		}
	}

	/**
	 * 读取属性的值。
	 * 
	 * @param target
	 * @param propertyName
	 * @return
	 */
	public static Object getProperty(Object target, String propertyName) {
		if (target == null || StringUtils.isBlank(propertyName)) {
			return null;
		}
		Class<?> clazz = target.getClass();
		Method getter = ClassUtils.findGetter(clazz, propertyName);
		if (getter != null) {
			try {
				return getter.invoke(target);
			} catch (RuntimeException e) {
				throw e;
			} catch (Exception e) {
				throw new ClassException(StringUtils.join("Get property",
						propertyName + "'s value from target",
						target.toString() + "failed"));
			}
		}
		return getFieldValue(target, propertyName);
	}

	/**
	 * 根据class name用默认构造函数创建对象。
	 * 
	 * @param className
	 * @return
	 */
	public static Object createBean(String className) {
		Class<?> clazz = ClassUtils.forName(className);
		return createInstance(clazz);
	}

	/**
	 * 根据class用默认构造函数创建对象
	 * 
	 * @param clazz
	 * @return
	 */
	public static <T> T createInstance(Class<T> clazz) {
		try {
			return clazz.newInstance();
		} catch (RuntimeException e) {
			String msg = "Create bean for class '" + clazz.getName()
					+ "' failed.";
			Log.w(BeanUtils.class.getName(), msg, e);
			throw e;
		} catch (Exception e) {
			String msg = "Create bean for class '" + clazz.getName()
					+ "' failed.";
			Log.w(BeanUtils.class.getName(), msg, e);
			throw new AndroidException(msg, e);
		}
	}

	public static Object invoke(Object target, Method method, Object... args) {
		try {
			return method.invoke(target, args);
		} catch (IllegalArgumentException e) {
			throw e;
		} catch (Exception e) {
			throw new AndroidException(target + " invoke method "
					+ method.getName() + " failed.", e);
		}
	}
}
