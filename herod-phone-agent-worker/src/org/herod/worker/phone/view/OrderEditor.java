/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.worker.phone.view;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.herod.framework.MapWrapper;
import org.herod.framework.utils.StringUtils;
import org.herod.order.common.AbstractGoodsListFragment.IShoppingCartCache;
import org.herod.order.common.SerialNumberUtils;
import org.herod.order.common.model.Order;
import org.herod.order.common.model.OrderItem;
import org.herod.order.common.model.OrderItemFlag;
import org.herod.worker.phone.model.OrderUpdateInfo;

/**
 * 
 * 
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 * 
 */
public class OrderEditor implements IShoppingCartCache {
	private Order order;
	private Map<Long, String> goodsIdSerialNumberMap = new HashMap<Long, String>();
	private Map<String, Integer> newOrderItemQuantityMap = new HashMap<String, Integer>();
	private Map<Long, OrderItem> newOrderItems = new HashMap<Long, OrderItem>();

	public OrderEditor(Order order) {
		super();
		this.order = order;
		init(order);
	}

	private void init(Order order) {
		for (OrderItem orderItem : order.getOrderItems()) {
			newOrderItemQuantityMap.put(orderItem.getSerialNumber(),
					orderItem.getQuantity());
			goodsIdSerialNumberMap.put(orderItem.getGoodsId(),
					orderItem.getSerialNumber());
		}
	}

	public int increaseItem(String serialNumber) {
		if (newOrderItemQuantityMap.containsKey(serialNumber)) {
			int quantity = newOrderItemQuantityMap.get(serialNumber);
			quantity++;
			newOrderItemQuantityMap.put(serialNumber, quantity);
			return quantity;
		}

		for (OrderItem orderItem : newOrderItems.values()) {
			if (StringUtils.equals(orderItem.getSerialNumber(), serialNumber)) {
				int quantity = orderItem.getQuantity();
				quantity = quantity >= 0 ? quantity + 1 : 1;
				orderItem.setQuantity(quantity);
				return quantity;
			}
		}
		return 0;
	}

	public int decreaseItem(String serialNumber) {
		if (newOrderItemQuantityMap.containsKey(serialNumber)) {
			int quantity = newOrderItemQuantityMap.get(serialNumber);
			quantity = quantity > 0 ? quantity - 1 : 0;
			newOrderItemQuantityMap.put(serialNumber, quantity);
			return quantity;
		}
		for (OrderItem orderItem : newOrderItems.values()) {
			if (StringUtils.equals(orderItem.getSerialNumber(), serialNumber)) {
				int quantity = orderItem.getQuantity();
				quantity = quantity > 0 ? quantity - 1 : 0;
				orderItem.setQuantity(quantity);
				return quantity;
			}
		}
		return 0;
	}

	@Override
	public int increase(long shopId, MapWrapper<String, ?> goods) {
		long goodsId = goods.getLong("id");
		String orderItemSN = SerialNumberUtils.buildOrderItemSerialNumber(
				order.getSerialNumber(), goodsId);
		if (newOrderItemQuantityMap.containsKey(orderItemSN)) {
			return increaseItem(orderItemSN);
		}
		OrderItem orderItem = newOrderItems.get(goodsId);
		if (orderItem == null) {
			orderItem = createOrderItem(goods, orderItemSN, goodsId);
			newOrderItems.put(goodsId, orderItem);
		}
		int current = orderItem.getQuantity();
		current++;
		if (current > 99) {
			current = 99;
		}
		orderItem.setQuantity(current);
		return current;
	}

	private OrderItem createOrderItem(MapWrapper<String, ?> goods,
			String orderItemSN, long goodsId) {
		OrderItem orderItem = new OrderItem();
		orderItem.setOrderSerialNumber(order.getSerialNumber());
		orderItem.setSerialNumber(orderItemSN);
		orderItem.setGoodsId(goodsId);
		orderItem.setSellingPrice(goods.getDouble("sellingPrice"));
		orderItem.setGoodsCode(goods.getString("code"));
		orderItem.setGoodsName(goods.getString("name"));
		orderItem.setQuantity(0);
		orderItem.setAgentId(order.getAgentId());
		orderItem.setShopId(order.getShopId());
		orderItem.setShopName(order.getShopName());
		orderItem.setFlag(OrderItemFlag.Added);
		return orderItem;
	}

	@Override
	public int decrease(long shopId, long goodsId) {
		String orderItemSN = SerialNumberUtils.buildOrderItemSerialNumber(
				order.getSerialNumber(), goodsId);
		if (newOrderItemQuantityMap.containsKey(orderItemSN)) {
			return decreaseItem(orderItemSN);
		}
		OrderItem orderItem = newOrderItems.get(goodsId);
		if (orderItem == null) {
			return 0;
		}
		int current = orderItem.getQuantity();
		current--;
		if (current > 0) {
			orderItem.setQuantity(current);
		} else {
			newOrderItems.remove(goodsId);
			current = 0;
		}
		return current;
	}

	@Override
	public int getQuantity(long shopId, long goodsId) {
		return OrderEditorManager.getInstance().getQuantity(shopId, goodsId);
	}

	public OrderUpdateInfo toUpdateInfo(String newComment, String reason) {
		OrderUpdateInfo updateInfo = new OrderUpdateInfo(
				order.getSerialNumber());
		updateInfo.setNewOrderItems(newOrderItems.values());
		List<String> deletedOrderItems = updateInfo.getDeletedOrderItems();
		Map<String, Integer> quantityChangeMap = updateInfo
				.getQuantityChangeMap();
		for (OrderItem orderItem : order.getOrderItems()) {
			String serialNumber = orderItem.getSerialNumber();
			int newQuantity = newOrderItemQuantityMap.get(serialNumber);
			if (newQuantity <= 0) {
				deletedOrderItems.add(serialNumber);
			} else if (newQuantity != orderItem.getQuantity()) {
				quantityChangeMap.put(serialNumber,
						newOrderItemQuantityMap.get(serialNumber));
			}
		}
		updateInfo.setNewComment(newComment);
		updateInfo.setReason(reason);
		return updateInfo;
	}

	public int getOrderItemQuantity(String orderItemSN) {
		if (newOrderItemQuantityMap.containsKey(orderItemSN)) {
			return newOrderItemQuantityMap.get(orderItemSN);
		}
		for (OrderItem orderItem : newOrderItems.values()) {
			if (StringUtils.equals(orderItemSN, orderItem.getSerialNumber())) {
				return orderItem.getQuantity();
			}
		}
		return 0;
	}

	public Collection<OrderItem> getNewOrderItems() {
		return newOrderItems.values();
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}
}
