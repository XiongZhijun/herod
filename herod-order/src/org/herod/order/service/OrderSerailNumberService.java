/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.order.service;

import static org.herod.order.MediaType.DEFAULT_MEDIA_TYPE;

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

	@GET
	@Path("transaction")
	@Produces(DEFAULT_MEDIA_TYPE)
	String buildTransactionSerialNumber();
}
