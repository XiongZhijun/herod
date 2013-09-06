/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved.
 */
package org.herod.order.order;

/**
 * 
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 */
public interface OrderCenter {

	void init();

	void submitOrder(long agentId, long workerId, String orderSN);

	void acceptOrder(long agentId, long workerId, String orderSN);

	void rejectOrder(long agentId, long workerId, String orderSN);

	void cancelOrder(long agentId, long workerId, String orderSN);

	void completeOrder(long agentId, long workerId, String orderSN);

	WorkerOrdersCount getOrdersCount(long agentId, long workerId);

	public static class WorkerOrdersCount {
		public int submitted = 0;
		public int acceptted = 0;
	}

}
