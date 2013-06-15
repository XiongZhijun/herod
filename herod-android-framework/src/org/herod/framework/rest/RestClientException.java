/*
 * 香港摩比科技有限公司 版权所有
 *
 * www.mobi-inf.com
 */
package org.herod.framework.rest;

import org.springframework.http.HttpStatus;

public class RestClientException extends RestException {

	private static final long serialVersionUID = -6809849936051128772L;

	public RestClientException(HttpStatus statusCode, String statusText) {
		super("Response's statusCode '" + statusCode + "', statusText '"
				+ statusText + "'");
	}

}
