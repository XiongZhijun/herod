/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.framework.lbs;

import org.herod.framework.lbs.LocationManager.LocationClientOptionBuilder;
import org.herod.framework.lbs.LocationManager.LocationPlan;

import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClientOption;

/**
 * 定义了一种简单方式的定位计划，通过不同的时间间隔定位一定的次数来控制定位的频率。
 * 
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 * 
 */
public class SimpleLocationPlan implements LocationPlan, BDLocationListener,
		LocationClientOptionBuilder {
	public static final int[] DEFAULT_SCAN_SPANS = new int[] { 1000, 60 * 1000,
			2 * 60 * 1000 };
	public static final int[] DEFAULT_LOCATION_COUNTS = new int[] { 10, 10, 30 };
	/** 定位时间间隔，配合{@link #locationCounts}使用 */
	private int[] scanSpans = DEFAULT_SCAN_SPANS;
	/**
	 * 定位次数，配合{@link #scanSpans}使用，跟scanSpans为一一对应关系。 就默认设置
	 * {@link #DEFAULT_SCAN_SPANS}和{@link #DEFAULT_LOCATION_COUNTS}来说就是：
	 * <p>
	 * <ul>
	 * <li>
	 * 每隔1秒钟读取一次位置，一共10次；
	 * <li>
	 * 然后每隔1分钟读取一次，一共10次；
	 * <li>
	 * 最后每隔2分钟读取一次，一共30次。
	 */
	private int[] locationCounts = DEFAULT_LOCATION_COUNTS;
	/**
	 * 就是{@link #scanSpans}和{@link #locationCounts}的索引，控制每一次定位计划的间隔和次数的。
	 */
	private int level = 0;
	/** 控制每一个时间间隔里面的定位次数的 */
	private int locationCount = 0;
	private LocationManager locationManager;
	private OnLocationSuccessListener onLocationSuccessListener;

	public SimpleLocationPlan(OnLocationSuccessListener listener) {
		this(DEFAULT_SCAN_SPANS, DEFAULT_LOCATION_COUNTS, listener);
	}

	public SimpleLocationPlan(int[] scanSpans, int[] locationCounts,
			OnLocationSuccessListener listener) {
		super();
		this.scanSpans = scanSpans;
		this.locationCounts = locationCounts;
		this.onLocationSuccessListener = listener != null ? listener
				: DEFAULT_LOCATION_SUCCESS_LISTENER;
	}

	@Override
	public void execute(LocationManager locationManager) {
		this.locationManager = locationManager;
		locationManager.setLocationClientOptionBuilder(this);
		locationManager.registerLocationListener(this);
		level = 0;
		locationCount = 0;
		locationManager.start();
	}

	@Override
	public void onReceiveLocation(BDLocation location) {
		if (LocationManager.isLocactionFailed(location)) {
			Log.d("Location", "定位失败！");
			return;
		}
		onLocationSuccessListener.onLocationSuccess(location);
		locationCount++;
		if (locationCount > locationCounts[level]) {
			level++;
			if (level >= scanSpans.length) {
				locationManager.stop();
				return;
			}
			locationCount = 0;
			locationManager.restart();
		}
	}

	@Override
	public void onReceivePoi(BDLocation location) {

	}

	@Override
	public LocationClientOption build() {
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);
		option.setAddrType("all");// 返回的定位结果包含地址信息
		option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度,默认值gcj02
		option.setScanSpan(scanSpans[level]);// 设置发起定位请求的间隔时间为5000ms
		option.disableCache(true);// 禁止启用缓存定位
		option.setPoiNumber(5); // 最多返回POI个数
		option.setPoiDistance(1000); // poi查询距离
		option.setPoiExtraInfo(true); // 是否需要POI的电话和地址等详细信息
		option.setPriority(LocationClientOption.GpsFirst);
		return option;
	}

	public static interface OnLocationSuccessListener {
		void onLocationSuccess(BDLocation location);
	}

	public static final OnLocationSuccessListener DEFAULT_LOCATION_SUCCESS_LISTENER = new OnLocationSuccessListener() {
		public void onLocationSuccess(BDLocation location) {
		}
	};

}
