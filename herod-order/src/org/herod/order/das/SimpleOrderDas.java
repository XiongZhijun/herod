/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.order.das;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.herod.common.das.HerodBeanPropertyRowMapper;
import org.herod.common.das.HerodBeanPropertySqlParameterSource;
import org.herod.common.das.HerodJdbcTemplate;
import org.herod.common.das.HerodSingleColumnRowMapper;
import org.herod.order.model.Order;
import org.herod.order.model.OrderItem;
import org.herod.order.model.OrderStatus;
import org.herod.order.service.SimpleOrderStatusChecker.OrderStatusFinder;
import org.herod.order.service.SimplePhoneAgentWorkerService.OrderQueryService;
import org.herod.order.service.SimplePhoneAgentWorkerService.OrderStatusUpdateService;
import org.herod.order.service.SimplePhoneAgentWorkerService.OrderUpdateService;
import org.herod.order.service.SimplePhoneBuyerService.OrderDas;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

/**
 * 
 * 
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 * 
 */
public class SimpleOrderDas implements OrderStatusFinder, OrderQueryService,
		OrderStatusUpdateService, OrderUpdateService, OrderDas {
	private static final String SELECT_FROM_ORDERS_SQL = "SELECT O.SERIAL_NUMBER,O.BUYER_PHONE,O.BUYER_NAME,O.AGENT_ID,O.SHOP_ID,"
			+ "O.DELIVERY_WORKER_ID,O.STATUS,O.SUBMIT_TIME,O.PREPARE_TIME,O.COMPLETE_TIME,O.COMMENT, "
			+ "O.DELIVERY_ADDRESS, O.DELIVERY_LONGITUDE, O.DELIVERY_LATITUDE, "
			+ "S.NAME SHOP_NAME, S.CONTACT_NUMBER SHOP_PHONE, S.ADDRESS SHOP_ADDRESS, "
			+ "S.LONGITUDE SHOP_LONGITUDE, S.LATITUDE SHOP_LATITUDE, "
			+ "O.COST_OF_RUN_ERRANDS,O.SHOP_COST_OF_RUN_ERRANDS,O.SHOP_MIN_CHARGE_FOR_FREE_DELIVERY "
			+ "FROM ZRH_ORDER O LEFT JOIN ZRH_SHOP S ON O.SHOP_ID = S.ID ";
	private static final String UPDATE_ORDER_STATUS_AND_COMPLETE_TIME_SQL = "UPDATE ZRH_ORDER SET STATUS = ?, "
			+ "COMPLETE_TIME = ?, DELIVERY_WORKER_ID = ? WHERE SERIAL_NUMBER = ?";
	private static final String UPDATE_ORDER_STATUS_AND_WORKER_SQL = "UPDATE ZRH_ORDER SET STATUS = ?, "
			+ "DELIVERY_WORKER_ID = ? WHERE SERIAL_NUMBER = ?";
	private static final String UPDATE_ORDER_STATUS_SQL = "UPDATE ZRH_ORDER SET STATUS = ? WHERE SERIAL_NUMBER = ?";
	private static final String UPDATE_ORDER_COMMENT_SQL = "UPDATE ZRH_ORDER SET COMMENT = ? WHERE SERIAL_NUMBER = ?";
	private static final String INSERT_ORDER_SQL = "INSERT INTO ZRH_ORDER "
			+ "(SERIAL_NUMBER,BUYER_PHONE,BUYER_NAME,AGENT_ID,"
			+ "SHOP_ID,DELIVERY_WORKER_ID,STATUS,SUBMIT_TIME,PREPARE_TIME,COMPLETE_TIME,"
			+ "COMMENT,DELIVERY_ADDRESS,DELIVERY_LONGITUDE,DELIVERY_LATITUDE, "
			+ "COST_OF_RUN_ERRANDS,SHOP_COST_OF_RUN_ERRANDS,SHOP_MIN_CHARGE_FOR_FREE_DELIVERY) "
			+ "VALUES (:serialNumber,:buyerPhone,:buyerName,:agentId,:shopId,"
			+ ":workerId,:status,:submitTime,:prepareTime,:completeTime,:comment,"
			+ ":deliveryAddress.address,:deliveryAddress.location.longitude,"
			+ ":deliveryAddress.location.latitude,:costOfRunErrands,:shopCostOfRunErrands,:shopMinChargeForFreeDelivery)";
	@Autowired
	private HerodJdbcTemplate herodJdbcTemplate;
	@Autowired
	private OrderItemQueryService orderItemQueryService;

	@Override
	public OrderStatus findOrderStatus(String serialNumber) {
		RowMapper<OrderStatus> rm = new HerodSingleColumnRowMapper<OrderStatus>(
				OrderStatus.class);
		List<OrderStatus> statuses = herodJdbcTemplate.query(
				"SELECT STATUS FROM ZRH_ORDER WHERE SERIAL_NUMBER = ?", rm,
				serialNumber);
		if (CollectionUtils.isNotEmpty(statuses)) {
			return statuses.get(0);
		}
		return OrderStatus.Unsubmit;
	}

	@Override
	public void addOrders(List<Order> orders) {
		SqlParameterSource[] batchArgs = new SqlParameterSource[orders.size()];
		for (int i = 0; i < orders.size(); i++) {
			batchArgs[i] = new HerodBeanPropertySqlParameterSource(
					orders.get(i));
		}
		herodJdbcTemplate.batchUpdate(INSERT_ORDER_SQL, batchArgs);
	}

	@Override
	public void updateOrderComment(String serialNumber, String newComment) {
		herodJdbcTemplate.update(UPDATE_ORDER_COMMENT_SQL, newComment,
				serialNumber);
	}

	@Override
	public void updateOrderStatus(String serialNumber, OrderStatus status) {
		herodJdbcTemplate.update(UPDATE_ORDER_STATUS_SQL, status, serialNumber);
	}

	@Override
	public void updateOrderStatusAndWorker(String serialNumber,
			long agentWorker, OrderStatus status) {
		herodJdbcTemplate.update(UPDATE_ORDER_STATUS_AND_WORKER_SQL, status,
				agentWorker, serialNumber);
	}

	@Override
	public void updateOrderStatusAndCompleteTime(long workerId,
			String serialNumber, OrderStatus status, Date completeTime) {
		herodJdbcTemplate.update(UPDATE_ORDER_STATUS_AND_COMPLETE_TIME_SQL,
				status, completeTime, workerId, serialNumber);
	}

	@Override
	public List<Order> findOrdersBySerialNumbers(List<String> serialNumbers) {
		RowMapper<Order> rm = new HerodBeanPropertyRowMapper<Order>(Order.class);
		List<Order> orders = herodJdbcTemplate.query(SELECT_FROM_ORDERS_SQL
				+ " WHERE SERIAL_NUMBER IN (:serialNumbers)", rm,
				Collections.singletonMap("serialNumbers", serialNumbers));
		return queryOrderItemsAndFillOrders(orders);
	}

	@Override
	public List<Order> findWaitAcceptOrders(long workerId, long workerAgentId) {
		return queryOrders(
				" WHERE O.AGENT_ID = ? AND (O.STATUS = ? OR O.STATUS = ?) ORDER BY SUBMIT_TIME ",
				workerAgentId, OrderStatus.Submitted, OrderStatus.Rejected);
	}

	@Override
	public List<Order> findWaitCompleteOrders(long workerId) {
		return queryOrders(
				" WHERE DELIVERY_WORKER_ID = ? AND O.STATUS = ? ORDER BY SUBMIT_TIME DESC",
				workerId, OrderStatus.Acceptted);
	}

	@Override
	public List<Order> findHistoryOrdersByWorkerAndStatus(long workerId,
			OrderStatus status, int begin, int count) {
		return queryOrders(
				" WHERE DELIVERY_WORKER_ID = ? AND O.STATUS = ? ORDER BY COMPLETE_TIME DESC LIMIT ?, ?",
				workerId, status, begin, count);
	}

	protected List<Order> queryOrders(String whereConditionSql, Object... args) {
		RowMapper<Order> rm = new HerodBeanPropertyRowMapper<Order>(Order.class);
		List<Order> orders = herodJdbcTemplate.query(SELECT_FROM_ORDERS_SQL
				+ whereConditionSql, rm, args);
		return queryOrderItemsAndFillOrders(orders);
	}

	private List<Order> queryOrderItemsAndFillOrders(List<Order> orders) {
		List<String> serialNumbers = getOrderSerialNumbers(orders);
		List<OrderItem> orderItems = orderItemQueryService
				.findOrderItemsBySerialNumber(serialNumbers);
		for (OrderItem orderItem : orderItems) {
			for (Order order : orders) {
				if (orderItem.getOrderSerialNumber().equals(
						order.getSerialNumber())) {
					order.addOrderItem(orderItem);
					break;
				}
			}
		}
		return orders;
	}

	private List<String> getOrderSerialNumbers(List<Order> orders) {
		List<String> serialNumbers = new ArrayList<String>();
		for (Order order : orders) {
			serialNumbers.add(order.getSerialNumber());
		}
		return serialNumbers;
	}

	@Override
	public boolean isOrderExists(Set<String> serialNumbers) {
		if (CollectionUtils.isEmpty(serialNumbers)) {
			return true;
		}
		int count = herodJdbcTemplate
				.queryForInt(
						"SELECT COUNT(1) FROM ZRH_ORDER WHERE SERIAL_NUMBER IN (:SERIALNUMBERS)",
						Collections
								.singletonMap("SERIALNUMBERS", serialNumbers));
		return count > 0;
	}

	public static interface OrderItemQueryService {
		List<OrderItem> findOrderItemsBySerialNumber(
				List<String> orderSerialNumbers);
	}

	public void setHerodJdbcTemplate(HerodJdbcTemplate herodJdbcTemplate) {
		this.herodJdbcTemplate = herodJdbcTemplate;
	}

	public void setOrderItemQueryService(
			OrderItemQueryService orderItemQueryService) {
		this.orderItemQueryService = orderItemQueryService;
	}

}
