/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.order.common.model;

/**
 * 订单状态
 * 
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 * 
 */
public enum OrderStatus {

	/** 未提交 */
	Unsubmit(0),
	/** 已提交 */
	Submitted(1),
	/** 已受理 */
	Acceptted(3),
	/** 已完成 */
	Completed(4),
	/** 已拒绝 */
	Rejected(2),
	/** 已取消 */
	Cancelled(5);
	private int value;

	private OrderStatus(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}
}
