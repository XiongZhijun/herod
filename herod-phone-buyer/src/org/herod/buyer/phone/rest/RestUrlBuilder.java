/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.buyer.phone.rest;

import static org.herod.framework.Constants.LATITUDE;
import static org.herod.framework.Constants.LONGITUDE;
import static org.herod.framework.Constants.REST;

import org.herod.framework.lbs.LocationManager;
import org.herod.framework.rest.URLBuilder;
import org.herod.framework.tools.HttpUrlBuilderSupport;
import org.herod.order.common.OrderContext;

import android.content.Context;

import com.baidu.location.BDLocation;

/**
 * 
 * 
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 * 
 */
public class RestUrlBuilder extends HttpUrlBuilderSupport implements URLBuilder {

	private Context context;

	public RestUrlBuilder(Context context) {
		super();
		this.context = context.getApplicationContext();
	}

	@Override
	public String build(String relativeUrl) {
		clean();
		appendHttpServerUrl().appendRelativeUrl(REST).appendRelativeUrl(
				relativeUrl);
		BDLocation latestLocation = LocationManager.getInstance(context)
				.getLatestBDLocation();
		if (latestLocation != null) {
			appendParams(LATITUDE, latestLocation.getLatitude());
			appendParams(LONGITUDE, latestLocation.getLongitude());
		}
		return getString();
	}

	@Override
	protected String getHost() {
		return OrderContext.getRestServerHost();
	}

	@Override
	protected int getPort() {
		return OrderContext.getRestServerPort();
	}

}
