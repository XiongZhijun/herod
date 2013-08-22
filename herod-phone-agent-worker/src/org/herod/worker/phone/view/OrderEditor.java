/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.worker.phone.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.herod.framework.utils.StringUtils;
import org.herod.order.common.model.Order;
import org.herod.order.common.model.OrderItem;
import org.herod.worker.phone.model.OrderUpdateInfo;

/**
 * 
 * 
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 * 
 */
public class OrderEditor {
	private Order order;
	private Map<String, Integer> newOrderItemQuantityMap = new HashMap<String, Integer>();
	private Map<Long, String> goodsIdSerialNumberMap = new HashMap<Long, String>();
	private List<OrderItem> newOrderItems = new ArrayList<OrderItem>();

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

	public void addNewOrderItems(Map<Long, OrderItem> newOrderItemMap) {
		for (Entry<Long, OrderItem> entry : newOrderItemMap.entrySet()) {
			String serialNumber = goodsIdSerialNumberMap.get(entry.getKey());
			if (StringUtils.isBlank(serialNumber)) {
				newOrderItems.add(entry.getValue());
			} else {
				Integer quantity = newOrderItemQuantityMap.get(serialNumber);
				quantity += entry.getValue().getQuantity();
				newOrderItemQuantityMap.put(serialNumber, quantity);
			}
		}
	}

	public int increaseItem(String serialNumber) {
		if (newOrderItemQuantityMap.containsKey(serialNumber)) {
			int quantity = newOrderItemQuantityMap.get(serialNumber);
			quantity++;
			newOrderItemQuantityMap.put(serialNumber, quantity);
			return quantity;
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
		return 0;
	}

	public OrderUpdateInfo toUpdateInfo(String newComment, String reason) {
		OrderUpdateInfo updateInfo = new OrderUpdateInfo(
				order.getSerialNumber());
		updateInfo.setNewOrderItems(newOrderItems);
		List<String> deletedOrderItems = updateInfo.getDeletedOrderItems();
		Map<String, Integer> quantityChangeMap = updateInfo
				.getQuantityChangeMap();
		for (OrderItem orderItem : order.getOrderItems()) {
			String serialNumber = orderItem.getSerialNumber();
			if (newOrderItemQuantityMap.keySet().contains(serialNumber)) {
				int newQuantity = newOrderItemQuantityMap.get(serialNumber);
				if (newQuantity <= 0) {
					deletedOrderItems.add(serialNumber);
				} else if (newQuantity != orderItem.getQuantity()) {
					quantityChangeMap.put(serialNumber,
							newOrderItemQuantityMap.get(serialNumber));
				}
			} else {
				newOrderItems.add(orderItem);
			}
		}
		updateInfo.setNewComment(newComment);
		updateInfo.setReason(reason);
		return updateInfo;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}
}
