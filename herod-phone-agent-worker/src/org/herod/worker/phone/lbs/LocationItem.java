/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved.
 */
package org.herod.worker.phone.lbs;

import org.herod.worker.phone.model.Address;

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

	public LocationItem(Context context, Address address, int marker) {
		this(address, context.getResources().getDrawable(marker));
	}

	public LocationItem(Address address, Drawable marker) {
		super(new GeoPoint((int) (address.getLocation().getLatitude() * 1E6),
				(int) (address.getLocation().getLongitude() * 1E6)), address
				.getAddress(), null);
		setMarker(marker);
	}

}
