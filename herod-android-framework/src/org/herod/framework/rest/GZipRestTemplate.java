/*
 * 香港摩比科技有限公司 版权所有
 *
 * www.mobi-inf.com
 */
package org.herod.framework.rest;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;

import org.springframework.http.ContentCodingType;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * 支持GZip的{@link RestTemplate}，跟普通RestTemplate的区别就在于： <br>
 * {@link GZipRestTemplate}可以对提交的所有REST请求的body进行gzip压缩，而普通的RestTemplate则不行。
 * <p>
 * <b>注意：</b>由于tomcat默认不支持body编码为gzip的HTTP请求。因此当使用该{@link GZipRestTemplate}
 * 时，需要在tomcat端加上对应的GZip的Filter来进行Gzip解压缩。
 * 
 * @author Xiong Zhijun
 * 
 */
public class GZipRestTemplate extends RestTemplate {
	public GZipRestTemplate() {
		super();
	}

	public GZipRestTemplate(boolean includeDefaultConverters,
			ClientHttpRequestFactory requestFactory) {
		super(includeDefaultConverters, requestFactory);
	}

	public GZipRestTemplate(boolean includeDefaultConverters) {
		super(includeDefaultConverters);
	}

	public GZipRestTemplate(ClientHttpRequestFactory requestFactory) {
		super(requestFactory);
	}

	/**
	 * 加上gzip的ContentEncoding，表示会对该请求的body进行gzip压缩。
	 */
	@Override
	protected ClientHttpRequest createRequest(URI url, HttpMethod method)
			throws IOException {
		ClientHttpRequest request = super.createRequest(url, method);
		HttpHeaders headers = request.getHeaders();
		if (headers != null) {
			headers.setContentEncoding(Arrays.asList(ContentCodingType.GZIP));
		}
		return request;
	}
}
