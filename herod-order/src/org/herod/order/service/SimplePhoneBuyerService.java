/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.order.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.wink.common.annotations.Workspace;
import org.herod.common.das.HerodColumnMapRowMapper;
import org.herod.order.model.Address;
import org.herod.order.model.AgentWorker;
import org.herod.order.model.Order;
import org.herod.order.model.OrderItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * 
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 * 
 */
@Transactional
@Workspace(collectionTitle = "Goods And Orders Service", workspaceTitle = "Order System")
public class SimplePhoneBuyerService implements PhoneBuyerService {
	@Autowired
	private OrderDas orderDas;
	@Autowired
	private OrderItemDas orderItemDas;
	@Autowired
	private BuyerUsedAddressQueryService buyerUsedAddressQueryService;
	@Autowired
	private OrderLogService orderLogService;
	@Autowired
	private DeliveryWorkerAllocationStrategy deliveryWorkerAllocationStrategy;
	@Autowired
	@Qualifier("simpleJdbcTemplate")
	private SimpleJdbcTemplate simpleJdbcTemplate;

	@Override
	public Result submitOrders(String ordersJson, double latitude,
			double longitude) {
		OrderList orderList = OrderList.convertToOrderList(ordersJson,
				longitude, latitude);
		if (orderList.isEmpty()) {
			return new SimpleResult(ResultCode.NoOrderSubmitted);
		}
		if (orderDas.isOrderExists(orderList.getSerialNumbers())) {
			return new SimpleResult(ResultCode.SomeOrderIsExists);
		}
		List<Order> orders = orderList.getOrders();
		orderDas.addOrders(orders);
		orderItemDas.addOrderItems(orderList.getOrderItems());
		orderLogService.buyerSubmitLog(orders);
		return Result.SUCCESS;
	}

	@Override
	public Map<String, Order> findOrdersBySerialNumber(String[] serialNumbers) {
		Map<String, Order> orderMap = new HashMap<String, Order>();
		List<Order> orders = orderDas.findOrdersBySerialNumbers(Arrays
				.asList(serialNumbers));
		for (Order order : orders) {
			orderMap.put(order.getSerialNumber(), order);
		}
		return orderMap;
	}

	@Override
	public List<Address> findUsedAddress(String phone) {
		return buyerUsedAddressQueryService.findUsedAddressesByPhone(phone);
	}

	@Override
	public List<Map<String, Object>> findShopTypes(double latitude,
			double longitude) {
		Set<Object> shopTypeIds = new HashSet<Object>();
		for (Map<String, Object> shop : getAllShops()) {
			ServiceAreaWrapper serviceArea = new ServiceAreaWrapper(shop);
			if (serviceArea.isInServiceArea(latitude, longitude)) {
				shopTypeIds.add(getLong(shop, "shopTypeId"));
			}
		}
		if (CollectionUtils.isEmpty(shopTypeIds)) {
			return Collections.emptyList();
		}
		RowMapper<Map<String, Object>> rm = new HerodColumnMapRowMapper();
		return simpleJdbcTemplate
				.query("SELECT ID,NAME,IMAGE_URL FROM ZRH_SHOP_TYPE WHERE DELETE_FLAG = 0 AND ID IN (:shopTypeIds) ORDER BY SORT",
						rm,
						Collections.singletonMap("shopTypeIds", shopTypeIds));
	}

	@Override
	public List<Map<String, Object>> findShopesByType(long typeId,
			double latitude, double longitude) {
		List<Map<String, Object>> nearbyShops = new ArrayList<Map<String, Object>>();
		for (Map<String, Object> shop : getAllShops()) {
			ServiceAreaWrapper serviceArea = new ServiceAreaWrapper(shop);
			if (getLong(shop, "shopTypeId") == typeId
					&& serviceArea.isInServiceArea(latitude, longitude)) {
				nearbyShops.add(shop);
			}
		}
		return nearbyShops;
	}

	@Override
	public Map<String, Object> findShopById(long shopId, double latitude,
			double longitude) {
		for (Map<String, Object> shop : getAllShops()) {
			if (getLong(shop, "id") == shopId) {
				return shop;
			}
		}
		return null;
	}

	@Override
	public List<Map<String, Object>> findGoodsTypesByShop(long shopId,
			double latitude, double longitude) {
		return queryForList(
				"SELECT ID,NAME,ALIAS,SHOP_ID,AGENT_ID, TIMESTAMP FROM ZRH_GOODS_CATEGORY WHERE SHOP_ID = ? AND DELETE_FLAG = 0 ORDER BY SORT ",
				shopId);
	}

	@Override
	public List<Map<String, Object>> findGoodsesByType(long goodsTypeId,
			int begin, int count, double latitude, double longitude) {
		return queryForList(
				"SELECT G.ID, G.NAME, G.CODE, G.ALIAS, G.SUPPLY_PRICE, "
						+ "G.SELLING_PRICE, G.UNIT, G.COMMENT, G.LARGE_IMAGE, G.THUMBNAIL, "
						+ "G.CATEGORY_ID, G.SHOP_ID, G.AGENT_ID, G.TIMESTAMP, S.NAME AS SHOP_NAME FROM ZRH_GOODS G "
						+ "LEFT JOIN ZRH_SHOP S ON G.SHOP_ID = S.ID WHERE G.CATEGORY_ID = ? AND G.DELETE_FLAG = 0 ORDER BY G.SORT LIMIT ?, ?",
				goodsTypeId, begin, count);
	}

	@Override
	public List<Map<String, Object>> searchGoodses(String goodsName, int begin,
			int count, double latitude, double longitude) {
		List<Map<String, Object>> allGoodses = queryForList(
				"SELECT G.ID, G.NAME, G.CODE, G.ALIAS, G.SUPPLY_PRICE, "
						+ "G.SELLING_PRICE, G.UNIT, G.COMMENT, G.LARGE_IMAGE, G.THUMBNAIL, "
						+ "G.CATEGORY_ID, G.SHOP_ID, G.AGENT_ID, S.NAME AS SHOP_NAME, S.SERVICE_TIMES FROM ZRH_GOODS G "
						+ "LEFT JOIN ZRH_SHOP S ON G.SHOP_ID = S.ID WHERE G.NAME LIKE ? AND G.DELETE_FLAG = 0 AND S.DELETE_FLAG = 0 ORDER BY G.SORT LIMIT ?, ?",
				"%" + goodsName + "%", begin, count);
		List<Map<String, Object>> results = new ArrayList<Map<String, Object>>();
		for (Map<String, Object> goods : allGoodses) {
			String serviceTimes = (String) goods.get("serviceTimes");
			ServiceTimeManager serviceTimeManager = new ServiceTimeManager(
					serviceTimes);
			if (serviceTimeManager.isInServiceNow()) {
				results.add(goods);
			}
		}
		return results;
	}

	private List<Map<String, Object>> queryForList(String sql, Object... args) {
		RowMapper<Map<String, Object>> rm = new HerodColumnMapRowMapper();
		return simpleJdbcTemplate.query(sql, rm, args);
	}

	private static long getLong(Map<String, Object> shop, String key) {
		Object value = shop.get(key);
		if (value == null) {
			return 0;
		}
		if (value instanceof Long || value.getClass() == long.class) {
			return (Long) value;
		}
		return Long.parseLong(value.toString());
	}

	public void setSimpleJdbcTemplate(SimpleJdbcTemplate simpleJdbcTemplate) {
		this.simpleJdbcTemplate = simpleJdbcTemplate;
	}

	private List<Map<String, Object>> getAllShops() {
		return queryForList("SELECT ID, NAME, SHOP_TYPE_ID, AGENT_ID, ADDRESS, CONTACT_NUMBER, LONGITUDE, LATITUDE, SERVICE_RADIUS, IMAGE_URL, BANK_NAME, BANK_ACCOUNT, ORGANIZATION_CODE, BUSINESS_LICENSE, LINKMAN, COMMENT, COST_OF_RUN_ERRANDS, MIN_CHARGE_FOR_FREE_DELIVERY,SERVICE_TIMES, TIMESTAMP,STATUS FROM ZRH_SHOP WHERE DELETE_FLAG = 0 ORDER BY SORT ");
	}

	public void setOrderDas(OrderDas orderDas) {
		this.orderDas = orderDas;
	}

	public void setOrderItemDas(OrderItemDas orderItemDas) {
		this.orderItemDas = orderItemDas;
	}

	public void setBuyerUsedAddressQueryService(
			BuyerUsedAddressQueryService buyerUsedAddressQueryService) {
		this.buyerUsedAddressQueryService = buyerUsedAddressQueryService;
	}

	public void setOrderLogService(OrderLogService orderLogService) {
		this.orderLogService = orderLogService;
	}

	public void setDeliveryWorkerAllocationStrategy(
			DeliveryWorkerAllocationStrategy deliveryWorkerAllocationStrategy) {
		this.deliveryWorkerAllocationStrategy = deliveryWorkerAllocationStrategy;
	}

	public static interface OrderDas {
		void addOrders(List<Order> orders);

		List<Order> findOrdersBySerialNumbers(List<String> serialNumbers);

		boolean isOrderExists(Set<String> serialNumbers);
	}

	public static interface DeliveryWorkerAllocationStrategy {
		Map<String, AgentWorker> allocate(List<Order> orders);
	}

	public static interface OrderItemDas {
		void addOrderItems(List<OrderItem> orderItems);

	}

	public static interface BuyerUsedAddressQueryService {
		List<Address> findUsedAddressesByPhone(String phone);
	}
}
