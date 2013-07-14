/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.buyer.phone;

import org.herod.buyer.phone.mocks.BuyerServiceMock;
import org.herod.buyer.phone.rest.RestBuyerService;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

/**
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 * 
 */
public class BuyerContext {
	private static final String ORDER_INDEX = "OrderIndex";
	private static final String TERMINAL_ID = "TerminalId";
	private static String restServerHost;
	private static int restServerPort;
	private static String imageServerHost;
	private static int imageServerPort;
	private static BuyerService buyerService = new BuyerServiceMock();
	private static ShopService shopService;

	public static void init(Context context) {
		BuyerServiceProxy buyerServiceProxy = new BuyerServiceProxy(
				new RestBuyerService(context));
		buyerService = buyerServiceProxy;
		shopService = buyerServiceProxy;
		restServerHost = context.getString(R.string.RestServerHost);
		restServerPort = Integer.parseInt(context
				.getString(R.string.RestServerPort));
		imageServerHost = context.getString(R.string.ImageServerHost);
		imageServerPort = Integer.parseInt(context
				.getString(R.string.ImageServerPort));
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

	public static long getTerminalId(Context context) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		return preferences.getLong(TERMINAL_ID, 0);
	}

	public static void setTerminalId(Context context, long terminalId) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		Editor editor = preferences.edit();
		editor.putLong(TERMINAL_ID, terminalId);
		editor.commit();
	}

	public static long increaseOrderIndex(Context context) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		long index = preferences.getLong(ORDER_INDEX, 0);
		index++;
		Editor editor = preferences.edit();
		editor.putLong(ORDER_INDEX, index);
		editor.commit();
		return index;
	}

	public static String getRestServerHost() {
		return restServerHost;
	}

	public static int getRestServerPort() {
		return restServerPort;
	}

	public static String getImageServerHost() {
		return imageServerHost;
	}

	public static int getImageServerPort() {
		return imageServerPort;
	}
}
