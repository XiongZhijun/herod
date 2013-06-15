/*
 * 香港摩比科技有限公司 版权所有
 *
 * www.mobi-inf.com
 */
package org.herod.framework.rest;

import org.springframework.http.HttpStatus;

public class RestServerException extends RestException {

	public RestServerException(HttpStatus statusCode, String statusText) {
		super("Response's statusCode '" + statusCode + "', statusText '"
				+ statusText + "'");
	}

	private static final long serialVersionUID = -3512170110800078285L;

}
