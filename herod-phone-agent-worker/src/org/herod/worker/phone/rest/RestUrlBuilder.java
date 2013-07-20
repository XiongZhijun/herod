/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.worker.phone.rest;

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
	private static final String REST = "rest";
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
				.appendParams("username", WorkerContext.getRestUserName())
				.appendParams("password", WorkerContext.getRestPassword())
				.appendParams("token", WorkerContext.getLoginTokenString())
				.appendParams("imei", imei);
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
