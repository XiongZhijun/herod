/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.order.das;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.herod.common.das.HerodBeanPropertySqlParameterSource;
import org.herod.order.model.OrderItem;
import org.herod.order.model.OrderItemFlag;
import org.herod.order.service.SimplePhoneAgentWorkerService.OrderItemUpdateService;
import org.herod.order.service.SimplePhoneBuyerService.OrderItemDas;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

/**
 * 
 * 
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 * 
 */
public class SimpleOrderItemDas implements OrderItemUpdateService, OrderItemDas {
	private static final String UPDATE_ITEM_QUANTITY_SQL = "UPDATE HEROD_ORDER_ITEMS SET QUANTITY = ? WHERE SERIAL_NUMBER = ?";
	private static final String UPDATE_ITEM_FLAG_SQL = "UPDATE HEROD_ORDER_ITEMS SET FLAG = ? WHERE SERIAL_NUMBER = ?";
	private static final String INSERT_ORDER_ITEM_SQL = "INSERT INTO HEROD_ORDER_ITEMS (SERIAL_NUMBER,"
			+ "ORDER_SERIAL_NUMBER,GOODS_ID,GOODS_CODE,AGENT_ID,"
			+ "SHOP_ID,UNIT_PRICE,QUANTITY,FLAG) "
			+ "VALUES (:serialNumber,:orderSerialNumber,:goodsId,"
			+ ":goodsCode,:agentId,:shopId,:unitPrice,:quantity,:flag) ";
	@Autowired
	@Qualifier("simpleJdbcTemplate")
	private SimpleJdbcTemplate simpleJdbcTemplate;

	@Override
	public void addOrderItems(List<OrderItem> orderItems) {
		SqlParameterSource[] batchArgs = new SqlParameterSource[orderItems
				.size()];
		for (int i = 0; i < orderItems.size(); i++) {
			batchArgs[i] = new HerodBeanPropertySqlParameterSource(
					orderItems.get(i));
		}
		simpleJdbcTemplate.batchUpdate(INSERT_ORDER_ITEM_SQL, batchArgs);
	}

	@Override
	public void deleteOrderItem(List<String> orderItemSerialNumbers) {
		List<Object[]> batchArgs = new ArrayList<Object[]>();
		for (String serialNumber : orderItemSerialNumbers) {
			batchArgs.add(new Object[] { OrderItemFlag.Deleted, serialNumber });
		}
		simpleJdbcTemplate.batchUpdate(UPDATE_ITEM_FLAG_SQL, batchArgs);
	}

	@Override
	public void changeOrderItemQuantity(
			Map<String, Integer> orderItemQuantityChangeMap) {
		List<Object[]> batchArgs = new ArrayList<Object[]>();
		for (Entry<String, Integer> entry : orderItemQuantityChangeMap
				.entrySet()) {
			batchArgs.add(new Object[] { entry.getValue(), entry.getKey() });
		}
		simpleJdbcTemplate.batchUpdate(UPDATE_ITEM_QUANTITY_SQL, batchArgs);
	}

	public void setSimpleJdbcTemplate(SimpleJdbcTemplate simpleJdbcTemplate) {
		this.simpleJdbcTemplate = simpleJdbcTemplate;
	}

}
