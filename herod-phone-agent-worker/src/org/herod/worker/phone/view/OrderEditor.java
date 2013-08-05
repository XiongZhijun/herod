/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.worker.phone.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.herod.worker.phone.model.Order;
import org.herod.worker.phone.model.OrderItem;
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

	public void submitUpdateInfo() {

	}

	public void setOrder(Order order) {
		this.order = order;
	}
}
