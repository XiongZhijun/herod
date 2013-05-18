/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.order.service;

import java.util.List;
import java.util.Map;

import org.herod.order.model.Address;
import org.herod.order.model.Goods;
import org.herod.order.model.GoodsCategory;
import org.herod.order.model.Location;
import org.herod.order.model.Order;
import org.herod.order.model.Shop;

/**
 * 提供给手机买家的服务
 * 
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 * 
 */
public interface PhoneBuyerService {

	/**
	 * 根据买家的地理位置来读取店铺列表
	 * 
	 * @param location
	 * @return
	 */
	List<Shop> findShopsByLocation(Location location);

	/**
	 * 读取店铺的商品分类
	 * 
	 * @param shopId
	 *            店铺Id
	 * @return
	 */
	List<GoodsCategory> findGoodsCategoryByShop(long shopId);

	/**
	 * 根据店铺的商品分类读取店铺商品
	 * 
	 * @param shopId
	 *            店铺
	 * @param categoryId
	 *            商品分类
	 * @return
	 */
	List<Goods> findGoodsByCategory(long shopId, long categoryId);

	/**
	 * 提交订单
	 * 
	 * @param orders
	 *            订单
	 * @return
	 */
	Result submitOrders(List<Order> orders);

	/**
	 * 根据订单流水号读取订单详细信息
	 * 
	 * @param serialNumbers
	 *            订单流水号
	 * @return
	 */
	Map<String, Order> findOrdersBySerialNumber(List<String> serialNumbers);

	/**
	 * 读取买家常用地址
	 * 
	 * @param phone
	 *            买家手机号
	 * @return
	 */
	List<Address> findUsedAddress(String phone);
}
