/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.worker.phone.view;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.herod.framework.utils.StringUtils;
import org.herod.order.common.model.Order;

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

	public void stopEdit() {
		lock.lock();
		try {
			inEdit = false;
			orderEditor = null;
		} finally {
			lock.unlock();
		}
	}
}
