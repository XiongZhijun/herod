/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.order.model;

/**
 * 
 * 
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 * 
 */
public class Token {

	private String tokenString;
	private long workerId;

	public Token() {
		super();
	}

	public Token(String tokenString, long workerId) {
		super();
		this.tokenString = tokenString;
		this.workerId = workerId;
	}

	public String getTokenString() {
		return tokenString;
	}

	public void setTokenString(String tokenString) {
		this.tokenString = tokenString;
	}

	public long getWorkerId() {
		return workerId;
	}

	public void setWorkerId(long workerId) {
		this.workerId = workerId;
	}

}
