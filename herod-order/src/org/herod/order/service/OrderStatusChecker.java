/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.order.service;

import org.herod.order.model.OrderStatus;

/**
 * 订单状态检验工具对象，用来判断该订单能否进行相应的操作。
 * 
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 * 
 */
public interface OrderStatusChecker {

	/**
	 * 判断订单能否更新状态到目的状态
	 * 
	 * @param serialNumber
	 *            订单流水号
	 * @param destStatus
	 *            目标状态
	 * @return
	 */
	boolean canNotChangeStatus(String serialNumber, OrderStatus destStatus);

	/**
	 * 判断订单能否进行更新
	 * 
	 * @param serialNumber
	 *            订单流水号
	 * @return
	 */
	boolean canNotUpdate(String serialNumber);
}
