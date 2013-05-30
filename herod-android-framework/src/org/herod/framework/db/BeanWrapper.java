/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.framework.db;

import org.herod.framework.utils.BeanUtils;

/**
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 * 
 */
public class BeanWrapper {
	private Object bean;

	public BeanWrapper(Object bean) {
		super();
		this.bean = bean;
	}

	public Object getPropertyValue(String propertyName) {
		int index = propertyName.indexOf('.');
		if (index <= 0) {
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

}
