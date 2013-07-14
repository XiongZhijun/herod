/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.buyer.phone.mocks;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.herod.buyer.phone.BuyerService;
import org.herod.buyer.phone.model.Order;
import org.herod.buyer.phone.model.Result;
import org.herod.framework.MapWrapper;
import org.herod.framework.utils.StringUtils;

/**
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 * 
 */
public class BuyerServiceMock implements BuyerService {

	private List<MapWrapper<String, Object>> allShops = new ArrayList<MapWrapper<String, Object>>();
	private List<MapWrapper<String, Object>> allGoodsTypes = new ArrayList<MapWrapper<String, Object>>();
	private List<MapWrapper<String, Object>> allGoodses = new ArrayList<MapWrapper<String, Object>>();

	@Override
	public List<MapWrapper<String, Object>> findShopTypes() {
		List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
		data.add(createShopType(1, "外卖"));
		data.add(createShopType(2, "便利店"));
		data.add(createShopType(3, "蔬菜水果"));
		data.add(createShopType(4, "其它"));

		return toMapWrapperList(data);
	}

	private HashMap<String, Object> createShopType(long id, String value) {
		HashMap<String, Object> item = new HashMap<String, Object>();
		item.put("name", value);
		item.put("id", id);
		return item;
	}

	@Override
	public List<MapWrapper<String, Object>> findShopesByType(long typeId) {
		List<MapWrapper<String, Object>> shops = new ArrayList<MapWrapper<String, Object>>();
		shops.add(createShop(typeId * 100 + 1, "肯德基"));
		shops.add(createShop(typeId * 100 + 2, "麦当劳"));
		for (int i = 3; i < 10; i++) {
			shops.add(createShop(typeId * 100 + i, "外婆家-" + i + "号店"));
		}

		return shops;
	}

	private MapWrapper<String, Object> createShop(long id, String name) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("id", id);
		map.put("name", name);
		if (id % 3 == 0) {
			map.put("imageUrl",
					"http://a4.att.hudong.com/71/29/01300000180461121448296826222_s.jpg");
		} else if (id % 3 == 1) {
			map.put("imageUrl",
					"http://z.abang.com/d/hangzhoucai/1/0/i/3/-/-/DSC04097-.jpg");
		} else if (id % 3 == 2) {
			map.put("imageUrl",
					"http://www.sj998.com/uploads/allimg/130201/102552-130201103419627.jpg");
		}
		map.put("minChargeForFreeDelivery", id * 1.0d);
		map.put("costOfRunErrands", 5d);
		map.put("phone", "15990196179");
		MapWrapper<String, Object> shop = new MapWrapper<String, Object>(map);
		addShopToCache(shop);
		return shop;
	}

	private void addShopToCache(MapWrapper<String, Object> shop) {
		for (MapWrapper<String, Object> tmp : allShops) {
			if (tmp.getLong("id") == shop.getLong("id")) {
				return;
			}
		}
		allShops.add(shop);
	}

	@Override
	public List<MapWrapper<String, Object>> findGoodsTypesByShop(long shopId) {
		List<MapWrapper<String, Object>> data = new ArrayList<MapWrapper<String, Object>>();
		data.add(createGoodsType(shopId * 10 + 1, "烟酒", shopId));
		data.add(createGoodsType(shopId * 10 + 2, "饮料", shopId));
		data.add(createGoodsType(shopId * 10 + 3, "办公用品", shopId));
		data.add(createGoodsType(shopId * 10 + 4, "日用百货", shopId));
		data.add(createGoodsType(shopId * 10 + 5, "五金", shopId));
		data.add(createGoodsType(shopId * 10 + 6, "建材", shopId));
		return data;
	}

	private MapWrapper<String, Object> createGoodsType(long id, String name,
			long shopId) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("id", id);
		map.put("name", name);
		map.put("shopId", shopId);
		MapWrapper<String, Object> goodsType = new MapWrapper<String, Object>(
				map);
		addGoodsTypeToCache(goodsType);
		return goodsType;
	}

	private void addGoodsTypeToCache(MapWrapper<String, Object> goodsType) {
		for (MapWrapper<String, Object> tmp : allGoodsTypes) {
			if (tmp.getLong("id") == goodsType.getLong("id")) {
				return;
			}
		}
		allGoodsTypes.add(goodsType);
	}

	@Override
	public List<MapWrapper<String, Object>> findGoodsesByType(long goodsTypeId,
			int begin, int count) {
		List<MapWrapper<String, Object>> data = new ArrayList<MapWrapper<String, Object>>();
		for (long i = begin + 1; i < begin + 1 + count; i++) {
			data.add(createGoods(goodsTypeId * 1000 + i, goodsTypeId + "-商品-"
					+ i, goodsTypeId));
		}
		return data;
	}

	private MapWrapper<String, Object> createGoods(long id, String name,
			long goodsTypeId) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("id", id);
		map.put("name", name);
		map.put("price", 5.0d);
		map.put("goodsTypeId", goodsTypeId);
		MapWrapper<String, Object> shop = findShopByGoodsType(goodsTypeId);
		map.put("shopId", shop.getLong("id"));
		map.put("shopName", shop.getString("name"));
		MapWrapper<String, Object> goods = new MapWrapper<String, Object>(map);
		addGoodsToCache(goods);
		return goods;
	}

	private MapWrapper<String, Object> findShopByGoodsType(long goodsTypeId) {
		long shopId = 0;
		for (MapWrapper<String, Object> type : allGoodsTypes) {
			if (type.getLong("id") == goodsTypeId) {
				shopId = type.getLong("shopId");
			}
		}
		for (MapWrapper<String, Object> shop : allShops) {
			if (shop.getLong("id") == shopId) {
				return shop;
			}
		}
		return new MapWrapper<String, Object>(new HashMap<String, Object>());
	}

	private void addGoodsToCache(MapWrapper<String, Object> goods) {
		for (MapWrapper<String, Object> tmp : allGoodses) {
			if (tmp.getLong("id") == goods.getLong("id")) {
				return;
			}
		}
		allGoodses.add(goods);
	}

	@Override
	public MapWrapper<String, Object> findShopById(long shopId) {
		for (MapWrapper<String, Object> shop : allShops) {
			if (shopId == shop.getLong("id")) {
				return shop;
			}
		}
		return null;
	}

	private List<MapWrapper<String, Object>> toMapWrapperList(
			List<Map<String, Object>> mapList) {
		List<MapWrapper<String, Object>> result = new ArrayList<MapWrapper<String, Object>>();
		for (Map<String, Object> map : mapList) {
			result.add(new MapWrapper<String, Object>(map));
		}
		return result;
	}

	@Override
	public List<MapWrapper<String, Object>> searchGoodses(String goodsName,
			int begin, int count) {
		if (StringUtils.isBlank(goodsName)) {
			return Collections.emptyList();
		}
		ArrayList<MapWrapper<String, Object>> list = new ArrayList<MapWrapper<String, Object>>();
		for (MapWrapper<String, Object> goods : allGoodses) {
			if (goods.getString("name").indexOf(goodsName) >= 0) {
				list.add(goods);
			}
		}
		int end = begin + count;
		if (begin >= list.size()) {
			return Collections.emptyList();
		}
		if (end > list.size()) {
			end = list.size();
		}
		return list.subList(begin, end);
	}

	@Override
	public String getTransactionSerialNumber() {
		return "1";
	}

	@Override
	public Result submitOrders(List<Order> orders) {
		// TODO Auto-generated method stub
		return null;
	}

}
