/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.order.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.herod.order.model.Order;
import org.herod.order.model.OrderItem;
import org.herod.order.model.OrderLog.Operation;
import org.herod.order.model.OrderStatus;
import org.herod.order.model.OrderUpdateInfo;
import org.herod.order.model.Token;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 
 * 
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 * 
 */
public class SimplePhoneAgentWorkerService implements PhoneAgentWorkerService {
	@Autowired
	private AgentWorkerIdentityService identityService;
	@Autowired
	private OrderStatusChecker orderStatusChecker;
	@Autowired
	private OrderQueryService orderQueryService;
	@Autowired
	private OrderLogService orderLogService;
	@Autowired
	private OrderStatusUpdateService orderStatusUpdateService;
	@Autowired
	private OrderUpdateService orderUpdateService;
	@Autowired
	private OrderItemUpdateService orderItemUpdateService;
	@Autowired
	private LoginService loginService;

	@Override
	public Token login(String name, String password, String imei) {
		return loginService.doLogin(name, password, imei);
	}

	@Override
	public List<Order> findWaitAcceptOrders() {
		long agentId = identityService.getCurrentWorkerAgentId();
		return orderQueryService.findWaitAcceptOrders(agentId);
	}

	@Override
	public List<Order> findWaitCompleteOrders() {
		long workerId = identityService.getCurrentWorkerId();
		return orderQueryService.findOrdersByWorkerAndStatus(workerId,
				OrderStatus.Acceptted);
	}

	@Override
	public List<Order> findCompletedOrders() {
		long workerId = identityService.getCurrentWorkerId();
		return orderQueryService.findOrdersByWorkerAndStatusWithOneDay(
				workerId, OrderStatus.Completed);
	}

	@Override
	public List<Order> findCanceledOrders() {
		long workerId = identityService.getCurrentWorkerId();
		return orderQueryService.findOrdersByWorkerAndStatusWithOneDay(
				workerId, OrderStatus.Cancelled);
	}

	@Override
	public Result acceptOrder(String serialNumber) {
		if (orderStatusChecker.canChangeStatus(serialNumber,
				OrderStatus.Acceptted)) {
			return new SimpleResult(
					ResultCode.CurrentStatusCanNotDoSuchOperate, serialNumber);
		}
		long currentWorkerId = identityService.getCurrentWorkerId();
		orderStatusUpdateService.updateOrderStatusAndWorker(serialNumber,
				currentWorkerId, OrderStatus.Acceptted);
		orderLogService.agentWorkerlog(serialNumber, Operation.Accept, null);
		return Result.SUCCESS;
	}

	@Override
	public Result updateOrder(OrderUpdateInfo updateInfo) {
		String serialNumber = updateInfo.getOrderSerialNumber();
		if (orderStatusChecker.canUpdate(serialNumber)) {
			return new SimpleResult(
					ResultCode.CurrentStatusCanNotDoSuchOperate, serialNumber);
		}
		List<String> deletedOrderItems = updateInfo.getDeletedOrderItems();
		String newComment = updateInfo.getNewComment();
		List<OrderItem> newOrderItems = updateInfo.getNewOrderItems();
		Map<String, Integer> quantityChangeMap = updateInfo
				.getQuantityChangeMap();

		orderUpdateService.updateOrderComment(serialNumber, newComment);
		orderItemUpdateService.deleteOrderItem(deletedOrderItems);
		orderItemUpdateService.addOrderItems(newOrderItems);
		orderItemUpdateService.changeOrderItemQuantity(quantityChangeMap);

		orderLogService.agentWorkerlog(serialNumber, Operation.Update, null);
		return Result.SUCCESS;
	}

	@Override
	public Result rejectOrder(String serialNumber, String reason) {
		if (orderStatusChecker.canChangeStatus(serialNumber,
				OrderStatus.Rejected)) {
			return new SimpleResult(
					ResultCode.CurrentStatusCanNotDoSuchOperate, serialNumber);
		}
		orderStatusUpdateService.updateOrderStatus(serialNumber,
				OrderStatus.Rejected);
		orderLogService.agentWorkerlog(serialNumber, Operation.Reject, reason);
		return Result.SUCCESS;
	}

	@Override
	public Result cancelOrder(String serialNumber, String reason) {
		if (orderStatusChecker.canChangeStatus(serialNumber,
				OrderStatus.Cancelled)) {
			return new SimpleResult(
					ResultCode.CurrentStatusCanNotDoSuchOperate, serialNumber);
		}
		orderStatusUpdateService.updateOrderStatus(serialNumber,
				OrderStatus.Cancelled);
		orderLogService.agentWorkerlog(serialNumber, Operation.Cancel, reason);
		return Result.SUCCESS;
	}

	@Override
	public Result completeOrder(String serialNumber) {
		if (orderStatusChecker.canChangeStatus(serialNumber,
				OrderStatus.Completed)) {
			return new SimpleResult(
					ResultCode.CurrentStatusCanNotDoSuchOperate, serialNumber);
		}
		orderStatusUpdateService.updateOrderStatusAndCompleteTime(serialNumber,
				OrderStatus.Completed, new Date());
		orderLogService.agentWorkerlog(serialNumber, Operation.Complete, null);
		return Result.SUCCESS;
	}

	public void setIdentityService(AgentWorkerIdentityService identityService) {
		this.identityService = identityService;
	}

	public void setOrderStatusChecker(OrderStatusChecker orderStatusChecker) {
		this.orderStatusChecker = orderStatusChecker;
	}

	public void setOrderQueryService(OrderQueryService orderQueryService) {
		this.orderQueryService = orderQueryService;
	}

	public void setOrderLogService(OrderLogService orderLogService) {
		this.orderLogService = orderLogService;
	}

	public void setOrderStatusUpdateService(
			OrderStatusUpdateService orderStatusUpdateService) {
		this.orderStatusUpdateService = orderStatusUpdateService;
	}

	public void setOrderUpdateService(OrderUpdateService orderUpdateService) {
		this.orderUpdateService = orderUpdateService;
	}

	public void setOrderItemUpdateService(
			OrderItemUpdateService orderItemUpdateService) {
		this.orderItemUpdateService = orderItemUpdateService;
	}

	public void setLoginService(LoginService loginService) {
		this.loginService = loginService;
	}

	public static interface OrderQueryService {
		List<Order> findWaitAcceptOrders(long agentId);

		List<Order> findOrdersByWorkerAndStatus(long workerId,
				OrderStatus status);

		List<Order> findOrdersByWorkerAndStatusWithOneDay(long workerId,
				OrderStatus status);

	}

	public static interface OrderUpdateService {
		void updateOrderComment(String serialNumber, String newComment);
	}

	public static interface OrderItemUpdateService {
		void deleteOrderItem(List<String> orderItemSerialNumbers);

		void addOrderItems(List<OrderItem> orderItems);

		void changeOrderItemQuantity(
				Map<String, Integer> orderItemQuantityChangeMap);
	}

	public static interface OrderStatusUpdateService {
		void updateOrderStatus(String serialNumber, OrderStatus status);

		void updateOrderStatusAndWorker(String serialNumber, long agentWorker,
				OrderStatus status);

		void updateOrderStatusAndCompleteTime(String serialNumber,
				OrderStatus status, Date completeTime);
	}

}
