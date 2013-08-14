/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved.
 */
package org.herod.worker.phone;

import java.util.List;

import org.herod.framework.MapWrapper;
import org.herod.order.common.model.Order;
import org.herod.order.common.model.Result;
import org.herod.worker.phone.model.OrderUpdateInfo;
import org.herod.worker.phone.model.Token;

/**
 * 
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 */
public interface WorkerService {

	Token login(String phone, String password);

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
	Result updateOrder(OrderUpdateInfo updateInfo);

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

	List<MapWrapper<String, Object>> findGoodsTypesByShop(long shopId);

	List<MapWrapper<String, Object>> findGoodsesByType(long goodsTypeId,
			int begin, int count);
}
