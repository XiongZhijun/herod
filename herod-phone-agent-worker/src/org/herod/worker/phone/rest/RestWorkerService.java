/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.worker.phone.rest;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.herod.framework.MapWrapper;
import org.herod.framework.rest.GZipRestTemplateBuilder;
import org.herod.framework.rest.RestServiceSupport;
import org.herod.framework.rest.URLBuilder;
import org.herod.framework.tools.GsonUtils;
import org.herod.order.common.model.Order;
import org.herod.order.common.model.Result;
import org.herod.order.common.model.SimpleResult;
import org.herod.worker.phone.WorkerService;
import org.herod.worker.phone.model.OrderUpdateInfo;
import org.herod.worker.phone.model.Token;
import org.springframework.web.client.RestTemplate;

import android.content.Context;

import com.google.gson.Gson;
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

	public RestWorkerService(Context context) {
		super();
		this.restTemplate = new GZipRestTemplateBuilder().buildRestTemplate();
		this.urlBuilder = new RestUrlBuilder(context);
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
		return postForObject("/herod/agentworker/orders/{serialNumber}/accept",
				SimpleResult.class, null, serialNumber);
	}

	@Override
	public Result updateOrder(OrderUpdateInfo updateInfo) {
		return postForObject("/herod/agentworker/orders/{serialNumber}/update",
				SimpleResult.class, updateInfo,
				updateInfo.getOrderSerialNumber());
	}

	@Override
	public Result rejectOrder(String serialNumber, String reason) {
		return postForObject(
				"/herod/agentworker/orders/{serialNumber}/reject?reason={reason}",
				SimpleResult.class, null, serialNumber, reason);
	}

	@Override
	public Result cancelOrder(String serialNumber, String reason) {
		return postForObject(
				"/herod/agentworker/orders/{serialNumber}/cancel?reason={reason}",
				SimpleResult.class, null, serialNumber, reason);
	}

	@Override
	public Result completeOrder(String serialNumber) {
		return postForObject(
				"/herod/agentworker/orders/{serialNumber}/complete",
				SimpleResult.class, null, serialNumber);
	}

	@Override
	public URLBuilder getUrlBuilder() {
		return urlBuilder;
	}

	@Override
	public RestTemplate getRestTemplate() {
		return restTemplate;
	}

	@Override
	public List<MapWrapper<String, Object>> findGoodsTypesByShop(long shopId) {
		String json = getForObject("/herod/order/shops/{shopId}/goodsTypes",
				String.class, shopId);
		return toMapWrapperList(json);
	}

	@Override
	public List<MapWrapper<String, Object>> findGoodsesByType(long goodsTypeId,
			int begin, int count) {
		String json = getForObject(
				"/herod/order/goodsTypes/{goodsTypeId}/goodses?begin={begin}&count={count}",
				String.class, goodsTypeId, begin, count);
		return toMapWrapperList(json);
	}

	private List<MapWrapper<String, Object>> toMapWrapperList(String json) {
		Type type = new TypeToken<List<Map<String, String>>>() {
		}.getType();
		List<Map<String, String>> mapList = new Gson().fromJson(json, type);
		List<MapWrapper<String, Object>> results = new ArrayList<MapWrapper<String, Object>>();
		for (Map<String, String> map : mapList) {
			results.add(toMapWrapper(map));
		}
		return results;
	}

	private MapWrapper<String, Object> toMapWrapper(Map<String, String> map) {
		Map<String, Object> newMap = new HashMap<String, Object>();
		newMap.putAll(map);
		MapWrapper<String, Object> mapWrapper = new MapWrapper<String, Object>(
				newMap);
		return mapWrapper;
	}
}
