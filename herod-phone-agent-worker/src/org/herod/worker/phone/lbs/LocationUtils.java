/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved.
 */
package org.herod.worker.phone.lbs;

import org.herod.framework.lbs.Location;
import org.herod.order.common.model.Address;
import org.herod.worker.phone.model.MapAddress;

import com.baidu.mapapi.search.MKPlanNode;
import com.baidu.mapapi.search.MKWpNode;
import com.baidu.platform.comapi.basestruct.GeoPoint;

/**
 * 
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 */
public class LocationUtils {

	public static GeoPoint createGeoPoint(Location location) {
		return new GeoPoint((int) (location.getLatitude() * 1E6),
				(int) (location.getLongitude() * 1E6));
	}

	public static GeoPoint createGeoPoint(Address address) {
		return createGeoPoint(address.getLocation());
	}

	public static MKPlanNode createMKPlanNode(Location location) {
		MKPlanNode node = new MKPlanNode();
		node.pt = LocationUtils.createGeoPoint(location);
		return node;
	}

	public static MKPlanNode createMKPlanNode(MapAddress address) {
		return createMKPlanNode(address.getAddress());
	}

	public static MKPlanNode createMKPlanNode(Address address) {
		MKPlanNode node = createMKPlanNode(address.getLocation());
		node.name = address.getAddress();
		return node;
	}

	public static MKWpNode createMKWpNode(MapAddress address) {
		return createMKWpNode(address.getAddress());

	}

	public static MKWpNode createMKWpNode(Address address) {
		MKWpNode node = new MKWpNode();
		node.pt = createGeoPoint(address);
		node.name = address.getAddress();
		return node;
	}
}
