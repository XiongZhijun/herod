/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.order.web.pms;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.herod.common.das.HerodSingleColumnRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

import com.fpi.bear.permission.BearSecurityUtils;

/**
 * 
 * 
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 * 
 */
public class SimpleUserService implements UserService {
	@Autowired
	@Qualifier("simpleJdbcTemplate")
	private SimpleJdbcTemplate simpleJdbcTemplate;

	@Override
	public long getCurrentAgentId() {
		Object userName = BearSecurityUtils.getSecurityUser().getPrincipal();
		RowMapper<Long> rm = new HerodSingleColumnRowMapper<Long>(Long.class);
		List<Long> agentIds = simpleJdbcTemplate.query(
				"SELECT ID FROM ZRH_AGENT WHERE ADMIN_ACCOUNT = ?", rm,
				userName);
		if (CollectionUtils.isNotEmpty(agentIds)) {
			return agentIds.get(0);
		}
		return -1;
	}

	public void setSimpleJdbcTemplate(SimpleJdbcTemplate simpleJdbcTemplate) {
		this.simpleJdbcTemplate = simpleJdbcTemplate;
	}

	public static enum UserType {
		/** 管理员 */
		Admin,
		/** 代理商管理员 */
		AgentAdmin,
		/** 代理商配送人员 */
		AgentDeliveryWorker,
		/** 手机买家 */
		PhoneBuyer,
		/** 商店管理员 */
		ShopAdmin
	}
}
