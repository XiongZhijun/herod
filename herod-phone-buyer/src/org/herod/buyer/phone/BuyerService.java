/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.buyer.phone;

import java.util.List;

import org.herod.buyer.phone.model.Order;
import org.herod.buyer.phone.model.Result;
import org.herod.framework.MapWrapper;

/**
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 * 
 */
public interface BuyerService {

	String getTransactionSerialNumber();

	List<MapWrapper<String, Object>> findShopTypes();

	List<MapWrapper<String, Object>> findShopesByType(long typeId);

	MapWrapper<String, Object> findShopById(long shopId);

	List<MapWrapper<String, Object>> findGoodsTypesByShop(long shopId);

	List<MapWrapper<String, Object>> findGoodsesByType(long goodsTypeId,
			int begin, int count);

	List<MapWrapper<String, Object>> searchGoodses(String goodsName, int begin,
			int count);

	Result submitOrders(List<Order> orders);
}
