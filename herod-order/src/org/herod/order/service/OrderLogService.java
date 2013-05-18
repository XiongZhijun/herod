/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.order.service;

import java.util.List;

import org.herod.order.model.Buyer;
import org.herod.order.model.Order;
import org.herod.order.model.OrderLog.Operation;
import org.herod.order.model.AgentWorker;

/**
 * 订单的日志服务，主要用来保存日志
 * 
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 * 
 */
public interface OrderLogService {

	/**
	 * 保存{@link AgentWorker}的操作日志
	 * 
	 * @param orderSerialNumber
	 * @param operation
	 * @param reason
	 */
	void agentWorkerlog(String orderSerialNumber, Operation operation,
			String reason);

	/**
	 * 保存{@link Buyer} 的提交日志
	 * 
	 * @param orders
	 */
	void buyerSubmitLog(List<Order> orders);
}
