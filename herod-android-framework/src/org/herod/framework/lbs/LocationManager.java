/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.framework.lbs;

import org.herod.framework.Location;

import android.content.Context;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

/**
 * 
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

	public void start() {
		if (locationClient == null) {
			return;
		}
		if (locationClientOptionBuilder != null) {
			locationClient.setLocOption(locationClientOptionBuilder.build());
		}
		locationClient.start();
	}

	public void restart() {
		stop();
		start();
	}

	public void stop() {
		if (locationClient != null) {
			locationClient.stop();
		}
	}

	@Override
	public void onReceiveLocation(BDLocation location) {
		this.latestLocation = location;
		if (locationListener != null) {
			locationListener.onReceiveLocation(location);
		}
	}

	public static boolean isLocactionFailed(BDLocation location) {
		return location == null
				|| (location.getLocType() != 61 && location.getLocType() != 65
						&& location.getLocType() != 66
						&& location.getLocType() != 68 && location.getLocType() != 161);
	}

	@Override
	public void onReceivePoi(BDLocation location) {
	}

	public BDLocation getLatestBDLocation() {
		return latestLocation;
	}

	public Location getLatestLocation() {
		if (latestLocation == null) {
			return null;
		}
		return new Location(latestLocation.getLongitude(),
				latestLocation.getLatitude());
	}

	public void executeWithPlan(LocationPlan plan) {
		stop();
		plan.execute(this);
	}

	public void registerLocationListener(BDLocationListener locationListener) {
		this.locationListener = locationListener;
	}

	public void setLocationClientOptionBuilder(
			LocationClientOptionBuilder builder) {
		this.locationClientOptionBuilder = builder;
	}

	public static interface LocationPlan {
		void execute(LocationManager locationManager);
	}

	public static interface LocationClientOptionBuilder {
		LocationClientOption build();
	}

}
