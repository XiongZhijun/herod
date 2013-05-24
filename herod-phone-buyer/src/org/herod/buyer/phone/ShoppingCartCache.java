/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.buyer.phone;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 * 
 */
public class ShoppingCartCache {
	private static ShoppingCartCache instance = new ShoppingCartCache();
	private Map<Long, ShopCache> shopCaches = new HashMap<Long, ShopCache>();

	private ShoppingCartCache() {
	}

	public static ShoppingCartCache getInstance() {
		return instance;
	}

	public void put(long shopId, long goodsId, int quantity) {
		ShopCache sellerCache = shopCaches.get(shopId);
		if (sellerCache == null) {
			sellerCache = new ShopCache(shopId);
			shopCaches.put(shopId, sellerCache);
		}
		sellerCache.put(goodsId, quantity);
	}

	public int increase(long shopId, long goodsId) {
		int current = getQuantity(shopId, goodsId);
		current++;
		if (current > 99) {
			current = 99;
		}
		put(shopId, goodsId, current);
		return current;
	}

	public int decrease(long shopId, long goodsId) {
		int quantity = getQuantity(shopId, goodsId);
		if (quantity > 0) {
			quantity--;
		}
		if (quantity <= 0) {
			remove(shopId, goodsId);
			return 0;
		} else {
			put(shopId, goodsId, quantity);
			return quantity;
		}
	}

	public void remove(long shopId, long goodsId) {
		ShopCache sellerCache = shopCaches.get(shopId);
		if (sellerCache != null) {
			sellerCache.remove(goodsId);
		}
	}

	public int getQuantity(long shopId, long goodsId) {
		ShopCache sellerCache = shopCaches.get(shopId);
		if (sellerCache == null) {
			return 0;
		}
		return sellerCache.getQuantity(goodsId);
	}

	private class ShopCache {
		Map<Long, Integer> goodsQuantities = new HashMap<Long, Integer>();

		ShopCache(long shopId) {
			super();
		}

		void put(long goodsId, int quantity) {
			if (quantity < 0) {
				quantity = 0;
			}
			goodsQuantities.put(goodsId, quantity);
		}

		void remove(long goodsId) {
			goodsQuantities.remove(goodsId);
		}

		int getQuantity(long goodsId) {
			if (goodsQuantities.containsKey(goodsId)) {
				return goodsQuantities.get(goodsId);
			}
			return 0;
		}
	}
}
