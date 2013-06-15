/*
 * 香港摩比科技有限公司 版权所有
 *
 * www.mobi-inf.com
 */
package org.herod.framework.tools;

import org.herod.framework.utils.StringUtils;

/**
 * @author Xiong Zhijun
 * 
 */
public abstract class HttpUrlBuilderSupport extends StringBuilderSupport {
	public static final String STORE = "store";
	public static final String PASSWORD = "password";
	public static final String USER_NAME = "username";
	public static final String HTTP_PREFIX = "http://";
	public static final String QUESTION_MARK = "?";
	public static final String AND = "&";
	public static final String EQUAL_SIGN = "=";
	public static final String FORWARD_SLASH = "/";
	public static final String COLON = ":";

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
			append(AND);
		}
		if (StringUtils.isNotBlank(key) && value != null) {
			append(key).append(EQUAL_SIGN).append(value.toString());
		}
		return this;
	}

}
