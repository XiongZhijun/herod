/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.buyer.phone;

import java.util.List;
import java.util.Map;

/**
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 * 
 */
public interface BuyerService {

	List<Map<String, Object>> findShopTypes();

	List<Map<String, Object>> findShopesByType(long typeId);

	List<Map<String, Object>> findGoodsTypesByShop(long shopId);

	List<Map<String, Object>> findGoodsesByType(long goodsTypeId,
			long beginGoodsId, int count);
}
