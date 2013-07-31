/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.order.das;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.herod.common.das.HerodBeanPropertyRowMapper;
import org.herod.common.das.HerodBeanPropertySqlParameterSource;
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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

/**
 * 
 * 
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 * 
 */
public class SimpleOrderDas implements OrderStatusFinder, OrderQueryService,
		OrderStatusUpdateService, OrderUpdateService, OrderDas {
	private static final String SELECT_FROM_ORDERS_SQL = "SELECT SERIAL_NUMBER,BUYER_PHONE,BUYER_NAME,AGENT_ID,SHOP_ID,"
			+ "DELIVERY_WORKER_ID,STATUS,SUBMIT_TIME,PREPARE_TIME,COMPLETE_TIME,COMMENT, "
			+ "DELIVERY_ADDRESS, DELIVERY_LONGITUDE, DELIVERY_LATITUDE "
			+ "FROM ZRH_ORDER ";
	private static final String UPDATE_ORDER_STATUS_AND_COMPLETE_TIME_SQL = "UPDATE ZRH_ORDER SET STATUS = ?, "
			+ "COMPLETE_TIME = ? WHERE SERIAL_NUMBER = ?";
	private static final String UPDATE_ORDER_STATUS_AND_WORKER_SQL = "UPDATE ZRH_ORDER SET STATUS = ?, "
			+ "DELIVERY_WORKER_ID = ? WHERE SERIAL_NUMBER = ?";
	private static final String UPDATE_ORDER_STATUS_SQL = "UPDATE ZRH_ORDER SET STATUS = ? WHERE SERIAL_NUMBER = ?";
	private static final String UPDATE_ORDER_COMMENT_SQL = "UPDATE ZRH_ORDER SET COMMENT = ? WHERE SERIAL_NUMBER = ?";
	private static final String INSERT_ORDER_SQL = "INSERT INTO ZRH_ORDER "
			+ "(SERIAL_NUMBER,BUYER_PHONE,BUYER_NAME,AGENT_ID,"
			+ "SHOP_ID,DELIVERY_WORKER_ID,STATUS,SUBMIT_TIME,PREPARE_TIME,COMPLETE_TIME,"
			+ "COMMENT,DELIVERY_ADDRESS,DELIVERY_LONGITUDE,DELIVERY_LATITUDE) "
			+ "VALUES (:serialNumber,:buyerPhone,:buyerName,:agentId,:shopId,"
			+ ":workerId,:status,:submitTime,:prepareTime,:completeTime,:comment,"
			+ ":deliveryAddress.address,:deliveryAddress.location.longitude,"
			+ ":deliveryAddress.location.latitude)";
	@Autowired
	@Qualifier("simpleJdbcTemplate")
	private SimpleJdbcTemplate simpleJdbcTemplate;
	@Autowired
	private OrderItemQueryService orderItemQueryService;

	@Override
	public OrderStatus findOrderStatus(String serialNumber) {
		RowMapper<OrderStatus> rm = new HerodSingleColumnRowMapper<OrderStatus>(
				OrderStatus.class);
		List<OrderStatus> statuses = simpleJdbcTemplate.query(
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
		// TODO 需要把跑腿费等信息保存到数据库中
		simpleJdbcTemplate.batchUpdate(INSERT_ORDER_SQL, batchArgs);
	}

	@Override
	public void updateOrderComment(String serialNumber, String newComment) {
		simpleJdbcTemplate.update(UPDATE_ORDER_COMMENT_SQL, newComment,
				serialNumber);
	}

	@Override
	public void updateOrderStatus(String serialNumber, OrderStatus status) {
		simpleJdbcTemplate
				.update(UPDATE_ORDER_STATUS_SQL, status, serialNumber);
	}

	@Override
	public void updateOrderStatusAndWorker(String serialNumber,
			long agentWorker, OrderStatus status) {
		simpleJdbcTemplate.update(UPDATE_ORDER_STATUS_AND_WORKER_SQL, status,
				agentWorker, serialNumber);
	}

	@Override
	public void updateOrderStatusAndCompleteTime(String serialNumber,
			OrderStatus status, Date completeTime) {
		simpleJdbcTemplate.update(UPDATE_ORDER_STATUS_AND_COMPLETE_TIME_SQL,
				status, completeTime, serialNumber);
	}

	@Override
	public List<Order> findOrdersBySerialNumbers(List<String> serialNumbers) {
		return queryOrders(" WHERE SERIAL_NUMBER IN ?", serialNumbers);
	}

	@Override
	public List<Order> findWaitAcceptOrders(long workerId) {
		return queryOrders(
				" WHERE DELIVERY_WORKER_ID = ? AND (STATUS = ? OR STATUS = ?)", workerId,
				OrderStatus.Submitted.name(), OrderStatus.Rejected.name());
	}

	@Override
	public List<Order> findOrdersByWorkerAndStatus(long workerId,
			OrderStatus status) {
		return queryOrders(" WHERE DELIVERY_WORKER_ID = ? AND STATUS = ?",
				workerId, OrderStatus.Acceptted.name());

	}

	@Override
	public List<Order> findOrdersByWorkerAndStatusWithOneDay(long workerId,
			OrderStatus status) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		Date now = calendar.getTime();
		return queryOrders(
				" WHERE SUBMIT_TIME > ? AND DELIVERY_WORKER_ID = ? AND STATUS = ?",
				now, workerId, OrderStatus.Acceptted.name());
	}

	protected List<Order> queryOrders(String whereConditionSql, Object... args) {
		RowMapper<Order> rm = new HerodBeanPropertyRowMapper<Order>(Order.class);
		List<Order> orders = simpleJdbcTemplate.query(SELECT_FROM_ORDERS_SQL
				+ whereConditionSql, rm, args);
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

	public static interface OrderItemQueryService {
		List<OrderItem> findOrderItemsBySerialNumber(
				List<String> orderSerialNumbers);
	}

	public void setSimpleJdbcTemplate(SimpleJdbcTemplate simpleJdbcTemplate) {
		this.simpleJdbcTemplate = simpleJdbcTemplate;
	}

}
