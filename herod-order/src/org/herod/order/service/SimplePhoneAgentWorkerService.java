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
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 * 
 */
@Transactional
public class SimplePhoneAgentWorkerService implements PhoneAgentWorkerService {
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
	public List<Order> findWaitAcceptOrders(String token, String imei) {
		long workerId = loginService.getWorkerId(token, imei);
		long workerAgentId = loginService.getWorkerAgentId(token, imei);
		return orderQueryService.findWaitAcceptOrders(workerId, workerAgentId);
	}

	@Override
	public List<Order> findWaitCompleteOrders(String token, String imei) {
		long workerId = loginService.getWorkerId(token, imei);
		return orderQueryService.findWaitCompleteOrders(workerId);
	}

	@Override
	public List<Order> findCompletedOrders(String token, String imei,
			int begin, int count) {
		long workerId = loginService.getWorkerId(token, imei);
		return orderQueryService.findHistoryOrdersByWorkerAndStatus(workerId,
				OrderStatus.Completed, begin, count);
	}

	@Override
	public List<Order> findCanceledOrders(String token, String imei, int begin,
			int count) {
		long workerId = loginService.getWorkerId(token, imei);
		return orderQueryService.findHistoryOrdersByWorkerAndStatus(workerId,
				OrderStatus.Cancelled, begin, count);
	}

	@Override
	public Result acceptOrder(String serialNumber, String token, String imei) {
		if (orderStatusChecker.canNotChangeStatus(serialNumber,
				OrderStatus.Acceptted)) {
			return new SimpleResult(
					ResultCode.CurrentStatusCanNotDoSuchOperate, serialNumber);
		}
		long workerId = loginService.getWorkerId(token, imei);
		long agentId = loginService.getWorkerAgentId(token, imei);
		orderStatusUpdateService.updateOrderStatusAndWorker(serialNumber,
				workerId, OrderStatus.Acceptted);
		orderLogService.agentWorkerlog(agentId, workerId, serialNumber,
				Operation.Accept, null);
		return Result.SUCCESS;
	}

	@Override
	public Result updateOrder(OrderUpdateInfo updateInfo, String token,
			String imei) {
		String serialNumber = updateInfo.getOrderSerialNumber();
		if (orderStatusChecker.canNotUpdate(serialNumber)) {
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

		long workerId = loginService.getWorkerId(token, imei);
		long agentId = loginService.getWorkerAgentId(token, imei);
		orderLogService.agentWorkerlog(agentId, workerId, serialNumber,
				Operation.Update, null);
		return Result.SUCCESS;
	}

	@Override
	public Result rejectOrder(String serialNumber, String reason, String token,
			String imei) {
		if (orderStatusChecker.canNotChangeStatus(serialNumber,
				OrderStatus.Rejected)) {
			return new SimpleResult(
					ResultCode.CurrentStatusCanNotDoSuchOperate, serialNumber);
		}
		orderStatusUpdateService.updateOrderStatus(serialNumber,
				OrderStatus.Rejected);
		long workerId = loginService.getWorkerId(token, imei);
		long agentId = loginService.getWorkerAgentId(token, imei);
		orderLogService.agentWorkerlog(agentId, workerId, serialNumber,
				Operation.Reject, reason);
		return Result.SUCCESS;
	}

	@Override
	public Result cancelOrder(String serialNumber, String reason, String token,
			String imei) {
		if (orderStatusChecker.canNotChangeStatus(serialNumber,
				OrderStatus.Cancelled)) {
			return new SimpleResult(
					ResultCode.CurrentStatusCanNotDoSuchOperate, serialNumber);
		}
		orderStatusUpdateService.updateOrderStatusAndCompleteTime(serialNumber,
				OrderStatus.Cancelled, new Date());
		long workerId = loginService.getWorkerId(token, imei);
		long agentId = loginService.getWorkerAgentId(token, imei);
		orderLogService.agentWorkerlog(agentId, workerId, serialNumber,
				Operation.Cancel, reason);
		return Result.SUCCESS;
	}

	@Override
	public Result completeOrder(String serialNumber, String token, String imei) {
		if (orderStatusChecker.canNotChangeStatus(serialNumber,
				OrderStatus.Completed)) {
			return new SimpleResult(
					ResultCode.CurrentStatusCanNotDoSuchOperate, serialNumber);
		}
		orderStatusUpdateService.updateOrderStatusAndCompleteTime(serialNumber,
				OrderStatus.Completed, new Date());
		long workerId = loginService.getWorkerId(token, imei);
		long agentId = loginService.getWorkerAgentId(token, imei);
		orderLogService.agentWorkerlog(agentId, workerId, serialNumber,
				Operation.Complete, null);
		return Result.SUCCESS;
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
		List<Order> findWaitAcceptOrders(long workerId, long workerAgentId);

		List<Order> findWaitCompleteOrders(long workerId);

		List<Order> findHistoryOrdersByWorkerAndStatus(long workerId,
				OrderStatus status, int begin, int count);

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