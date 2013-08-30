/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved.
 */
package org.herod.framework.rest;

import java.net.URI;
import java.util.Map;

import org.herod.framework.utils.FileUtils;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.web.client.RestClientException;
import org.springframework.web.util.UriTemplate;

import android.app.Application;

/**
 * 
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 */
public class GZipCacheRestTemplate extends GZipRestTemplate {
	private static final String REST_CACHE = "RestCache";
	private static Application application;

	public GZipCacheRestTemplate() {
		super();
	}

	public GZipCacheRestTemplate(boolean includeDefaultConverters,
			ClientHttpRequestFactory requestFactory) {
		super(includeDefaultConverters, requestFactory);
	}

	public GZipCacheRestTemplate(boolean includeDefaultConverters) {
		super(includeDefaultConverters);
	}

	public GZipCacheRestTemplate(ClientHttpRequestFactory requestFactory) {
		super(requestFactory);
	}

	@Override
	public <T> T getForObject(String url, Class<T> responseType,
			Map<String, ?> urlVariables) throws RestClientException {
		URI expanded = buildURI(url, urlVariables);
		T result = getFromCache(expanded, responseType);
		if (result != null) {
			return result;
		}
		result = super.getForObject(url, responseType, urlVariables);
		saveToCache(expanded, responseType, result);
		return result;
	}

	@Override
	public <T> T getForObject(String url, Class<T> responseType,
			Object... urlVariables) throws RestClientException {
		URI expanded = buildURI(url, urlVariables);
		T result = getFromCache(expanded, responseType);
		if (result != null) {
			return result;
		}
		result = super.getForObject(url, responseType, urlVariables);
		saveToCache(expanded, responseType, result);
		return result;
	}

	@Override
	public <T> T getForObject(URI url, Class<T> responseType)
			throws RestClientException {
		T result = getFromCache(url, responseType);
		if (result != null) {
			return result;
		}
		result = super.getForObject(url, responseType);
		saveToCache(url, responseType, result);
		return result;
	}

	private URI buildURI(String url, Object[] urlVariables) {
		UriTemplate uriTemplate = new UriTemplate(url);
		URI expanded = uriTemplate.expand(urlVariables);
		return expanded;
	}

	private URI buildURI(String url, Map<String, ?> urlVariables) {
		UriTemplate uriTemplate = new UriTemplate(url);
		URI expanded = uriTemplate.expand(urlVariables);
		return expanded;
	}

	@SuppressWarnings("unchecked")
	protected <T> T getFromCache(URI url, Class<T> responseType) {
		if (!String.class.equals(responseType)) {
			return null;
		}
		String fileName = url.toString().replace("/", "_").replace("&", "_")
				.replace(":", "_").replace(".", "_").replace("?", "_");
		String text = FileUtils.readFromFile(application, REST_CACHE, fileName);
		return (T) text;
	}

	protected <T> void saveToCache(URI url, Class<T> responseType, T result) {
		String fileName = url.toString().replace("/", "_").replace("&", "_")
				.replace(":", "_").replace(".", "_").replace("?", "_");
		if (String.class.equals(responseType)) {
			FileUtils.saveToFile(application, REST_CACHE, fileName,
					result.toString());
		}
	}

	public static void setApplication(Application application) {
		GZipCacheRestTemplate.application = application;
	}

}
