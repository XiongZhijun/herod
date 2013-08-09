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
 * 
 * 
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 * 
 */
public class SimpleLocationPlan implements LocationPlan, BDLocationListener,
		LocationClientOptionBuilder {
	private int[] scanSpans = new int[] { 1000, 60 * 1000, 2 * 60 * 1000 };
	private int[] locationCounts = new int[] { 10, 10, 30 };
	private int level = 0;
	private int locationCount = 0;
	private LocationManager locationManager;
	private OnLocationSuccessListener onLocationSuccessListener;

	public SimpleLocationPlan(OnLocationSuccessListener listener) {
		super();
		this.onLocationSuccessListener = listener;
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
		// TODO Auto-generated method stub

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

}
