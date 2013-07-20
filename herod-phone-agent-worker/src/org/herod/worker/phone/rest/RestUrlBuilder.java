/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.worker.phone.rest;

import org.herod.framework.rest.URLBuilder;
import org.herod.framework.tools.HttpUrlBuilderSupport;
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

	public RestUrlBuilder(Context context) {
		super();
	}

	@Override
	public String build(String relativeUrl) {
		clean();
		appendHttpServerUrl().appendRelativeUrl(REST)
				.appendRelativeUrl(relativeUrl)
				.appendParams("username", getRestUserName())
				.appendParams("password", getRestPassword());
		return getString();
	}

	protected String getRestPassword() {
		return WorkerContext.getRestPassword();
	}

	protected String getRestUserName() {
		return WorkerContext.getRestUserName();
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
