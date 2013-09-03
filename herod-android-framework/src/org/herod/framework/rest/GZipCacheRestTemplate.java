/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved.
 */
package org.herod.framework.rest;

import static org.herod.framework.Constants.AMPERSAND;
import static org.herod.framework.Constants.COLON;
import static org.herod.framework.Constants.FORWARD_SLASH;
import static org.herod.framework.Constants.PERIOD;
import static org.herod.framework.Constants.QUESTION_MARK;
import static org.herod.framework.Constants.UNDERLINE;

import java.io.File;
import java.io.FileFilter;
import java.net.URI;
import java.util.Map;
import java.util.regex.Pattern;

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
	private static final String TIMESTAMP_REPLACED_STRING = "timestamp=\\\\d*";
	private static final String TIMESTAMP_PATTERN = "timestamp=\\d*";
	private static final String REST_CACHE = "RestCache";
	private static Application application;
	private NeedCacheMatcher needCacheMatcher = ALL_MATCHER;

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
		if (!String.class.equals(responseType) && !needCacheMatcher.matche(url)) {
			return null;
		}
		String fileName = getFileName(url);
		String text = FileUtils.readFromFile(application, REST_CACHE, fileName);
		return (T) text;
	}

	protected <T> void saveToCache(URI url, Class<T> responseType, T result) {
		if (!needCacheMatcher.matche(url)) {
			return;
		}
		String fileName = getFileName(url);
		removeHistoryFile(fileName);
		if (String.class.equals(responseType)) {
			FileUtils.saveToFile(application, REST_CACHE, fileName,
					result.toString());
		}
	}

	private String getFileName(URI url) {
		String fileName = url.toString().replace(FORWARD_SLASH, UNDERLINE)
				.replace(AMPERSAND, UNDERLINE).replace(COLON, UNDERLINE)
				.replace(PERIOD, UNDERLINE).replace(QUESTION_MARK, UNDERLINE);
		return fileName;
	}

	protected void removeHistoryFile(String fileName) {
		final Pattern pattern = Pattern.compile(fileName.replaceAll(
				TIMESTAMP_PATTERN, TIMESTAMP_REPLACED_STRING));
		File fileDir = application.getExternalFilesDir(REST_CACHE);
		File[] files = fileDir.listFiles(new FileFilter() {
			public boolean accept(File file) {
				String fileName = file.getName();
				return pattern.matcher(fileName).matches();
			}
		});
		for (File file : files) {
			file.delete();
		}
	}

	public void setNeedCacheMatcher(NeedCacheMatcher needCacheMatcher) {
		if (needCacheMatcher != null)
			this.needCacheMatcher = needCacheMatcher;
	}

	public static void setApplication(Application application) {
		GZipCacheRestTemplate.application = application;
	}

	public static interface NeedCacheMatcher {
		boolean matche(URI url);
	}

	private static final NeedCacheMatcher ALL_MATCHER = new NeedCacheMatcher() {
		public boolean matche(URI url) {
			return true;
		}

	};
}
