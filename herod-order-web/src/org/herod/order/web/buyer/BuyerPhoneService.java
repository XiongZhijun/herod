/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.order.web.buyer;

import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

/**
 * 
 * 
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 * 
 */
@Path("/herod/order")
public interface BuyerPhoneService {

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
}
