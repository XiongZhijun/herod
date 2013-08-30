/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved.
 */
package org.herod.framework.rest;

import org.springframework.web.client.RestTemplate;

/**
 * 
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 */
public class GZipCacheRestTemplateBuilder extends GZipRestTemplateBuilder {

	@Override
	protected RestTemplate createRestTemplate() {
		return new GZipCacheRestTemplate();
	}
}
