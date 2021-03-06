/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.order.model;

import org.herod.common.GpsTools;

/**
 * 
 * 
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 * 
 */
public class Location {
	/** 经度 */
	private double longitude;
	/** 纬度 */
	private double latitude;

	public Location() {
		super();
	}

	public Location(double longitude, double latitude) {
		super();
		this.longitude = longitude;
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public boolean isInServiceArea(Shop shop) {
		Location location = shop.getLocation();
		double distance = GpsTools.calculateDistance(latitude, longitude,
				location.getLatitude(), location.getLongitude());
		return distance <= shop.getServiceArea();
	}
}
