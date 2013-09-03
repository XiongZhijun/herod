/*
 * 香港摩比科技有限公司 版权所有
 *
 * www.mobi-inf.com
 */
package org.herod.framework.rest;

import org.codehaus.jackson.Version;
import org.codehaus.jackson.map.DeserializationConfig.Feature;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.module.SimpleModule;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

/**
 * {@link RestTemplate} 建造器，用来创建{@link RestTemplate}对象。
 * 
 * @author Xiong Zhijun
 * 
 */
public class RestTemplateBuilder {

	private static final String ALPHA = "alpha";
	private static final String SIMPLE_MODULE_NAME = "Module1";

	public RestTemplate buildRestTemplate() {
		RestTemplate restTemplate = createRestTemplate();
		restTemplate.setErrorHandler(buildResponseErrorHandler());
		restTemplate.getMessageConverters().add(buildStringConverter());
		restTemplate.getMessageConverters().add(buildJsonConverter());
		return restTemplate;
	}

	protected RestTemplate createRestTemplate() {
		return new RestTemplate();
	}

	protected ResponseErrorHandler buildResponseErrorHandler() {
		return new SimpleRestResponseErrorHandler();
	}

	protected HttpMessageConverter<String> buildStringConverter() {
		return new StringHttpMessageConverter();
	}

	protected HttpMessageConverter<?> buildJsonConverter() {
		MappingJacksonHttpMessageConverter converter = new MappingJacksonHttpMessageConverter();
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.getDeserializationConfig().set(
				Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		SimpleModule module = new SimpleModule(SIMPLE_MODULE_NAME, new Version(
				0, 1, 0, ALPHA));
		objectMapper.registerModule(module);
		converter.setObjectMapper(objectMapper);
		return converter;
	}
}
