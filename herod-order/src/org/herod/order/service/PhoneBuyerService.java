/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.order.service;

import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.herod.order.model.Address;
import org.herod.order.model.Order;

/**
 * 提供给手机买家的服务
 * 
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 * 
 */
@Path("/herod/order")
public interface PhoneBuyerService {
	public static final String DEFAULT_MEDIA_TYPE = "application/json;charset=utf-8";

	@GET
	@Path("shopTypes")
	@Produces(DEFAULT_MEDIA_TYPE)
	List<Map<String, Object>> findShopTypes(
			@QueryParam("latitude") double latitude,
			@QueryParam("longitude") double longitude);

	@GET
	@Path("/shopTypes/{typeId}/shops")
	@Produces(DEFAULT_MEDIA_TYPE)
	List<Map<String, Object>> findShopesByType(
			@PathParam("typeId") long typeId,
			@QueryParam("latitude") double latitude,
			@QueryParam("longitude") double longitude);

	@GET
	@Path("shops/{shopId}")
	@Produces(DEFAULT_MEDIA_TYPE)
	Map<String, Object> findShopById(@PathParam("shopId") long shopId,
			@QueryParam("latitude") double latitude,
			@QueryParam("longitude") double longitude);

	@GET
	@Path("shops/{shopId}/goodsTypes")
	@Produces(DEFAULT_MEDIA_TYPE)
	List<Map<String, Object>> findGoodsTypesByShop(
			@PathParam("shopId") long shopId,
			@QueryParam("latitude") double latitude,
			@QueryParam("longitude") double longitude);

	@GET
	@Path("goodsTypes/{goodsTypeId}/goodses")
	@Produces(DEFAULT_MEDIA_TYPE)
	List<Map<String, Object>> findGoodsesByType(
			@PathParam("goodsTypeId") long goodsTypeId,
			@QueryParam("begin") int begin, @QueryParam("count") int count,
			@QueryParam("latitude") double latitude,
			@QueryParam("longitude") double longitude);

	@GET
	@Path("goodses")
	@Produces(DEFAULT_MEDIA_TYPE)
	List<Map<String, Object>> searchGoodses(
			@QueryParam("goodsName") String goodsName,
			@QueryParam("begin") int begin, @QueryParam("count") int count,
			@QueryParam("latitude") double latitude,
			@QueryParam("longitude") double longitude);

	/**
	 * 提交订单
	 * 
	 * @param orders
	 *            订单
	 * @return
	 */
	@POST
	@Produces(DEFAULT_MEDIA_TYPE)
	Result submitOrders(String ordersJson,
			@QueryParam("latitude") double latitude,
			@QueryParam("longitude") double longitude);

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
