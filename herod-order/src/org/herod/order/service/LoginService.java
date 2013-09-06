/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved.
 */
package org.herod.order.service;

import org.herod.order.model.Token;

/**
 * 
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 */
public interface LoginService {

	Token doLogin(String name, String password, String imei);

	long getWorkerAgentId(String token, String imei);

	long getWorkerAgentId(long workerId);

	long getWorkerId(String token, String imei);

	boolean isUserValid(String token, String imei);

}