/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved.
 */
package org.herod.worker.phone;

import java.util.ArrayList;
import java.util.List;

import org.herod.framework.Location;
import org.herod.framework.lbs.LocationManager;
import org.herod.worker.phone.lbs.LocationItem;
import org.herod.worker.phone.lbs.LocationOverlay;
import org.herod.worker.phone.model.Address;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.RouteOverlay;
import com.baidu.mapapi.search.MKAddrInfo;
import com.baidu.mapapi.search.MKBusLineResult;
import com.baidu.mapapi.search.MKDrivingRouteResult;
import com.baidu.mapapi.search.MKPlanNode;
import com.baidu.mapapi.search.MKPoiResult;
import com.baidu.mapapi.search.MKSearch;
import com.baidu.mapapi.search.MKSearchListener;
import com.baidu.mapapi.search.MKShareUrlResult;
import com.baidu.mapapi.search.MKSuggestionResult;
import com.baidu.mapapi.search.MKTransitRouteResult;
import com.baidu.mapapi.search.MKWalkingRouteResult;
import com.baidu.platform.comapi.basestruct.GeoPoint;

/**
 * 
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 */
public class MapActivity extends FragmentActivity implements MKSearchListener,
		OnClickListener {
	BMapManager mBMapMan = null;
	MapView mMapView = null;
	private MKSearch mMKSearch;
	private Address address;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mBMapMan = new BMapManager(getApplication());
		mBMapMan.init("F5451f7dee03f65b9d1f7f6812a69e12", null);
		setContentView(R.layout.activity_map);
		mMapView = (MapView) findViewById(R.id.bmapsView);
		mMapView.setBuiltInZoomControls(true);
		findViewById(R.id.location).setOnClickListener(this);
		findViewById(R.id.route).setOnClickListener(this);
	}

	@Override
	protected void onResume() {
		mMapView.onResume();
		if (mBMapMan != null) {
			mBMapMan.start();
		}
		super.onResume();
		Location currentLocation = LocationManager.getInstance(this)
				.getLatestLocation();
		setCenter(currentLocation);

		address = (Address) getIntent().getExtras().getSerializable("address");
		List<LocationItem> locationItems = new ArrayList<LocationItem>();
		locationItems.add(new LocationItem(this, new Address("我的位置",
				currentLocation), R.drawable.current_location));
		locationItems.add(new LocationItem(this, address,
				R.drawable.dest_location));
		showLocation(locationItems);

	}

	private void setCenter(Location center) {
		MapController mMapController = mMapView.getController();
		GeoPoint point = new GeoPoint((int) (center.getLatitude() * 1E6),
				(int) (center.getLongitude() * 1E6));
		mMapController.setCenter(point);
		mMapController.setZoom(17);
	}

	@Override
	protected void onPause() {
		mMapView.onPause();
		if (mBMapMan != null) {
			mBMapMan.stop();
		}
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		mMapView.destroy();
		if (mBMapMan != null) {
			mBMapMan.destroy();
			mBMapMan = null;
		}
		super.onDestroy();
	}

	@Override
	public void onGetAddrResult(MKAddrInfo arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onGetBusDetailResult(MKBusLineResult arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onGetDrivingRouteResult(MKDrivingRouteResult result, int arg1) {
		if (result == null) {
			Toast.makeText(this, "错误", Toast.LENGTH_LONG).show();
			return;
		}
		Toast.makeText(this, "正确", Toast.LENGTH_LONG).show();
		RouteOverlay routeOverlay = new RouteOverlay(this, mMapView); // 此处仅展示一个方案作为示例
		routeOverlay.setData(result.getPlan(0).getRoute(0));
		mMapView.getOverlays().add(routeOverlay);
		mMapView.refresh();
	}

	@Override
	public void onGetPoiDetailSearchResult(int arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onGetPoiResult(MKPoiResult arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onGetShareUrlResult(MKShareUrlResult arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onGetSuggestionResult(MKSuggestionResult arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onGetTransitRouteResult(MKTransitRouteResult arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onGetWalkingRouteResult(MKWalkingRouteResult arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.location) {
			Location currentLocation = LocationManager.getInstance(this)
					.getLatestLocation();
			setCenter(currentLocation);
		} else if (v.getId() == R.id.route) {
			Location currentLocation = LocationManager.getInstance(this)
					.getLatestLocation();
			setCenter(currentLocation);
			mMKSearch = new MKSearch();
			mMKSearch.init(mBMapMan, this);
			MKPlanNode start = new MKPlanNode();
			start.pt = new GeoPoint(
					(int) (currentLocation.getLatitude() * 1E6),
					(int) (currentLocation.getLongitude() * 1E6));
			MKPlanNode end = new MKPlanNode();
			Location location = address.getLocation();
			end.pt = new GeoPoint((int) (location.getLatitude() * 1E6),
					(int) (location.getLongitude() * 1E6));// 设置驾车路线搜索策略，时间优先、费用最少或距离最短
			mMKSearch.setDrivingPolicy(MKSearch.ECAR_TIME_FIRST);
			mMKSearch.drivingSearch(null, start, null, end);
		}
	}

	protected void showLocation(List<LocationItem> locationItems) {
		LocationOverlay locationOverlay = new LocationOverlay(this,
				R.drawable.current_location, mMapView);
		for (LocationItem item : locationItems) {
			locationOverlay.addItem(item);
		}
		mMapView.getOverlays().add(locationOverlay);
		mMapView.refresh();
	}
}
