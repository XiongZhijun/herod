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
public interface Result {

	int getCode();

	String getContent();

	boolean isSuccess();

	Result SUCCESS = new Result() {
		public boolean isSuccess() {
			return true;
		}

		public String getContent() {
			return null;
		}

		public int getCode() {
			return ResultCode.Success;
		}
	};
}
