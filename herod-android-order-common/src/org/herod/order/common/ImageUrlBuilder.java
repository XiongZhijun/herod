/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.order.common;

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
		appendHttpServerUrl().appendRelativeUrl(relativeUrl);
		return getString();
	}

	@Override
	protected String getHost() {
		return OrderContext.getImageServerHost();
	}

	@Override
	protected int getPort() {
		return OrderContext.getImageServerPort();
	}

}
