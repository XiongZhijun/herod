/*
 * 香港摩比科技有限公司 版权所有
 *
 * www.mobi-inf.com
 */
package org.herod.framework.rest;

import org.springframework.web.client.RestTemplate;

import android.content.Context;

/**
 * REST客户端辅助类，简化调用rest服务时的步骤。可以继承该类来实现{@link RestTemplate}以及{@link URLBuilder}
 * 的注入。
 * 
 * @author Xiong Zhijun
 * 
 */
public abstract class RestServiceSupport {

	public <T> T getForObject(String relativeUrl, Class<T> responseType,
			Object... urlVariables) {
		String url = getUrlBuilder().build(relativeUrl);
		return getRestTemplate().getForObject(url, responseType, urlVariables);
	}

	public <T> T getForObject(Context context, int relativeUrlResId,
			Class<T> responseType, Object... urlVariables) {
		String url = getUrlBuilder().build(context.getString(relativeUrlResId));
		return getRestTemplate().getForObject(url, responseType, urlVariables);
	}

	public <T> T postForObject(String relativeUrl, Class<T> responseType,
			Object request, Object... uriVariables) {
		String url = getUrlBuilder().build(relativeUrl);
		return getRestTemplate().postForObject(url, request, responseType,
				uriVariables);
	}

	public <T> T postForObject(Context context, int relativeUrlResId,
			Class<T> responseType, Object request, Object... uriVariables) {
		String url = getUrlBuilder().build(context.getString(relativeUrlResId));
		return getRestTemplate().postForObject(url, request, responseType,
				uriVariables);
	}

	public void postForObject(String relativeUrl, Object request,
			Object... uriVariables) {
		postForObject(relativeUrl, String.class, request, uriVariables);
	}

	public void postForObject(Context context, int relativeUrlResId,
			Object request, Object... uriVariables) {
		postForObject(context, relativeUrlResId, String.class, request,
				uriVariables);
	}

	public void putForObject(String relativeUrl, Object request,
			Object... uriVariables) {
		String url = getUrlBuilder().build(relativeUrl);
		getRestTemplate().put(url, request, uriVariables);
	}

	public void putForObject(Context context, int relativeUrlResId,
			Object request, Object... uriVariables) {
		String url = getUrlBuilder().build(context.getString(relativeUrlResId));
		getRestTemplate().put(url, request, uriVariables);
	}

	public void delete(String relativeUrl, Object... uriVariables) {
		String url = getUrlBuilder().build(relativeUrl);
		getRestTemplate().delete(url, uriVariables);
	}

	public void delete(Context context, int relativeUrlResId,
			Object... uriVariables) {
		String url = getUrlBuilder().build(context.getString(relativeUrlResId));
		getRestTemplate().delete(url, uriVariables);
	}

	public abstract URLBuilder getUrlBuilder();

	public abstract RestTemplate getRestTemplate();

}
