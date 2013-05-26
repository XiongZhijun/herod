/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.buyer.phone.mocks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.herod.buyer.phone.BuyerService;

/**
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 * 
 */
public class BuyerServiceMock implements BuyerService {

	private ArrayList<Map<String, Object>> allShops;

	@Override
	public List<Map<String, Object>> findShopTypes() {
		List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
		data.add(createShopType(1, "外卖"));
		data.add(createShopType(2, "便利店"));
		data.add(createShopType(3, "蔬菜水果"));
		data.add(createShopType(4, "其它"));
		return data;
	}

	private HashMap<String, Object> createShopType(long id, String value) {
		HashMap<String, Object> item = new HashMap<String, Object>();
		item.put("name", value);
		item.put("id", id);
		return item;
	}

	@Override
	public List<Map<String, Object>> findShopesByType(long typeId) {
		allShops = new ArrayList<Map<String, Object>>();
		allShops.add(createShop(typeId * 100 + 1, "肯德基"));
		allShops.add(createShop(typeId * 100 + 2, "麦当劳"));
		for (int i = 3; i < 10; i++) {
			allShops.add(createShop(typeId * 100 + i, "外婆家-" + i + "号店"));
		}
		return allShops;
	}

	private HashMap<String, Object> createShop(long id, String name) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("id", id);
		map.put("name", name);
		return map;
	}

	@Override
	public List<Map<String, Object>> findGoodsTypesByShop(long shopId) {
		List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
		data.add(createGoodsType(shopId * 10 + 1, "烟酒"));
		data.add(createGoodsType(shopId * 10 + 2, "饮料"));
		data.add(createGoodsType(shopId * 10 + 3, "办公用品"));
		data.add(createGoodsType(shopId * 10 + 4, "日用百货"));
		data.add(createGoodsType(shopId * 10 + 5, "五金"));
		data.add(createGoodsType(shopId * 10 + 6, "建材"));
		return data;
	}

	private Map<String, Object> createGoodsType(long id, String name) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("id", id);
		map.put("name", name);
		return map;
	}

	@Override
	public List<Map<String, Object>> findGoodsesByType(long goodsTypeId,
			long beginGoodsId, int count) {
		List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
		for (long i = beginGoodsId + 1; i < beginGoodsId + 1 + count; i++) {
			data.add(createGoods(goodsTypeId * 1000 + i, goodsTypeId + "-商品-"
					+ i));
		}
		return data;
	}

	private Map<String, Object> createGoods(long id, String name) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("id", id);
		map.put("name", name);
		map.put("price", 5.0d);
		return map;
	}

	@Override
	public Map<String, Object> findShopById(long shopId) {
		for (Map<String, Object> shop : allShops) {
			if (shopId == (Long) shop.get("id")) {
				return shop;
			}
		}
		return null;
	}

}
