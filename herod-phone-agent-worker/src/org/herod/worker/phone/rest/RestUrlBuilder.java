/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.worker.phone.rest;

import static org.herod.framework.Constants.REST;
import static org.herod.worker.phone.Constants.IMEI;
import static org.herod.worker.phone.Constants.TOKEN;

import org.herod.framework.rest.URLBuilder;
import org.herod.framework.tools.HttpUrlBuilderSupport;
import org.herod.framework.utils.DeviceUtils;
import org.herod.worker.phone.WorkerContext;

import android.content.Context;

/**
 * 
 * 
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 * 
 */
public class RestUrlBuilder extends HttpUrlBuilderSupport implements URLBuilder {
	private String imei;

	public RestUrlBuilder(Context context) {
		super();
		imei = DeviceUtils.getImei(context);
	}

	@Override
	public String build(String relativeUrl) {
		clean();
		appendHttpServerUrl().appendRelativeUrl(REST)
				.appendRelativeUrl(relativeUrl)
				.appendParams(TOKEN, WorkerContext.getLoginTokenString())
				.appendParams(IMEI, imei);
		return getString();
	}

	@Override
	protected String getHost() {
		return WorkerContext.getRestServerHost();
	}

	@Override
	protected int getPort() {
		return WorkerContext.getRestServerPort();
	}

}
