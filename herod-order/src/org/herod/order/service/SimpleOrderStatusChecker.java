/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.order.service;

import static org.herod.order.model.OrderStatus.Acceptted;
import static org.herod.order.model.OrderStatus.Cancelled;
import static org.herod.order.model.OrderStatus.Completed;
import static org.herod.order.model.OrderStatus.Rejected;
import static org.herod.order.model.OrderStatus.Submitted;
import static org.herod.order.model.OrderStatus.Unsubmit;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.herod.order.model.OrderStatus;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 
 * 
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 * 
 */
public class SimpleOrderStatusChecker implements OrderStatusChecker {
	@Autowired
	private OrderStatusFinder orderStatusFinder;

	@Override
	public boolean canNotChangeStatus(String serialNumber,
			OrderStatus destStatus) {
		OrderStatus orderStatus = orderStatusFinder
				.findOrderStatus(serialNumber);
		List<OrderStatus> statuses = findStatusbyCurrentStatus(destStatus);
		return !statuses.contains(orderStatus);
	}

	@Override
	public boolean canNotUpdate(String serialNumber) {
		OrderStatus orderStatus = orderStatusFinder
				.findOrderStatus(serialNumber);
		return !CAN_UPDATE_STATUS.contains(orderStatus);
	}

	@SuppressWarnings("unchecked")
	protected List<OrderStatus> findStatusbyCurrentStatus(OrderStatus destStatus) {
		for (Object[] entry : ORDER_STATUS_MACHINE) {
			if (entry[0] == destStatus) {
				return (List<OrderStatus>) entry[1];
			}
		}
		return Collections.emptyList();
	}

	public void setOrderStatusFinder(OrderStatusFinder orderStatusFinder) {
		this.orderStatusFinder = orderStatusFinder;
	}

	public static interface OrderStatusFinder {
		OrderStatus findOrderStatus(String serialNumber);
	}

	private static final List<OrderStatus> CAN_UPDATE_STATUS = Arrays.asList(
			Unsubmit, Submitted, Acceptted, Rejected);

	private static final Object[][] ORDER_STATUS_MACHINE = new Object[][] {
			{ Unsubmit, Arrays.asList() },
			{ Submitted, Arrays.asList(Unsubmit) },
			{ Acceptted, Arrays.asList(Submitted) },
			{ Completed, Arrays.asList(Acceptted) },
			{ Cancelled, Arrays.asList(Unsubmit, Submitted, Acceptted) },
			{ Rejected, Arrays.asList(Acceptted) } };

}
