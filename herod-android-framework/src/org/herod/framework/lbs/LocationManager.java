/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.framework.lbs;

import org.herod.framework.lbs.SimpleLocationPlan.OnLocationSuccessListener;

import android.content.Context;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

/**
 * 管理定位的工具类，通过该类的一些方法和实现一些接口来实现各种灵活的定位方式。
 * 
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 * 
 */
public class LocationManager implements BDLocationListener {
	private static LocationManager instance = null;
	private LocationClient locationClient;
	private LocationClientOptionBuilder locationClientOptionBuilder;
	private BDLocation latestLocation;
	private BDLocationListener locationListener;

	private LocationManager(Context context) {
		locationClient = new LocationClient(context.getApplicationContext());
		locationClient.registerLocationListener(this);
	}

	public static LocationManager getInstance(Context context) {
		if (instance == null) {
			synchronized (LocationManager.class) {
				if (instance == null) {
					instance = new LocationManager(context);
				}
			}
		}
		return instance;
	}

	/**
	 * 启动定位
	 */
	public void start() {
		if (locationClient == null) {
			return;
		}
		if (locationClientOptionBuilder != null) {
			locationClient.setLocOption(locationClientOptionBuilder.build());
		}
		locationClient.start();
	}

	/**
	 * 重启定位
	 */
	public void restart() {
		stop();
		start();
	}

	/**
	 * 停止定位
	 */
	public void stop() {
		if (locationClient != null) {
			locationClient.stop();
		}
	}

	/**
	 * 接收定位数据时的回调
	 */
	@Override
	public void onReceiveLocation(BDLocation location) {
		this.latestLocation = location;
		if (locationListener != null) {
			locationListener.onReceiveLocation(location);
		}
	}

	/**
	 * 判断定位是否失败
	 * 
	 * @param location
	 * @return
	 */
	public static boolean isLocactionFailed(BDLocation location) {
		return location == null
				|| (location.getLocType() != 61 && location.getLocType() != 65
						&& location.getLocType() != 66
						&& location.getLocType() != 68 && location.getLocType() != 161);
	}

	@Override
	public void onReceivePoi(BDLocation location) {
	}

	/**
	 * 读取最近一次定位数据
	 * 
	 * @return
	 */
	public BDLocation getLatestBDLocation() {
		return latestLocation;
	}

	/**
	 * 读取最近一次定位数据
	 * 
	 * @return
	 */
	public Location getLatestLocation() {
		if (latestLocation == null) {
			return null;
		}
		return new Location(latestLocation.getLongitude(),
				latestLocation.getLatitude());
	}

	/**
	 * 执行简单空的定位计划
	 */
	public void executeWithSimplePlan() {
		executeWithSimplePlan(null);
	}

	/**
	 * 执行定位计划。
	 * 
	 * @param listener
	 *            定位成功后的回调
	 */
	public void executeWithSimplePlan(OnLocationSuccessListener listener) {
		executeWithPlan(new SimpleLocationPlan(listener));
	}

	/**
	 * 执行定位计划
	 * 
	 * @param plan
	 */
	public void executeWithPlan(LocationPlan plan) {
		stop();
		plan.execute(this);
		if (latestLocation != null) {
			onReceiveLocation(latestLocation);
		}
	}

	/**
	 * 注册定位成功回调
	 * 
	 * @param locationListener
	 */
	public void registerLocationListener(BDLocationListener locationListener) {
		this.locationListener = locationListener;
	}

	/**
	 * 设置LocationClientOption建造器
	 * 
	 * @param builder
	 */
	public void setLocationClientOptionBuilder(
			LocationClientOptionBuilder builder) {
		this.locationClientOptionBuilder = builder;
	}

	/**
	 * 定义了一个定位计划应该满足的规范。所有实现该接口的类均应通过回调{@link LocationManager#start()}来启动真正的定位。
	 */
	public static interface LocationPlan {
		void execute(LocationManager locationManager);
	}

	/**
	 * LocationClientOption的建造器。
	 */
	public static interface LocationClientOptionBuilder {
		LocationClientOption build();
	}

}
