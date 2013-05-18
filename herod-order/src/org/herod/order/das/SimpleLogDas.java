/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.order.das;

import java.util.Arrays;
import java.util.List;

import org.herod.common.das.HerodBeanPropertySqlParameterSource;
import org.herod.order.model.OrderLog;
import org.herod.order.service.SimpleOrderLogService.LogDas;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

/**
 * 
 * 
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 * 
 */
public class SimpleLogDas implements LogDas {
	private static final String INSERT_LOGS_SQL = "INSERT INTO HEROD_ORDER_LOGS (ORDER_SERIAL_NUMBER,OPERATION,REASON,OPERATOR_TYPE,OPERATOR,OPERATE_TIME) "
			+ "VALUES (:orderSerialNumber,:operation,:reason,:operatorType,:operator,:operateTime) ";
	@Autowired
	@Qualifier("simpleJdbcTemplate")
	private SimpleJdbcTemplate simpleJdbcTemplate;

	@Override
	public void addLog(OrderLog log) {
		addLogs(Arrays.asList(log));
	}

	@Override
	public void addLogs(List<OrderLog> logs) {
		SqlParameterSource[] batchArgs = new SqlParameterSource[logs.size()];
		for (int i = 0; i < logs.size(); i++) {
			batchArgs[i] = new HerodBeanPropertySqlParameterSource(logs.get(i));
		}
		simpleJdbcTemplate.batchUpdate(INSERT_LOGS_SQL, batchArgs);
	}

	public void setSimpleJdbcTemplate(SimpleJdbcTemplate simpleJdbcTemplate) {
		this.simpleJdbcTemplate = simpleJdbcTemplate;
	}

}
