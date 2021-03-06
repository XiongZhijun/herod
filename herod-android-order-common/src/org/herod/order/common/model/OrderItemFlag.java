/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.order.common.model;

/**
 * 订单项标记
 * 
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 * 
 */
public enum OrderItemFlag {
	/** 普通状态，说明该订单项是正常的 */
	Common,
	/** 被更新过 */
	Updated,
	/** 该订单项处在缺货的状态 */
	Stockout,
	/** 说明订单项已经被删除 */
	Deleted,
	/** 新增的 */
	Added
}
