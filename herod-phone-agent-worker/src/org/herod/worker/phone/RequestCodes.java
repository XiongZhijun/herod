/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved.
 */
package org.herod.worker.phone;

/**
 * 
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 */
public interface RequestCodes {
	int REQUEST_NEW_ORDER_ITEMS = 1;
	int REQUEST_CANCEL_ORDER = 2;
	int REQUEST_ORDER_ASYNC_OPERATE = 3;
	int REQUEST_UPDATE_ORDER = 4;

	int RESULT_SUCCESS = 1000;
	int RESULT_FAILED = 1001;

}
