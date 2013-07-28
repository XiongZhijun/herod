/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.worker.phone.rest;

import java.util.Date;
import java.util.List;

import org.herod.framework.rest.GZipRestTemplateBuilder;
import org.herod.framework.rest.RestServiceSupport;
import org.herod.framework.rest.URLBuilder;
import org.herod.framework.tools.DateTypeAdapter;
import org.herod.framework.tools.GsonUtils;
import org.herod.worker.phone.Result;
import org.herod.worker.phone.WorkerService;
import org.herod.worker.phone.model.Order;
import org.herod.worker.phone.model.OrderUpdateInfo;
import org.herod.worker.phone.model.Token;
import org.springframework.web.client.RestTemplate;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

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
				"/herod/agentworker/login?workerPhone={workerPhone}&workerPassword={workerPassword}",
				Token.class, null, phone, password);
	}

	@Override
	public List<Order> findWaitAcceptOrders() {
		return getOrders("/herod/agentworker/orders/waitaccept");
	}

	@Override
	public List<Order> findWaitCompleteOrders() {
		return getOrders("/herod/agentworker/orders/waitcomplete");
	}

	@Override
	public List<Order> findCompletedOrders() {
		return getOrders("/herod/agentworker/orders/completed");
	}

	@Override
	public List<Order> findCanceledOrders() {
		return getOrders("/herod/agentworker/orders/canceled");
	}

	private List<Order> getOrders(String url) {
		String json = getForObject(url, String.class);
		return toOrderList(json);
	}

	private List<Order> toOrderList(String json) {
		List<Order> orders = GsonUtils.buildDeaultGson().fromJson(json,
				new TypeToken<List<Order>>() {
				}.getType());
		return orders;
	}

	@Override
	public Result acceptOrder(String serialNumber) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Result updateOrder(OrderUpdateInfo updateInfo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Result rejectOrder(String serialNumber, String reason) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Result cancelOrder(String serialNumber, String reason) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Result completeOrder(String serialNumber) {
		// TODO Auto-generated method stub
		return null;
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
