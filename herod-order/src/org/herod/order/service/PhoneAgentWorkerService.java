/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.order.service;

import java.util.List;

import org.herod.order.model.Order;
import org.herod.order.model.OrderUpdateInfo;
import org.herod.order.model.Token;

/**
 * 代理商工作人员手机用户服务
 * 
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 * 
 */
public interface PhoneAgentWorkerService {

	/**
	 * 登录系统
	 * 
	 * @param name
	 *            登录名
	 * @param password
	 *            密码
	 * @param imei
	 *            手机的imei号
	 * @return
	 */
	Token login(String name, String password, String imei);

	List<Order> findWaitAcceptOrders();

	List<Order> findWaitCompleteOrders();

	List<Order> findCompletedOrders();

	List<Order> findCanceledOrders();

	/**
	 * 受理订单
	 * 
	 * @param serialNumber
	 * @return
	 */
	Result acceptOrder(String serialNumber);

	/**
	 * 更新订单
	 * 
	 * @param updateInfo
	 * @param reason
	 * @return
	 */
	Result updateOrder(OrderUpdateInfo updateInfo, String reason);

	/**
	 * 拒绝订单
	 * 
	 * @param serialNumber
	 * @param reason
	 * @return
	 */
	Result rejectOrder(String serialNumber, String reason);

	/**
	 * 取消订单
	 * 
	 * @param serialNumber
	 * @param reason
	 * @return
	 */
	Result cancelOrder(String serialNumber, String reason);

	/**
	 * 完成订单
	 * 
	 * @param serialNumber
	 * @return
	 */
	Result completeOrder(String serialNumber);
}
