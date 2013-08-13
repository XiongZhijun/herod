/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.order.das;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.collections.CollectionUtils;
import org.herod.common.das.HerodBeanPropertyRowMapper;
import org.herod.common.das.HerodBeanPropertySqlParameterSource;
import org.herod.common.das.HerodJdbcTemplate;
import org.herod.order.das.SimpleOrderDas.OrderItemQueryService;
import org.herod.order.model.OrderItem;
import org.herod.order.model.OrderItemFlag;
import org.herod.order.service.SimplePhoneAgentWorkerService.OrderItemUpdateService;
import org.herod.order.service.SimplePhoneBuyerService.OrderItemDas;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

/**
 * 
 * 
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 * 
 */
public class SimpleOrderItemDas implements OrderItemUpdateService,
		OrderItemDas, OrderItemQueryService {
	private static final String UPDATE_ITEM_QUANTITY_SQL = "UPDATE ZRH_ORDER_ITEM SET QUANTITY = ?, FLAG = ? WHERE SERIAL_NUMBER = ?";
	private static final String DELETE_ITEM_SQL = "UPDATE ZRH_ORDER_ITEM SET QUANTITY = 0, FLAG = ? WHERE SERIAL_NUMBER = ?";
	private static final String INSERT_ORDER_ITEM_SQL = "INSERT INTO ZRH_ORDER_ITEM (SERIAL_NUMBER,"
			+ "ORDER_SERIAL_NUMBER,GOODS_ID,GOODS_CODE,AGENT_ID,"
			+ "SHOP_ID,SELLING_PRICE,QUANTITY,FLAG,ORIGINAL_QUANTITY) "
			+ "VALUES (:serialNumber,:orderSerialNumber,:goodsId,"
			+ ":goodsCode,:agentId,:shopId,:sellingPrice,:quantity,:flag,:quantity) ";
	@Autowired
	@Qualifier("herodJdbcTemplate")
	private HerodJdbcTemplate herodJdbcTemplate;

	@Override
	public void addOrderItems(List<OrderItem> orderItems) {
		SqlParameterSource[] batchArgs = new SqlParameterSource[orderItems
				.size()];
		for (int i = 0; i < orderItems.size(); i++) {
			batchArgs[i] = new HerodBeanPropertySqlParameterSource(
					orderItems.get(i));
		}
		herodJdbcTemplate.batchUpdate(INSERT_ORDER_ITEM_SQL, batchArgs);
	}

	@Override
	public void deleteOrderItem(List<String> orderItemSerialNumbers) {
		List<Object[]> batchArgs = new ArrayList<Object[]>();
		for (String serialNumber : orderItemSerialNumbers) {
			batchArgs.add(new Object[] { OrderItemFlag.Deleted, serialNumber });
		}
		herodJdbcTemplate.batchUpdate(DELETE_ITEM_SQL, batchArgs);
	}

	@Override
	public void changeOrderItemQuantity(
			Map<String, Integer> orderItemQuantityChangeMap) {
		List<Object[]> batchArgs = new ArrayList<Object[]>();
		for (Entry<String, Integer> entry : orderItemQuantityChangeMap
				.entrySet()) {
			batchArgs.add(new Object[] { entry.getValue(),
					OrderItemFlag.Updated, entry.getKey() });
		}
		herodJdbcTemplate.batchUpdate(UPDATE_ITEM_QUANTITY_SQL, batchArgs);
	}

	public void setHerodJdbcTemplate(HerodJdbcTemplate herodJdbcTemplate) {
		this.herodJdbcTemplate = herodJdbcTemplate;
	}

	@Override
	public List<OrderItem> findOrderItemsBySerialNumber(
			List<String> orderSerialNumbers) {
		if (CollectionUtils.isEmpty(orderSerialNumbers)) {
			return Collections.emptyList();
		}
		RowMapper<OrderItem> rm = new HerodBeanPropertyRowMapper<OrderItem>(
				OrderItem.class);
		return herodJdbcTemplate
				.query("SELECT O.SERIAL_NUMBER,O.ORDER_SERIAL_NUMBER,O.GOODS_ID, "
						+ "O.GOODS_CODE,O.AGENT_ID,O.SHOP_ID,O.SELLING_PRICE,O.SUPPLY_PRICE, "
						+ "O.QUANTITY,O.FLAG,G.NAME GOODS_NAME,S.NAME SHOP_NAME "
						+ "FROM ZRH_ORDER_ITEM O,ZRH_GOODS G, ZRH_SHOP S "
						+ "WHERE O.ORDER_SERIAL_NUMBER IN (:ids) AND S.ID = O.SHOP_ID "
						+ "AND G.ID = O.GOODS_ID", rm,
						Collections.singletonMap("ids", orderSerialNumbers));
	}

}
