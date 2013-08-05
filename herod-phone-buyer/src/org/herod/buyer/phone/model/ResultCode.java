/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.buyer.phone.model;

/**
 * 
 * 
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 * 
 */
public interface ResultCode {

	int Failed = 0;

	int Success = 1;

	int CurrentStatusCanNotDoSuchOperate = 2;

	int SomeOrderIsExists = 3;

	int NoOrderSubmitted = 4;
}
