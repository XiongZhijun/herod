/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.worker.phone.model;

/**
 * 订单状态
 * 
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 * 
 */
public enum OrderStatus {
	/** 未提交 */
	Unsubmit,
	/** 已提交 */
	Submitted,
	/** 已受理 */
	Acceptted,
	/** 已完成 */
	Completed,
	/** 已拒绝 */
	Rejected,
	/** 已取消 */
	Cancelled
}
