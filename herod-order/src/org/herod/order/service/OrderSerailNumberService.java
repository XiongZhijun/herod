/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.order.service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

/**
 * 
 * 
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 * 
 */
@Path("/herod/sn")
public interface OrderSerailNumberService {
	public static final String DEFAULT_MEDIA_TYPE = "application/json;charset=utf-8";

	@GET
	@Path("transaction")
	@Produces(DEFAULT_MEDIA_TYPE)
	String buildTransactionSerialNumber();
}
