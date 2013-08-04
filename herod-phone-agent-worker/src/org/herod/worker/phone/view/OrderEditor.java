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
	private Map<String, Integer> oldOrderItemQuantityMap = new HashMap<String, Integer>();
	private List<OrderItem> newOrderItems = new ArrayList<OrderItem>();

	public OrderEditor(Order order) {
		super();
		this.order = order;
		for (OrderItem orderItem : order.getOrderItems()) {
			oldOrderItemQuantityMap.put(orderItem.getSerialNumber(),
					orderItem.getQuantity());
		}
	}

	public int increaseItem(String serialNumber) {
		OrderItem orderItem = order.findOrderItemBySerialNumber(serialNumber);
		if (orderItem != null) {
			orderItem.increaseQuantity();
			return orderItem.getQuantity();
		}
		return 0;
	}

	public int decreaseItem(String serialNumber) {
		OrderItem orderItem = order.findOrderItemBySerialNumber(serialNumber);
		if (orderItem != null) {
			orderItem.decreaseQuantity();
			return orderItem.getQuantity();
		}
		return 0;
	}

	public Order restore() {
		for (OrderItem orderItem : order.getOrderItems()) {
			orderItem.setQuantity(oldOrderItemQuantityMap.get(orderItem
					.getSerialNumber()));
		}
		newOrderItems.clear();
		return order;
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
			if (oldOrderItemQuantityMap.keySet().contains(serialNumber)) {
				int oldQuantity = oldOrderItemQuantityMap.get(serialNumber);
				if (orderItem.getQuantity() <= 0) {
					deletedOrderItems.add(serialNumber);
				} else if (oldQuantity != orderItem.getQuantity()) {
					quantityChangeMap
							.put(serialNumber, orderItem.getQuantity());
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
}
