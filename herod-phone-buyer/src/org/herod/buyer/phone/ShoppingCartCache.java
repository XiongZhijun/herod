/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.buyer.phone;

import java.util.HashMap;
import java.util.Map;

import org.herod.buyer.phone.model.Order;
import org.herod.buyer.phone.model.OrderItem;

/**
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 * 
 */
public class ShoppingCartCache {
	private static ShoppingCartCache instance = new ShoppingCartCache();
	private Map<Long, Order> orderCaches = new HashMap<Long, Order>();

	private ShoppingCartCache() {
	}

	public static ShoppingCartCache getInstance() {
		return instance;
	}

	public int getTotalQuantity() {
		int total = 0;
		for (Order order : orderCaches.values()) {
			total += order.getTotalQuantity();
		}
		return total;
	}

	public void put(long shopId, long goodsId, String goodsCode, int quantity) {
		Order order = orderCaches.get(shopId);
		if (order == null) {
			order = new Order();
			order.setShopId(shopId);
			orderCaches.put(shopId, order);
		}
		OrderItem orderItem = order.findOrderItemByGoodsId(goodsId);
		if (orderItem == null) {
			order.addOrderItem(createorderItem(goodsId, goodsCode, quantity));
		} else {
			orderItem.setQuantity(quantity);
		}
	}

	private OrderItem createorderItem(long goodsId, String goodsCode,
			int quantity) {
		OrderItem orderItem = new OrderItem();
		orderItem.setGoodsId(goodsId);
		orderItem.setGoodsCode(goodsCode);
		orderItem.setQuantity(quantity);
		return orderItem;
	}

	public int increase(long shopId, long goodsId, String goodsCode) {
		int current = getQuantity(shopId, goodsId);
		current++;
		if (current > 99) {
			current = 99;
		}
		put(shopId, goodsId, goodsCode, current);
		return current;
	}

	public int decrease(long shopId, long goodsId, String goodsCode) {
		int quantity = getQuantity(shopId, goodsId);
		if (quantity > 0) {
			quantity--;
		}
		if (quantity <= 0) {
			remove(shopId, goodsId);
			return 0;
		} else {
			put(shopId, goodsId, goodsCode, quantity);
			return quantity;
		}
	}

	public void remove(long shopId, long goodsId) {
		Order order = orderCaches.get(shopId);
		if (order != null) {
			order.removeOrderItemByGoodsId(goodsId);
		}
	}

	public int getQuantity(long shopId, long goodsId) {
		Order order = orderCaches.get(shopId);
		if (order == null) {
			return 0;
		}
		OrderItem orderItem = order.findOrderItemByGoodsId(goodsId);
		if (orderItem != null) {
			return orderItem.getQuantity();
		}
		return 0;
	}

}
