/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.buyer.phone.rest;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.herod.buyer.phone.BuyerService;
import org.herod.framework.MapWrapper;
import org.herod.framework.rest.GZipRestTemplateBuilder;
import org.herod.framework.rest.RestServiceSupport;
import org.herod.framework.rest.URLBuilder;
import org.herod.framework.tools.GsonUtils;
import org.herod.order.common.model.Order;
import org.herod.order.common.model.Result;
import org.herod.order.common.model.SimpleResult;
import org.springframework.util.CollectionUtils;
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
public class RestBuyerService extends RestServiceSupport implements
		BuyerService {
	private RestTemplate restTemplate;
	private URLBuilder urlBuilder;

	public RestBuyerService(Context context) {
		super();
		this.restTemplate = new GZipRestTemplateBuilder().buildRestTemplate();
		this.urlBuilder = new RestUrlBuilder(context);
	}

	@Override
	public String getTransactionSerialNumber() {
		return getForObject("/herod/sn/transaction", String.class);
	}

	@Override
	public List<MapWrapper<String, Object>> findShopTypes() {
		String json = getForObject("/herod/order/shopTypes", String.class);
		return toMapWrapperList(json);
	}

	@Override
	public List<MapWrapper<String, Object>> findShopesByType(long typeId) {
		String json = getForObject("/herod/order/shopTypes/{typeId}/shops",
				String.class, typeId);
		return toMapWrapperList(json);
	}

	@Override
	public MapWrapper<String, Object> findShopById(long shopId) {
		String json = getForObject("/herod/order/shops/{shopId}", String.class,
				shopId);
		return toMapWrapper(json);
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

	@Override
	public List<MapWrapper<String, Object>> searchGoodses(String goodsName,
			int begin, int count) {
		String json = getForObject(
				"/herod/order/goodses?goodsName={goodsName}&begin={begin}&count={count}",
				String.class, goodsName, begin, count);
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

	private MapWrapper<String, Object> toMapWrapper(String json) {
		Type type = new TypeToken<Map<String, String>>() {
		}.getType();
		Map<String, String> map = new Gson().fromJson(json, type);
		return toMapWrapper(map);
	}

	private MapWrapper<String, Object> toMapWrapper(Map<String, String> map) {
		Map<String, Object> newMap = new HashMap<String, Object>();
		newMap.putAll(map);
		MapWrapper<String, Object> mapWrapper = new MapWrapper<String, Object>(
				newMap);
		return mapWrapper;
	}

	@Override
	public Result submitOrders(List<Order> orders) {
		return postForObject("/herod/order", SimpleResult.class,
				new Gson().toJson(orders));
	}

	public Map<String, Order> findOrders(Set<String> serialNumbers) {
		if (CollectionUtils.isEmpty(serialNumbers)) {
			return Collections.emptyMap();
		}
		StringBuilder url = new StringBuilder("/herod/order?");
		int i = 0;
		for (String sn : serialNumbers) {
			if (i > 0) {
				url.append("&");
			}
			url.append("serialNumber=").append(sn);
			i++;
		}
		String json = getForObject(url.toString(), String.class);
		Type type = new TypeToken<Map<String, Order>>() {
		}.getType();
		return GsonUtils.buildDeaultGson().fromJson(json, type);
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
