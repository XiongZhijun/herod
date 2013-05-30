/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.framework.bean;

import java.lang.reflect.Field;

import org.herod.framework.utils.BeanUtils;
import org.herod.framework.utils.ClassUtils;

/**
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 * 
 */
public class BeanWrapper {
	private Object bean;
	private Class<?> clazz;

	public BeanWrapper(Object bean) {
		super();
		this.bean = bean;
		this.clazz = bean.getClass();
	}

	public Object getPropertyValue(String propertyName) {
		int index = propertyName.indexOf('.');
		if (index < 0) {
			return BeanUtils.getFieldValue(bean, propertyName);
		}
		String level1 = propertyName.substring(0, index);
		Object value = BeanUtils.getFieldValue(bean, level1);
		if (value == null) {
			return null;
		}
		String level2 = propertyName
				.substring(index + 1, propertyName.length());
		return new BeanWrapper(value).getPropertyValue(level2);
	}

	public void setPropertyValue(String propertyName, ValueGetter valueGetter) {
		int index = propertyName.indexOf('.');
		if (index < 0) {
			Field field = ClassUtils.findField(clazz, propertyName);
			if (field == null) {
				return;
			}
			Object value = valueGetter.getValue(field);
			BeanUtils.setFieldValue(bean, field, value);
			return;
		}
		String level1 = propertyName.substring(0, index);
		Field field = ClassUtils.findField(clazz, level1);
		if (field == null) {
			return;
		}
		Object value = BeanUtils.getFieldValue(bean, field);
		if (value == null) {
			value = BeanUtils.createInstance(field.getType());
		}
		String level2 = propertyName
				.substring(index + 1, propertyName.length());
		new BeanWrapper(value).setPropertyValue(level2, valueGetter);
	}

	public static interface ValueGetter {
		Object getValue(Field field);
	}

}
