/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.order.service;

/**
 * 
 * 
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 * 
 */
public interface ResultCode {

	int Success = 1;

	int CurrentStatusCanNotDoSuchOperate = 2;

	int SomeOrderIsExists = 3;
}
