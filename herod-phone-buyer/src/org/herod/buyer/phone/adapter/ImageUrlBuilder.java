/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.buyer.phone.adapter;

import org.herod.framework.rest.URLBuilder;
import org.herod.framework.tools.HttpUrlBuilderSupport;

/**
 * 
 * 
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 * 
 */
public class ImageUrlBuilder extends HttpUrlBuilderSupport implements
		URLBuilder {

	@Override
	public String build(String relativeUrl) {
		clean();
		appendHttpServerUrl().appendRelativeUrl(relativeUrl)
				.appendParams("username", "root")
				.appendParams("password", "123456");
		return getString();
	}

	@Override
	protected String getHost() {
		return "192.168.100.102";
	}

	@Override
	protected int getPort() {
		return 9191;
	}

}
