/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.order.web;

import org.herod.order.service.SimpleUserService.UserType;
import org.herod.order.web.pms.UserDao;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 
 * 
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 * 
 */
public class UserAccessService {
	private static final String DEFAULT_PASSWORD = "123456";
	@Autowired
	private UserDao userDao;

	public void createAgentUser(String account) {
		userDao.addUser(account, DEFAULT_PASSWORD, UserType.AgentAdmin);
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}
}
