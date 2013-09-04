/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.buyer.phone.rest;

import java.lang.reflect.Type;
import java.net.URI;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.herod.buyer.phone.BuyerService;
import org.herod.framework.MapWrapper;
import org.herod.framework.rest.GZipCacheRestTemplate;
import org.herod.framework.rest.GZipCacheRestTemplate.DefaultFileNameBuilder;
import org.herod.framework.rest.GZipCacheRestTemplate.NeedCacheMatcher;
import org.herod.framework.rest.GZipCacheRestTemplateBuilder;
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
		BuyerService, NeedCacheMatcher {
	private static final String[] LOCATION_CORRELATION_FILES = new String[] { "/herod/order/shopTypes" };
	private static final String[] NOT_CACHE_URLS = new String[] {
			"/herod/sn/transaction", "/herod/order?", "/herod/order/goodses?",
			"/herod/order/shopTypes/" };
	private GZipCacheRestTemplate restTemplate;
	private URLBuilder urlBuilder;

	public RestBuyerService(Context context) {
		super();
		this.restTemplate = (GZipCacheRestTemplate) new GZipCacheRestTemplateBuilder()
				.buildRestTemplate();
		this.restTemplate.setNeedCacheMatcher(this);
		this.restTemplate.setFileNameBuilder(new BuyerFileNameBuilder());
		this.urlBuilder = new RestUrlBuilder(context);
	}

	@Override
	public String getTransactionSerialNumber() {
		return getForObject("/herod/sn/transaction", String.class);
	}

	@Override
	public List<MapWrapper<String, Object>> findShopTypes() {
		String json = getForObject(
				"/herod/order/shopTypes?timestamp={timestamp}", String.class,
				buildTimestamp());
		return toMapWrapperList(json);
	}

	private long buildTimestamp() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		return calendar.getTime().getTime();
	}

	@Override
	public List<MapWrapper<String, Object>> findShopesByType(long typeId) {
		String json = getForObject("/herod/order/shopTypes/{typeId}/shops",
				String.class, typeId);
		return toMapWrapperList(json);
	}

	@Override
	public MapWrapper<String, Object> findShopById(long shopId, long timestamp) {
		String json = getForObject(
				"/herod/order/shops/{shopId}?timestamp={timestamp}",
				String.class, shopId, timestamp);
		return toMapWrapper(json);
	}

	@Override
	public List<MapWrapper<String, Object>> findGoodsTypesByShop(long shopId,
			long timestamp) {
		String json = getForObject(
				"/herod/order/shops/{shopId}/goodsTypes?timestamp={timestamp}",
				String.class, shopId, timestamp);
		return toMapWrapperList(json);
	}

	@Override
	public List<MapWrapper<String, Object>> findGoodsesByType(long goodsTypeId,
			int begin, int count, long timestamp) {
		String json = getForObject(
				"/herod/order/goodsTypes/{goodsTypeId}/goodses?begin={begin}&count={count}&timestamp={timestamp}",
				String.class, goodsTypeId, begin, count, timestamp);
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

	@Override
	public boolean matche(URI url) {
		String urlStr = url.toString();
		for (String notCacheURL : NOT_CACHE_URLS) {
			if (urlStr.indexOf(notCacheURL) >= 0) {
				return false;
			}
		}
		return true;
	}

	private class BuyerFileNameBuilder extends DefaultFileNameBuilder {

		@Override
		public String build(URI url) {
			String fileName = super.build(url);
			String urlStr = url.toString();
			for (String locationCorrelationFile : LOCATION_CORRELATION_FILES) {
				if (urlStr.indexOf(locationCorrelationFile) >= 0) {
					return fileName;
				}
			}
			return fileName.replaceAll("latitude=\\d*_?\\d*", "").replaceAll(
					"longitude=\\d*_?\\d*", "");
		}

	}

}
