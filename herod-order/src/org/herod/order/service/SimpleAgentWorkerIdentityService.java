/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.order.service;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * 
 * 
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 * 
 */
public class SimpleAgentWorkerIdentityService implements
		AgentWorkerIdentityService {
	@Autowired
	private UserService userService;

	@Override
	public long getCurrentWorkerId() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getCurrentWorkerName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getCurrentWorkerAgentId() {
		// TODO 这个需要重新实现的。
		return userService.getCurrentAgentId();
	}

}
