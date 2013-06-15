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
			.getName();

	@Override
	public void handleError(ClientHttpResponse response) throws IOException {
		try {
			if (response == null) {
				throw new NoResponseException("response is null.");
			}
			HttpStatus statusCode = response.getStatusCode();
			if (statusCode == null) {
				throw new NoResponseException("response status code is null.");
			}
			switch (statusCode.series()) {
			case SERVER_ERROR:
				throw new RestServerException(statusCode,
						response.getStatusText());
			case CLIENT_ERROR:
			default:
				throw new RestClientException(statusCode,
						response.getStatusText());
			}
		} catch (RuntimeException e) {
			throw new NoResponseException("服务器没有返回！", e);
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
