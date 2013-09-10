/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved.
 */
package org.herod.worker.phone.lbs;

import org.herod.order.common.model.Address;
import org.herod.worker.phone.R;
import org.herod.worker.phone.model.MapAddress;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.baidu.mapapi.map.OverlayItem;
import com.baidu.platform.comapi.basestruct.GeoPoint;

/**
 * 
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 */
public class LocationItem extends OverlayItem {

	public LocationItem(Context context, MapAddress address) {
		this(address.getAddress(), context.getResources().getDrawable(
				getMarker(address)));
	}

	private static int getMarker(MapAddress address) {
		switch (address.getType()) {
		case Shop:
			return R.drawable.ic_pin_shop;
		case Buyer:
			return R.drawable.ic_pin_buyer;
		default:
			return R.drawable.location_current;
		}
	}

	public LocationItem(Address address, Drawable marker) {
		super(new GeoPoint((int) (address.getLocation().getLatitude() * 1E6),
				(int) (address.getLocation().getLongitude() * 1E6)), address
				.getAddress(), null);
		setMarker(marker);
	}

}
