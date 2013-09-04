/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.buyer.phone;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.herod.framework.MapWrapper;
import org.herod.order.common.model.Order;
import org.herod.order.common.model.Result;

/**
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 * 
 */
public interface BuyerService {

	String getTransactionSerialNumber();

	List<MapWrapper<String, Object>> findShopTypes();

	List<MapWrapper<String, Object>> findShopesByType(long typeId);

	MapWrapper<String, Object> findShopById(long shopId, long timestamp);

	List<MapWrapper<String, Object>> findGoodsTypesByShop(long shopId,
			long timestamp);

	List<MapWrapper<String, Object>> findGoodsesByType(long goodsTypeId,
			int begin, int count, long timestamp);

	List<MapWrapper<String, Object>> searchGoodses(String goodsName, int begin,
			int count);

	Result submitOrders(List<Order> orders);

	Map<String, Order> findOrders(Set<String> serialNumbers);
}
