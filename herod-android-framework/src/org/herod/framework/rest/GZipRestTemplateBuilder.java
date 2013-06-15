/*
 * 香港摩比科技有限公司 版权所有
 *
 * www.mobi-inf.com
 */
package org.herod.framework.rest;

import org.springframework.web.client.RestTemplate;

/**
 * {@link GZipRestTemplate} 建造器，用来创建{@link GZipRestTemplate}对象。
 * 
 * @author Xiong Zhijun
 * 
 */
public class GZipRestTemplateBuilder extends RestTemplateBuilder {

	@Override
	protected RestTemplate createRestTemplate() {
		return new GZipRestTemplate();
	}

}
