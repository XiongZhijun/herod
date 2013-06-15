/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.order.web.buyer;

import java.util.Map;

import org.herod.common.GpsTools;
import org.herod.order.model.Location;

/**
 * 
 * 
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 * 
 */
public class ServiceAreaWrapper {
	private Location location = new Location();
	private double serviceRadius;

	public ServiceAreaWrapper(Map<String, Object> map) {
		location.setLatitude(getDouble(map, "latitude"));
		location.setLongitude(getDouble(map, "longitude"));
		this.serviceRadius = getDouble(map, "serviceRadius");
	}

	public boolean isInServiceArea(double latitude, double longitude) {
		double distance = GpsTools.calculateDistance(latitude, longitude,
				location.getLatitude(), location.getLongitude());
		return distance <= serviceRadius;
	}

	private Double getDouble(Map<String, Object> map, String key) {
		Object value = map.get(key);
		if (value == null) {
			return 0d;
		}
		if (value instanceof Double || value.getClass() == long.class) {
			return (Double) value;
		}
		return Double.parseDouble(value.toString());
	}
}
