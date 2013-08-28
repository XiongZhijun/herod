/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved.
 */
package org.herod.order.order;

import java.util.Map;

import org.herod.order.model.OrderStatus;

/**
 * 
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 */
public interface OrderCenter {

	void init();

	void submitOrder(long workerId, String orderSN);

	void acceptOrder(long workerId, String orderSN);

	void rejectOrder(long workerId, String orderSN);

	void cancelOrder(long workerId, String orderSN);

	void completeOrder(long workerId, String orderSN);

	Map<OrderStatus, Integer> getOrdersCount(long workerId);

}
