/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.worker.phone.view;

import java.util.HashMap;
import java.util.Map;

import org.herod.worker.phone.model.Order;

/**
 * 
 * 
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 * 
 */
public class OrderEditorManager {
	private static OrderEditorManager manager = new OrderEditorManager();
	private Map<String, OrderEditor> editorMap = new HashMap<String, OrderEditor>();

	public static OrderEditorManager getInstance() {
		return manager;
	}

	public void addOrderEditor(Order order) {
		OrderEditor editor = editorMap.get(order.getSerialNumber());
		if (editor == null) {
			editorMap.put(order.getSerialNumber(), new OrderEditor(order));
		} else {
			editor.setOrder(order);
		}
	}

	public OrderEditor findOrderEditor(String serialNumber) {
		return editorMap.get(serialNumber);
	}

	public OrderEditor removeOrderEditor(String serialNumber) {
		return editorMap.remove(serialNumber);
	}
}
