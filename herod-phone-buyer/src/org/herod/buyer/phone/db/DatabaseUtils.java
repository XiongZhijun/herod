/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.buyer.phone.db;

import java.util.List;

import org.herod.buyer.phone.model.Order;
import org.herod.framework.utils.BeanUtils;

import android.content.ContentValues;

/**
 * 
 * 
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 * 
 */
public class DatabaseUtils {

	public static ContentValues toContentValues(Order order,
			List<String> columns) {
		ContentValues values = new ContentValues();
		for (String column : columns) {
			String propertyName = ColumnNameUtils.toPropertyName(column);
			Object value = BeanUtils.getFieldValue(order, propertyName);
		}
		return values;
	}
}
