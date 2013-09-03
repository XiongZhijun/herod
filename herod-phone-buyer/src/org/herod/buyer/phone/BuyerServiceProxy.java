/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.buyer.phone;

import static org.herod.order.common.Constants.ID;
import static org.herod.order.common.Constants.TIMESTAMP;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.herod.framework.MapWrapper;
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
	private Map<Long, Long> latestShopTimestampMap = new HashMap<Long, Long>();

	public BuyerServiceProxy(BuyerService buyerService) {
		super();
		this.buyerService = buyerService;
	}

	@Override
	public List<MapWrapper<String, Object>> findShopTypes() {
		return buyerService.findShopTypes();
	}

	@Override
	public List<MapWrapper<String, Object>> findShopesByType(long typeId,
			long timestamp) {
		List<MapWrapper<String, Object>> shopes = buyerService
				.findShopesByType(typeId, timestamp);
		if (shopCache.size() > 100) {
			shopCache.clear();
			latestShopTimestampMap.clear();
		}
		for (MapWrapper<String, Object> shop : shopes) {
			long shopId = shop.getLong(ID);
			shopCache.put(shopId, shop);
			latestShopTimestampMap.put(shopId, shop.getLong(TIMESTAMP));
		}
		return shopes;
	}

	@Override
	public MapWrapper<String, Object> findShopById(long shopId, long timestamp) {
		if (shopCache.containsKey(shopId)) {
			return shopCache.get(shopId);
		}
		latestShopTimestampMap.put(shopId, shopId);
		return buyerService.findShopById(shopId, timestamp);
	}

	@Override
	public List<MapWrapper<String, Object>> findGoodsTypesByShop(long shopId,
			long timestamp) {
		return buyerService.findGoodsTypesByShop(shopId, timestamp);
	}

	@Override
	public List<MapWrapper<String, Object>> findGoodsesByType(long goodsTypeId,
			int begin, int count, long timestamp) {
		return buyerService.findGoodsesByType(goodsTypeId, begin, count,
				timestamp);
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

	@Override
	public Map<String, Order> findOrders(Set<String> serialNumbers) {
		return buyerService.findOrders(serialNumbers);
	}

	@Override
	public MapWrapper<String, Object> findShopById(long shopId) {
		if (shopCache.containsKey(shopId)) {
			return shopCache.get(shopId);
		}
		long timestamp = latestShopTimestampMap.containsKey(shopId) ? latestShopTimestampMap
				.get(shopId) : 0;
		return findShopById(shopId, timestamp);
	}

}
