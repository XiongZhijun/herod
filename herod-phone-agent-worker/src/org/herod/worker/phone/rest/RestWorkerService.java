/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.worker.phone.rest;

import org.herod.framework.rest.GZipRestTemplateBuilder;
import org.herod.framework.rest.RestServiceSupport;
import org.herod.framework.rest.URLBuilder;
import org.herod.framework.utils.DeviceUtils;
import org.herod.worker.phone.WorkerService;
import org.herod.worker.phone.model.Token;
import org.springframework.web.client.RestTemplate;

import android.content.Context;

/**
 * 
 * 
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 * 
 */
public class RestWorkerService extends RestServiceSupport implements
		WorkerService {
	private RestTemplate restTemplate;
	private URLBuilder urlBuilder;
	private Context applicationContext;

	public RestWorkerService(Context context) {
		super();
		this.restTemplate = new GZipRestTemplateBuilder().buildRestTemplate();
		this.urlBuilder = new RestUrlBuilder(context);
		this.applicationContext = context.getApplicationContext();
	}

	@Override
	public Token login(String phone, String password) {
		return postForObject(
				"/herod/agentworker/login?workerPhone={workerPhone}&workerPassword={workerPassword}&imei={imei}",
				Token.class, null, phone, password,
				DeviceUtils.getImei(applicationContext));
	}

	@Override
	public URLBuilder getUrlBuilder() {
		return urlBuilder;
	}

	@Override
	public RestTemplate getRestTemplate() {
		return restTemplate;
	}

}
