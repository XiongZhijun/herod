/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.common.das;

import org.springframework.jdbc.core.ColumnMapRowMapper;

/**
 * 
 * 
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 * 
 */
public class HerodColumnMapRowMapper extends ColumnMapRowMapper {

	protected String getColumnKey(String columnName) {
		return ColumnNameUtils.toPropertyName(columnName);
	}
}
