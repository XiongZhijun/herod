/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.order.web.pms;

import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.herod.common.das.HerodSingleColumnRowMapper;
import org.herod.order.web.pms.SimpleUserService.UserType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

import com.fpi.bear.db.NodeAccessService;
import com.fpi.bear.db.impl.NodeImpl;

/**
 * 
 * 
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 * 
 */
public class UserDao {
	private static final int DEFAULT_AGENT_ROLE_ID = 10001;
	@Autowired
	@Qualifier("simpleJdbcTemplate")
	private SimpleJdbcTemplate simpleJdbcTemplate;
	@Autowired
	private UserPasswordEncryptor userPasswordEncryptor;
	@Autowired
	private NodeAccessService nodeAccessService;
	private int agentRoleId = DEFAULT_AGENT_ROLE_ID;

	public void addUser(String name, String password, UserType type) {
		RowMapper<String> rm = new HerodSingleColumnRowMapper<String>();
		List<String> existsNames = simpleJdbcTemplate.query(
				"SELECT NAME FROM USERS WHERE NAME = ?", rm, name);
		if (CollectionUtils.isNotEmpty(existsNames)) {
			throw new RuntimeException("账户已经存在了，不能重复添加！");
		}
		NodeImpl user = new NodeImpl();
		user.setNodeTypeCode("USERS");
		user.set("NAME", name);
		user.set("PASSWORD", userPasswordEncryptor.encrypt(password));
		user.set("USER_TYPE", type.name());
		user.set("CREATETIME", new Date());
		Object id = nodeAccessService.add(user);
		simpleJdbcTemplate.update(
				"insert into users_roles (ROLEID,USERID) values (?,?)",
				agentRoleId, id);
	}

	public void setNodeAccessService(NodeAccessService nodeAccessService) {
		this.nodeAccessService = nodeAccessService;
	}

	public void setAgentRoleId(int agentRoleId) {
		this.agentRoleId = agentRoleId;
	}

	public void setSimpleJdbcTemplate(SimpleJdbcTemplate simpleJdbcTemplate) {
		this.simpleJdbcTemplate = simpleJdbcTemplate;
	}

	public void setUserPasswordEncryptor(
			UserPasswordEncryptor userPasswordEncryptor) {
		this.userPasswordEncryptor = userPasswordEncryptor;
	}
}
