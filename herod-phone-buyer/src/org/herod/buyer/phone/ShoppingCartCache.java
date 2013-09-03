/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.buyer.phone;

import static org.herod.order.common.Constants.ID;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.herod.framework.MapWrapper;
import org.herod.order.common.AbstractGoodsListFragment.IShoppingCartCache;
import org.herod.order.common.model.Order;
import org.herod.order.common.model.OrderItem;

/**
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 * 
 */
public class ShoppingCartCache implements IShoppingCartCache {

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

	public double getTotalAmount() {
		double total = 0;
		for (Order order : orderCaches.values()) {
			total += order.getTotalAmountWithCostOfRunErrands();
		}
		return total;
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

	public int increase(long shopId, MapWrapper<String, ?> goods) {
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

	private OrderItem findAndCreateOrderItem(long shopId,
			MapWrapper<String, ?> goods) {
		Order order = findOrCreateOrder(shopId);
		long goodsId = goods.getLong(ID);
		OrderItem orderItem = order.findOrderItemByGoodsId(goodsId);
		if (orderItem == null) {
			orderItem = OrderUtils.createOrderItem(goods);
			order.addOrderItem(orderItem);
		}
		return orderItem;
	}

	private Order findOrCreateOrder(long shopId) {
		Order order = orderCaches.get(shopId);
		if (order == null) {
			MapWrapper<String, Object> shop = shopService.findShopById(shopId);
			order = OrderUtils.createOrder(shop);
			orderCaches.put(shopId, order);
		}
		return order;
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

	public void removeOrder(long shopId) {
		orderCaches.remove(shopId);
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

	public void clearOrders() {
		orderCaches.clear();
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
