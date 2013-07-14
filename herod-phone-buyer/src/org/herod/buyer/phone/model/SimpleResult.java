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
public class SimpleResult implements Result {
	private int code;
	private String content;

	public SimpleResult() {
		super();
	}

	public SimpleResult(int code) {
		super();
		this.code = code;
	}

	public SimpleResult(int code, String content) {
		super();
		this.code = code;
		this.content = content;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public int getCode() {
		return code;
	}

	@Override
	public String getContent() {
		return content;
	}

	@Override
	public boolean isSuccess() {
		return code == ResultCode.Success;
	}

}
