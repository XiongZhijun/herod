/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.buyer.phone;

import org.herod.buyer.phone.rest.RestBuyerService;
import org.herod.order.common.OrderContext;

import android.content.Context;

/**
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 * 
 */
public class BuyerContext {

	private static BuyerServiceProxy buyerService;

	public static void init(Context context) {
		if (buyerService == null) {
			buyerService = new BuyerServiceProxy(new RestBuyerService(context));
		}
		OrderContext.init(context);
	}

	public static BuyerService getBuyerService() {
		return buyerService;
	}

	public static ShopService getShopService() {
		return buyerService;
	}
}
