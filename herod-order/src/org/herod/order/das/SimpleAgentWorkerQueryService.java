/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved.
 */
package org.herod.order.das;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.herod.order.service.SimpleLoginService.AgentWorkerQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

/**
 * 
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 */
public class SimpleAgentWorkerQueryService implements AgentWorkerQueryService {
	@Autowired
	@Qualifier("simpleJdbcTemplate")
	private SimpleJdbcTemplate simpleJdbcTemplate;

	@Override
	public Map<String, Object> findWorkerByNameAndPassword(String name,
			String password) {
		List<Map<String, Object>> result = simpleJdbcTemplate.queryForList(
				"SELECT ID, NAME, AGENT_ID FROM ZRH_AGENT_DELIVERY_WORKER "
						+ "WHERE PHONE = ? AND PASSWORD = ? AND FLAG = 1",
				name, password);
		if (CollectionUtils.isEmpty(result)) {
			return null;
		}
		return result.get(0);
	}

	public void setSimpleJdbcTemplate(SimpleJdbcTemplate simpleJdbcTemplate) {
		this.simpleJdbcTemplate = simpleJdbcTemplate;
	}

}
