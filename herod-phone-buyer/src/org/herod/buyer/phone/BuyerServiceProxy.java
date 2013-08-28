/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.buyer.phone;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.herod.framework.MapWrapper;
import org.herod.order.common.Constants;
import org.herod.order.common.model.Order;
import org.herod.order.common.model.Result;

/**
 * 
 * 
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 * 
 */
public class BuyerServiceProxy implements BuyerService, ShopService {
	private Map<Long, MapWrapper<String, Object>> shopCache = new HashMap<Long, MapWrapper<String, Object>>();
	private BuyerService buyerService;

	public BuyerServiceProxy(BuyerService buyerService) {
		super();
		this.buyerService = buyerService;
	}

	@Override
	public List<MapWrapper<String, Object>> findShopTypes() {
		return buyerService.findShopTypes();
	}

	@Override
	public List<MapWrapper<String, Object>> findShopesByType(long typeId) {
		List<MapWrapper<String, Object>> shopes = buyerService
				.findShopesByType(typeId);
		if (shopCache.size() > 20) {
			shopCache.clear();
		}
		for (MapWrapper<String, Object> shop : shopes) {
			shopCache.put(shop.getLong(Constants.ID), shop);
		}
		return shopes;
	}

	@Override
	public MapWrapper<String, Object> findShopById(long shopId) {
		if (shopCache.containsKey(shopId)) {
			return shopCache.get(shopId);
		}
		return buyerService.findShopById(shopId);
	}

	@Override
	public List<MapWrapper<String, Object>> findGoodsTypesByShop(long shopId) {
		return buyerService.findGoodsTypesByShop(shopId);
	}

	@Override
	public List<MapWrapper<String, Object>> findGoodsesByType(long goodsTypeId,
			int begin, int count) {
		return buyerService.findGoodsesByType(goodsTypeId, begin, count);
	}

	@Override
	public List<MapWrapper<String, Object>> searchGoodses(String goodsName,
			int begin, int count) {
		return buyerService.searchGoodses(goodsName, begin, count);
	}

	@Override
	public String getTransactionSerialNumber() {
		return buyerService.getTransactionSerialNumber();
	}

	@Override
	public Result submitOrders(List<Order> orders) {
		return buyerService.submitOrders(orders);
	}

}
