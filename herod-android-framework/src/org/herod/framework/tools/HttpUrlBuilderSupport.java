/*
 * 香港摩比科技有限公司 版权所有
 *
 * www.mobi-inf.com
 */
package org.herod.framework.tools;

import static org.herod.framework.Constants.AMPERSAND;
import static org.herod.framework.Constants.COLON;
import static org.herod.framework.Constants.EQUAL_SIGN;
import static org.herod.framework.Constants.FORWARD_SLASH;
import static org.herod.framework.Constants.HTTP_PREFIX;
import static org.herod.framework.Constants.QUESTION_MARK;

import org.herod.framework.utils.StringUtils;

/**
 * @author Xiong Zhijun
 * 
 */
public abstract class HttpUrlBuilderSupport extends StringBuilderSupport {

	public HttpUrlBuilderSupport appendHttpServerUrl() {
		append(HTTP_PREFIX).append(getHost()).append(COLON).append(getPort());
		return this;
	}

	protected abstract String getHost();

	protected abstract int getPort();

	public HttpUrlBuilderSupport appendRelativeUrl(String url) {
		if (url == null) {
			return this;
		}
		if (!url.startsWith(FORWARD_SLASH)) {
			append(FORWARD_SLASH);
		}
		append(url);
		return this;
	}

	public HttpUrlBuilderSupport appendParams(String key, Object value) {
		if (getString().indexOf(QUESTION_MARK) < 0) {
			append(QUESTION_MARK);
		} else {
			append(AMPERSAND);
		}
		if (StringUtils.isNotBlank(key) && value != null) {
			append(key).append(EQUAL_SIGN).append(value.toString());
		}
		return this;
	}

}
