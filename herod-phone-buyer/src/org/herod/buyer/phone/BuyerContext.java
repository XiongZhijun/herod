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
	private static ShopService shopService;
	static {
		BuyerServiceProxy buyerServiceProxy = new BuyerServiceProxy(
				new BuyerServiceMock());
		buyerService = buyerServiceProxy;
		shopService = buyerServiceProxy;
	}

	public static BuyerService getBuyerService() {
		return buyerService;
	}

	public static void setBuyerService(BuyerService buyerService) {
		BuyerContext.buyerService = buyerService;
	}

	public static ShopService getShopService() {
		return shopService;
	}

	public static void setShopService(ShopService shopService) {
		BuyerContext.shopService = shopService;
	}

}
