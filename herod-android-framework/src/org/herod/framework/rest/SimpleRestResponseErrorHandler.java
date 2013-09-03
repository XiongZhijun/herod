/*
 * 香港摩比科技有限公司 版权所有
 *
 * www.mobi-inf.com
 */
package org.herod.framework.rest;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;

import android.util.Log;

public class SimpleRestResponseErrorHandler extends DefaultResponseErrorHandler {

	private static final String TAG = SimpleRestResponseErrorHandler.class
			.getSimpleName();

	@Override
	public void handleError(ClientHttpResponse response) throws IOException {
		if (response == null) {
			throw new NoResponseException("response is null.");
		}
		HttpStatus statusCode;
		try {
			statusCode = response.getStatusCode();
		} catch (Exception e) {
			Log.w(TAG, "get response status code failed.", e);
			throw new NoResponseException("get response status code failed.");
		}
		if (statusCode == null) {
			throw new NoResponseException("response status code is null.");
		}
		if (statusCode.value() == 403) {
			throw new AuthenticationException();
		}
		switch (statusCode.series()) {
		case SERVER_ERROR:
			throw new RestServerException(statusCode, response.getStatusText());
		case CLIENT_ERROR:
		default:
			throw new RestClientException(statusCode, response.getStatusText());
		}

	}

	@Override
	public boolean hasError(ClientHttpResponse response) throws IOException {
		if (response == null) {
			return true;
		}
		try {
			return super.hasError(response);
		} catch (Exception e) {
			Log.w(TAG, e.getMessage(), e);
			return true;
		}
	}

	@Override
	protected boolean hasError(HttpStatus statusCode) {
		try {
			return super.hasError(statusCode);
		} catch (Exception e) {
			Log.w(TAG, e.getMessage(), e);
			return true;
		}
	}

}
