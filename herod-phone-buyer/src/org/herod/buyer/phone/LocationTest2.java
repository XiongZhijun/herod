/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.buyer.phone;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

/**
 * 
 * 
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 * 
 */
public class LocationTest2 implements LocationListener {

	private Context context;
	private LocationManager locationManager;

	public void init(Context context) {
		this.context = context;
		locationManager = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);
//		locationManager.requestLocationUpdates(
//				LocationManager.NETWORK_PROVIDER, 1000, 0, this);
		// GPS 定位
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
				1000, 0, this);

	}

	@Override
	public void onLocationChanged(Location location) {
		Toast.makeText(context, location.toString(), Toast.LENGTH_LONG).show();
		if (!location.getProvider().equals(LocationManager.GPS_PROVIDER)) {
			return;
		}
		 location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		locationManager.removeUpdates(this);
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:"
				+ location.getLatitude() + "," + location.getLongitude()
				+ "?q=" + location.getLatitude() + ","
				+ location.getLongitude() + "(hhhh)"));// LabelName是标签内容
		// lat
		// long
		// 是经纬度
		context.startActivity(intent);

	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}
}
