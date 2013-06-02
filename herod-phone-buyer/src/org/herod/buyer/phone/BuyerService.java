/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.buyer.phone;

import java.util.List;

import org.herod.framework.MapWrapper;

/**
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 * 
 */
public interface BuyerService {

	List<MapWrapper<String, Object>> findShopTypes();

	List<MapWrapper<String, Object>> findShopesByType(long typeId);

	MapWrapper<String, Object> findShopById(long shopId);

	List<MapWrapper<String, Object>> findGoodsTypesByShop(long shopId);

	List<MapWrapper<String, Object>> findGoodsesByType(long goodsTypeId,
			long beginGoodsId, int count);
}
