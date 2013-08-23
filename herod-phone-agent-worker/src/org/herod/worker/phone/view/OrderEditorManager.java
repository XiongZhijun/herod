/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.worker.phone.view;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.herod.framework.utils.StringUtils;
import org.herod.order.common.SerialNumberUtils;
import org.herod.order.common.model.Order;
import org.herod.order.common.model.OrderItem;

/**
 * 
 * 
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 * 
 */
public class OrderEditorManager {
	private static OrderEditorManager manager = new OrderEditorManager();
	private Lock lock = new ReentrantLock();
	private OrderEditor orderEditor;
	private boolean inEdit = false;

	public static OrderEditorManager getInstance() {
		return manager;
	}

	public boolean startEdit(Order order) {
		lock.lock();
		try {
			if (inEdit) {
				return false;
			}
			orderEditor = new OrderEditor(order);
			inEdit = true;
			return true;
		} finally {
			lock.unlock();
		}
	}

	public OrderEditor getOrderEditor() {
		lock.lock();
		try {
			if (inEdit) {
				return orderEditor;
			}
			return null;
		} finally {
			lock.unlock();
		}
	}

	public boolean isInEdit(String orderSN) {
		if (!inEdit || orderEditor == null) {
			return false;
		}
		Order order = orderEditor.getOrder();
		return order != null
				&& StringUtils.equals(order.getSerialNumber(), orderSN);

	}

	public int getOrderItemQuantity(OrderItem orderItem) {
		if (isInEdit(orderItem.getOrderSerialNumber())) {
			return orderEditor
					.getOrderItemQuantity(orderItem.getSerialNumber());
		}
		return orderItem.getQuantity();
	}

	public void stopEdit() {
		lock.lock();
		try {
			inEdit = false;
			orderEditor = null;
		} finally {
			lock.unlock();
		}
	}

	public int getQuantity(long shopId, long goodsId) {
		Order order = getOrder();
		if (!inEdit || order.getShopId() != shopId) {
			return 0;
		}
		String orderItemSerialNumber = SerialNumberUtils
				.buildOrderItemSerialNumber(order.getSerialNumber(), goodsId);
		return orderEditor.getOrderItemQuantity(orderItemSerialNumber);

	}

	public Collection<OrderItem> getNewOrderItems() {
		if (inEdit) {
			return orderEditor.getNewOrderItems();
		}
		return Collections.emptyList();
	}

	private Order getOrder() {
		return orderEditor.getOrder();
	}
}
