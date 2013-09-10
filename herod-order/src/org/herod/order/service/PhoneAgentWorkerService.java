/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.order.service;

import static org.herod.order.MediaType.DEFAULT_MEDIA_TYPE;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.herod.order.model.Order;
import org.herod.order.model.OrderUpdateInfo;
import org.herod.order.model.Token;

/**
 * 代理商工作人员手机用户服务
 * 
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 * 
 */
@Path("/herod/agentworker")
public interface PhoneAgentWorkerService {

	/**
	 * 登录系统
	 * 
	 * @param phone
	 *            手机号
	 * @param password
	 *            密码
	 * @param imei
	 *            手机的imei号
	 * @return
	 */
	@POST
	@Path("login")
	@Produces(DEFAULT_MEDIA_TYPE)
	Token login(@QueryParam("workerPhone") String phone,
			@QueryParam("workerPassword") String password,
			@QueryParam("imei") String imei);

	@GET
	@Path("orders/waitaccept")
	@Produces(DEFAULT_MEDIA_TYPE)
	List<Order> findWaitAcceptOrders(@QueryParam("token") String token,
			@QueryParam("imei") String imei);

	@GET
	@Path("orders/waitcomplete")
	@Produces(DEFAULT_MEDIA_TYPE)
	List<Order> findWaitCompleteOrders(@QueryParam("token") String token,
			@QueryParam("imei") String imei);

	@GET
	@Path("orders/completed")
	@Produces(DEFAULT_MEDIA_TYPE)
	List<Order> findCompletedOrders(@QueryParam("token") String token,
			@QueryParam("imei") String imei, @QueryParam("begin") int begin,
			@QueryParam("count") int count);

	@GET
	@Path("orders/canceled")
	@Produces(DEFAULT_MEDIA_TYPE)
	List<Order> findCanceledOrders(@QueryParam("token") String token,
			@QueryParam("imei") String imei, @QueryParam("begin") int begin,
			@QueryParam("count") int count);

	/**
	 * 受理订单
	 * 
	 * @param serialNumber
	 * @return
	 */
	@POST
	@Path("orders/{serialNumber}/accept")
	@Produces(DEFAULT_MEDIA_TYPE)
	Result acceptOrder(@PathParam("serialNumber") String serialNumber,
			@QueryParam("token") String token, @QueryParam("imei") String imei);

	/**
	 * 更新订单
	 * 
	 * @param updateInfo
	 * @param reason
	 * @return
	 */
	@POST
	@Path("orders/{serialNumber}/update")
	@Produces(DEFAULT_MEDIA_TYPE)
	Result updateOrder(OrderUpdateInfo updateInfo,
			@QueryParam("token") String token, @QueryParam("imei") String imei);

	/**
	 * 拒绝订单
	 * 
	 * @param serialNumber
	 * @param reason
	 * @return
	 */
	@POST
	@Path("orders/{serialNumber}/reject")
	@Produces(DEFAULT_MEDIA_TYPE)
	Result rejectOrder(@PathParam("serialNumber") String serialNumber,
			@QueryParam("reason") String reason,
			@QueryParam("token") String token, @QueryParam("imei") String imei);

	/**
	 * 取消订单
	 * 
	 * @param serialNumber
	 * @param reason
	 * @return
	 */
	@POST
	@Path("orders/{serialNumber}/cancel")
	@Produces(DEFAULT_MEDIA_TYPE)
	Result cancelOrder(@PathParam("serialNumber") String serialNumber,
			@QueryParam("reason") String reason,
			@QueryParam("token") String token, @QueryParam("imei") String imei);

	/**
	 * 完成订单
	 * 
	 * @param serialNumber
	 * @return
	 */
	@POST
	@Path("orders/{serialNumber}/complete")
	@Produces(DEFAULT_MEDIA_TYPE)
	Result completeOrder(@PathParam("serialNumber") String serialNumber,
			@QueryParam("token") String token, @QueryParam("imei") String imei);
}
