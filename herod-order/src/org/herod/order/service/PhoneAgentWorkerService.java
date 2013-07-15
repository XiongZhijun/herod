/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.order.service;

import static org.herod.order.MediaType.DEFAULT_MEDIA_TYPE;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.PUT;
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
	 * @param name
	 *            登录名
	 * @param password
	 *            密码
	 * @param imei
	 *            手机的imei号
	 * @return
	 */
	Token login(String name, String password, String imei);

	@GET
	@Path("orders/waitaccept")
	@Produces(DEFAULT_MEDIA_TYPE)
	List<Order> findWaitAcceptOrders();

	@GET
	@Path("orders/waitcomplete")
	@Produces(DEFAULT_MEDIA_TYPE)
	List<Order> findWaitCompleteOrders();

	@GET
	@Path("orders/completed")
	@Produces(DEFAULT_MEDIA_TYPE)
	List<Order> findCompletedOrders();

	@GET
	@Path("orders/canceled")
	@Produces(DEFAULT_MEDIA_TYPE)
	List<Order> findCanceledOrders();

	/**
	 * 受理订单
	 * 
	 * @param serialNumber
	 * @return
	 */
	@PUT
	@Path("orders/{serialNumber}/accept")
	@Produces(DEFAULT_MEDIA_TYPE)
	Result acceptOrder(@PathParam("serialNumber") String serialNumber);

	/**
	 * 更新订单
	 * 
	 * @param updateInfo
	 * @param reason
	 * @return
	 */
	@PUT
	@Path("orders/{serialNumber}/update")
	@Produces(DEFAULT_MEDIA_TYPE)
	Result updateOrder(OrderUpdateInfo updateInfo);

	/**
	 * 拒绝订单
	 * 
	 * @param serialNumber
	 * @param reason
	 * @return
	 */
	@PUT
	@Path("orders/{serialNumber}/reject")
	@Produces(DEFAULT_MEDIA_TYPE)
	Result rejectOrder(@PathParam("serialNumber") String serialNumber,
			@QueryParam("reason") String reason);

	/**
	 * 取消订单
	 * 
	 * @param serialNumber
	 * @param reason
	 * @return
	 */
	@PUT
	@Path("orders/{serialNumber}/cancel")
	@Produces(DEFAULT_MEDIA_TYPE)
	Result cancelOrder(@PathParam("serialNumber") String serialNumber,
			@QueryParam("reason") String reason);

	/**
	 * 完成订单
	 * 
	 * @param serialNumber
	 * @return
	 */
	@PUT
	@Path("orders/{serialNumber}/complete")
	@Produces(DEFAULT_MEDIA_TYPE)
	Result completeOrder(@PathParam("serialNumber") String serialNumber);
}
