/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved.
 */
package org.herod.order.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.herod.order.model.Address;
import org.herod.order.model.Location;
import org.herod.order.model.Order;
import org.herod.order.model.OrderItem;
import org.herod.order.model.OrderStatus;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * 
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 */
public class OrderList {

	private List<Order> orders;
	private List<OrderItem> orderItems;

	private OrderList() {
		super();
	}

	private OrderList(List<Order> orders, List<OrderItem> orderItems) {
		super();
		this.orders = orders;
		this.orderItems = orderItems;
	}

	public List<Order> getOrders() {
		return orders;
	}

	public List<OrderItem> getOrderItems() {
		return orderItems;
	}

	public Set<String> getSerialNumbers() {
		Set<String> serialNumbers = new HashSet<String>();
		for (Order order : orders) {
			serialNumbers.add(order.getSerialNumber());
		}
		return serialNumbers;
	}

	public boolean isEmpty() {
		return CollectionUtils.isEmpty(orders);
	}

	public static OrderList convertToOrderList(String ordersJson,
			double longitude, double latitude) {
		List<Order> orders = new Gson().fromJson(ordersJson,
				new TypeToken<List<Order>>() {
				}.getType());
		if (CollectionUtils.isEmpty(orders)) {
			return new OrderList();
		}
		List<OrderItem> orderItems = new ArrayList<OrderItem>();
		for (Order order : orders) {
			Address deliveryAddress = order.getDeliveryAddress();
			if (deliveryAddress == null) {
				deliveryAddress = new Address();
				order.setDeliveryAddress(deliveryAddress);
			}
			deliveryAddress.setLocation(new Location(longitude, latitude));
			order.setSubmitTime(new Date());
			order.initOrderItemProperties();
			order.setStatus(OrderStatus.Submitted);
			orderItems.addAll(order.getOrderItems());
		}
		return new OrderList(orders, orderItems);
	}
}
