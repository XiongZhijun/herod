/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.buyer.phone;

import org.herod.buyer.phone.mocks.BuyerServiceMock;

/**
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 * 
 */
public class BuyerContext {

	private static BuyerService buyerService = new BuyerServiceMock();

	public static BuyerService getBuyerService() {
		return buyerService;
	}

	public static void setBuyerService(BuyerService buyerService) {
		BuyerContext.buyerService = buyerService;
	}

}
