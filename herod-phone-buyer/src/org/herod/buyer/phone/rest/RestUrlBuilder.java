/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.buyer.phone.rest;

import org.herod.buyer.phone.BuyerContext;
import org.herod.buyer.phone.lbs.LocationManager;
import org.herod.framework.rest.URLBuilder;
import org.herod.framework.tools.HttpUrlBuilderSupport;

import com.baidu.location.BDLocation;

import android.content.Context;

/**
 * 
 * 
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 * 
 */
public class RestUrlBuilder extends HttpUrlBuilderSupport implements URLBuilder {
	private static final String REST = "rest";
	private Context context;

	public RestUrlBuilder(Context context) {
		super();
		this.context = context.getApplicationContext();
	}

	@Override
	public String build(String relativeUrl) {
		clean();
		appendHttpServerUrl().appendRelativeUrl(REST)
				.appendRelativeUrl(relativeUrl)
				.appendParams("username", getRestUserName())
				.appendParams("password", getRestPassword());
		BDLocation latestLocation = LocationManager.getInstance(context)
				.getLatestBDLocation();
		if (latestLocation != null) {
			appendParams("latitude", latestLocation.getLatitude());
			appendParams("longitude", latestLocation.getLongitude());
		}
		return getString();
	}

	private String getRestPassword() {
		return BuyerContext.getRestPassword();
	}

	private String getRestUserName() {
		return BuyerContext.getRestUserName();
	}

	@Override
	protected String getHost() {
		return BuyerContext.getRestServerHost();
	}

	@Override
	protected int getPort() {
		return BuyerContext.getRestServerPort();
	}

}
