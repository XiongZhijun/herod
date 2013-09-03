/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved.
 */
package org.herod.buyer.phone;

import static org.herod.buyer.phone.Constants.CONTACT_NUMBER;
import static org.herod.order.common.Constants.CODE;
import static org.herod.order.common.Constants.ID;
import static org.herod.order.common.Constants.NAME;
import static org.herod.order.common.Constants.SELLING_PRICE;

import org.herod.framework.MapWrapper;
import org.herod.order.common.Constants;
import org.herod.order.common.model.Order;
import org.herod.order.common.model.OrderItem;

/**
 * 
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 */
public class OrderUtils {
	public static Order createOrder(MapWrapper<String, Object> shop) {
		Order order = new Order();
		double shopCostOfRunErrands = 0;
		double shopMinChargeForFreeDelivery = 0;
		if (shop != null) {
			shopCostOfRunErrands = shop
					.getDouble(Constants.COST_OF_RUN_ERRANDS);
			order.setCostOfRunErrands(shopCostOfRunErrands);
			shopMinChargeForFreeDelivery = shop
					.getDouble(Constants.MIN_CHARGE_FOR_FREE_DELIVERY);
		}
		order.setShopName(shop.getString(Constants.NAME));
		order.setShopPhone(shop.getString(CONTACT_NUMBER));
		order.setShopCostOfRunErrands(shopCostOfRunErrands);
		order.setShopMinChargeForFreeDelivery(shopMinChargeForFreeDelivery);
		order.setShopId(shop.getLong(ID));
		order.setAgentId(shop.getLong(Constants.AGENT_ID));
		return order;
	}

	public static OrderItem createOrderItem(MapWrapper<String, ?> goods) {
		OrderItem orderItem = new OrderItem();
		long goodsId = goods.getLong(ID);
		String goodsCode = goods.getString(CODE);
		String goodsName = goods.getString(NAME);
		double sellingPrice = goods.getDouble(SELLING_PRICE);
		orderItem.setSellingPrice(sellingPrice);
		orderItem.setGoodsCode(goodsCode);
		orderItem.setGoodsName(goodsName);
		orderItem.setGoodsId(goodsId);
		return orderItem;
	}
}
