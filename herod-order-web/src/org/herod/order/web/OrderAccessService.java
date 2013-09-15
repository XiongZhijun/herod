/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.order.web;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.herod.order.model.Order;
import org.herod.order.service.SimplePhoneBuyerService.OrderDas;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 
 * 
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 * 
 */
public class OrderAccessService {
	@Autowired
	private OrderDas orderDas;

	public Order findOrderBySerialNumber(String serialNumber) {
		List<Order> orders = orderDas.findOrdersBySerialNumbers(Arrays
				.asList(serialNumber));
		return CollectionUtils.isEmpty(orders) ? null : orders.get(0);
	}

	public void setOrderDas(OrderDas orderDas) {
		this.orderDas = orderDas;
	}
}
