/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.buyer.phone;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
	private List<GoodsQuantityChangedListener> listeners = new ArrayList<ShoppingCartCache.GoodsQuantityChangedListener>();
	private ShopService shopService = BuyerContext.getShopService();

	private ShoppingCartCache() {
	}

	public static ShoppingCartCache getInstance() {
		return instance;
	}

	public List<Order> getAllOrders() {
		return new ArrayList<Order>(orderCaches.values());
	}

	public int getTotalQuantity() {
		int total = 0;
		for (Order order : orderCaches.values()) {
			total += order.getTotalQuantity();
		}
		return total;
	}

	private Order findOrCreateOrder(long shopId) {
		Order order = orderCaches.get(shopId);
		if (order == null) {
			order = new Order();
			Map<String, Object> shop = shopService.findShopById(shopId);
			if (shop != null) {
				double costOfRunErrands = (Double) shop
						.get(Constants.COST_OF_RUN_ERRANDS);
				order.setCostOfRunErrands(costOfRunErrands);
			}
			order.setShopId(shopId);
			orderCaches.put(shopId, order);
		}
		return order;
	}

	private OrderItem findAndCreateOrderItem(long shopId, Map<String, ?> goods) {
		Order order = findOrCreateOrder(shopId);
		long goodsId = (Long) goods.get("id");
		OrderItem orderItem = order.findOrderItemByGoodsId(goodsId);
		if (orderItem == null) {
			orderItem = createOrderItem(goods);
			order.addOrderItem(orderItem);
		}
		return orderItem;
	}

	private OrderItem createOrderItem(Map<String, ?> goods) {
		OrderItem orderItem;
		orderItem = new OrderItem();
		long goodsId = (Long) goods.get("id");
		String goodsCode = (String) goods.get("code");
		String goodsName = (String) goods.get("name");
		double unitPrice = (Double) goods.get("price");
		orderItem.setUnitPrice(unitPrice);
		orderItem.setGoodsCode(goodsCode);
		orderItem.setGoodsName(goodsName);
		orderItem.setGoodsId(goodsId);
		return orderItem;
	}

	public int increaseWithExistGoods(long shopId, long goodsId) {
		OrderItem orderItem = findOrderItem(shopId, goodsId);
		if (orderItem == null) {
			return 0;
		}
		int quantity = orderItem.getQuantity();
		quantity++;
		orderItem.setQuantity(quantity);
		notifyQuantityChanged(shopId, goodsId, quantity);
		return quantity;
	}

	public int increase(long shopId, Map<String, ?> goods) {
		OrderItem orderItem = findAndCreateOrderItem(shopId, goods);
		int current = orderItem.getQuantity();
		current++;
		if (current > 99) {
			current = 99;
		}
		orderItem.setQuantity(current);
		notifyQuantityChanged(shopId, orderItem.getGoodsId(), current);
		return current;
	}

	public int decrease(long shopId, long goodsId) {
		OrderItem orderItem = findOrderItem(shopId, goodsId);
		if (orderItem == null) {
			return 0;
		}
		int current = orderItem.getQuantity();
		current--;
		if (current > 0) {
			orderItem.setQuantity(current);
		} else {
			removeOrderItem(shopId, goodsId);
			current = 0;
		}
		notifyQuantityChanged(shopId, goodsId, current);
		return current;

	}

	private OrderItem findOrderItem(long shopId, long goodsId) {
		Order order = orderCaches.get(shopId);
		if (order == null) {
			return null;
		}
		return order.findOrderItemByGoodsId(goodsId);
	}

	public void removeOrderItem(long shopId, long goodsId) {
		Order order = orderCaches.get(shopId);
		if (order == null) {
			return;
		}
		order.removeOrderItemByGoodsId(goodsId);
		if (order.getOrderItems().size() == 0) {
			orderCaches.remove(shopId);
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

	private void notifyQuantityChanged(long shopId, long goodsId,
			int newQuantity) {
		for (GoodsQuantityChangedListener listener : listeners) {
			listener.onGoodsQuantityChanged(shopId, goodsId, newQuantity);
		}
	}

	public void registGoodsQuantityChangedListener(
			GoodsQuantityChangedListener listener) {
		this.listeners.add(listener);
	}

	public void unReistGoodsQuantityChangedListener(
			GoodsQuantityChangedListener listener) {
		this.listeners.remove(listener);
	}

	public static interface GoodsQuantityChangedListener {
		void onGoodsQuantityChanged(long shopId, long goodsId, int newQuantity);
	}

}
