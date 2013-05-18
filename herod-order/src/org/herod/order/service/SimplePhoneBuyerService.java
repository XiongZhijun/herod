/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.order.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.herod.order.cache.ShopCache;
import org.herod.order.model.Address;
import org.herod.order.model.Goods;
import org.herod.order.model.GoodsCategory;
import org.herod.order.model.Location;
import org.herod.order.model.Order;
import org.herod.order.model.OrderItem;
import org.herod.order.model.Shop;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 
 * 
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 * 
 */
public class SimplePhoneBuyerService implements PhoneBuyerService {
	@Autowired
	private ShopCache shopCache;
	@Autowired
	private GoodsCategoryQueryService goodsCategoryQueryService;
	@Autowired
	private GoodsQueryService goodsQueryService;
	@Autowired
	private OrderDas orderDas;
	@Autowired
	private OrderItemDas orderItemDas;
	@Autowired
	private BuyerUsedAddressQueryService buyerUsedAddressQueryService;
	@Autowired
	private OrderLogService orderLogService;

	@Override
	public List<Shop> findShopsByLocation(Location location) {
		return shopCache.findShopsByLocation(location);
	}

	@Override
	public List<GoodsCategory> findGoodsCategoryByShop(long shopId) {
		return goodsCategoryQueryService.findGoodsCategoryByShop(shopId);
	}

	@Override
	public List<Goods> findGoodsByCategory(long shopId, long categoryId) {
		return goodsQueryService.findGoodsByCategory(shopId, categoryId);
	}

	@Override
	public Result submitOrders(List<Order> orders) {
		List<OrderItem> orderItems = new ArrayList<OrderItem>();
		for (Order order : orders) {
			order.setSubmitTime(new Date());
			orderItems.addAll(order.getOrderItems());
		}
		orderDas.addOrders(orders);
		orderItemDas.addOrderItems(orderItems);
		orderLogService.buyerSubmitLog(orders);
		return Result.SUCCESS;
	}

	@Override
	public Map<String, Order> findOrdersBySerialNumber(
			List<String> serialNumbers) {
		Map<String, Order> orderMap = new HashMap<String, Order>();
		List<Order> orders = orderDas.findOrdersBySerialNumbers(serialNumbers);
		for (Order order : orders) {
			orderMap.put(order.getSerialNumber(), order);
		}
		return orderMap;
	}

	@Override
	public List<Address> findUsedAddress(String phone) {
		return buyerUsedAddressQueryService.findUsedAddressesByPhone(phone);
	}

	public void setShopCache(ShopCache shopCache) {
		this.shopCache = shopCache;
	}

	public void setGoodsCategoryQueryService(
			GoodsCategoryQueryService goodsCategoryQueryService) {
		this.goodsCategoryQueryService = goodsCategoryQueryService;
	}

	public void setGoodsQueryService(GoodsQueryService goodsQueryService) {
		this.goodsQueryService = goodsQueryService;
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

	public static interface GoodsCategoryQueryService {
		List<GoodsCategory> findGoodsCategoryByShop(long shopId);
	}

	public static interface GoodsQueryService {
		List<Goods> findGoodsByCategory(long shopId, long categoryId);
	}

	public static interface OrderDas {
		void addOrders(List<Order> orders);

		List<Order> findOrdersBySerialNumbers(List<String> serialNumbers);
	}

	public static interface OrderItemDas {
		void addOrderItems(List<OrderItem> orderItems);

	}

	public static interface BuyerUsedAddressQueryService {
		List<Address> findUsedAddressesByPhone(String phone);
	}
}
