/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.buyer.phone;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

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
public class LocationTest {
	public LocationClient mLocationClient = null;
	public BDLocationListener myListener = new MyLocationListener();
	private Context context;

	public void init(Context context) {
		this.context = context;
		mLocationClient = new LocationClient(context.getApplicationContext()); // 声明LocationClient类
		mLocationClient.registerLocationListener(myListener); // 注册监听函数
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);
		option.setAddrType("all");// 返回的定位结果包含地址信息
		option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度,默认值gcj02
		option.setScanSpan(5000);// 设置发起定位请求的间隔时间为5000ms
		option.disableCache(true);// 禁止启用缓存定位
		option.setPoiNumber(5); // 最多返回POI个数
		option.setPoiDistance(1000); // poi查询距离
		option.setPoiExtraInfo(true); // 是否需要POI的电话和地址等详细信息
		option.setPriority(LocationClientOption.GpsFirst);
		mLocationClient.setLocOption(option);

		mLocationClient.start();
	}

	public void requestLocation() {
		if (mLocationClient != null && mLocationClient.isStarted())
			mLocationClient.requestLocation();
		else
			Log.d("LocSDK3", "locClient is null or not started");
	}

	public void requestPOI() {
		if (mLocationClient != null && mLocationClient.isStarted())
			mLocationClient.requestPoi();
	}

	public void requestOffLine() {
		if (mLocationClient != null && mLocationClient.isStarted())
			mLocationClient.requestOfflineLocation();
	}

	public class MyLocationListener implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null)
				return;
			StringBuffer sb = new StringBuffer(256);
			sb.append("time : ");
			sb.append(location.getTime());
			sb.append("\nerror code : ");
			sb.append(location.getLocType());
			sb.append("\nlatitude : ");
			sb.append(location.getLatitude());
			sb.append("\nlontitude : ");
			sb.append(location.getLongitude());
			sb.append("\nradius : ");
			sb.append(location.getRadius());
			if (location.getLocType() == BDLocation.TypeGpsLocation) {
				sb.append("\nspeed : ");
				sb.append(location.getSpeed());
				sb.append("\nsatellite : ");
				sb.append(location.getSatelliteNumber());
			} else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
				sb.append("\naddr : ");
				sb.append(location.getAddrStr());
			}
			showLocation(location, sb);

		}

		int i = 0;

		private void showLocation(BDLocation location, StringBuffer sb) {
			i++;
			Log.d("TEST", sb.toString());
			Toast.makeText(context, sb.toString(), Toast.LENGTH_LONG).show();
			if (location.getLocType() != 61 && i < 20) {
				return;
			}
			mLocationClient.stop();
			Intent intent = new Intent(Intent.ACTION_VIEW,
					Uri.parse("http://ditu.google.cn/maps?hl=zh&mrt=loc&q="
							+ location.getLatitude() + ","
							+ location.getLongitude() + "("
							+ location.getAddrStr() + ")"));// LabelName是标签内容
			// lat
			// long
			// 是经纬度
			context.startActivity(intent);
		}

		public void onReceivePoi(BDLocation poiLocation) {
			if (poiLocation == null) {
				return;
			}
			StringBuffer sb = new StringBuffer(256);
			sb.append("Poi time : ");
			sb.append(poiLocation.getTime());
			sb.append("\nerror code : ");
			sb.append(poiLocation.getLocType());
			sb.append("\nlatitude : ");
			sb.append(poiLocation.getLatitude());
			sb.append("\nlontitude : ");
			sb.append(poiLocation.getLongitude());
			sb.append("\nradius : ");
			sb.append(poiLocation.getRadius());
			if (poiLocation.getLocType() == BDLocation.TypeNetWorkLocation) {
				sb.append("\naddr : ");
				sb.append(poiLocation.getAddrStr());
			}
			if (poiLocation.hasPoi()) {
				sb.append("\nPoi:");
				sb.append(poiLocation.getPoi());
			} else {
				sb.append("noPoi information");
			}
			showLocation(poiLocation, sb);
		}
	}

}
