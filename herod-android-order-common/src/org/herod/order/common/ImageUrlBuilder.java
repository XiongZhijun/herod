/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.order.common;

import org.herod.framework.rest.URLBuilder;
import org.herod.framework.tools.HttpUrlBuilderSupport;
import static org.herod.order.common.Constants.*;

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
		// TODO 这个是不是要删掉
		appendHttpServerUrl().appendRelativeUrl(relativeUrl)
				.appendParams(USER_NAME, "root")
				.appendParams(PASSWORD, "123456");
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
