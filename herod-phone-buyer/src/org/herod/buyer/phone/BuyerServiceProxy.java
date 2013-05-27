/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.buyer.phone;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * 
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 * 
 */
public class BuyerServiceProxy implements BuyerService, ShopService {
	private Map<Long, Map<String, Object>> shopCache = new HashMap<Long, Map<String, Object>>();
	private BuyerService buyerService;

	public BuyerServiceProxy(BuyerService buyerService) {
		super();
		this.buyerService = buyerService;
	}

	@Override
	public List<Map<String, Object>> findShopTypes() {
		return buyerService.findShopTypes();
	}

	@Override
	public List<Map<String, Object>> findShopesByType(long typeId) {
		List<Map<String, Object>> shopes = buyerService
				.findShopesByType(typeId);
		if (shopCache.size() > 20) {
			shopCache.clear();
		}
		for (Map<String, Object> shop : shopes) {
			shopCache.put((Long) shop.get(Constants.ID), shop);
		}
		return shopes;
	}

	@Override
	public Map<String, Object> findShopById(long shopId) {
		if (shopCache.containsKey(shopId)) {
			return shopCache.get(shopId);
		}
		return buyerService.findShopById(shopId);
	}

	@Override
	public List<Map<String, Object>> findGoodsTypesByShop(long shopId) {
		return buyerService.findGoodsTypesByShop(shopId);
	}

	@Override
	public List<Map<String, Object>> findGoodsesByType(long goodsTypeId,
			long beginGoodsId, int count) {
		return buyerService.findGoodsesByType(goodsTypeId, beginGoodsId, count);
	}

}
