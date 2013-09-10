/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved.
 */
package org.herod.worker.phone;

import static org.herod.worker.phone.Constants.DEST_ADDRESS;
import static org.herod.worker.phone.Constants.WP_ADDRESSES;

import java.util.ArrayList;
import java.util.List;

import org.herod.framework.ViewFindable;
import org.herod.framework.lbs.Location;
import org.herod.framework.lbs.LocationManager;
import org.herod.framework.utils.TextViewUtils;
import org.herod.order.common.BaseActivity;
import org.herod.worker.phone.lbs.LocationItem;
import org.herod.worker.phone.lbs.LocationOverlay;
import org.herod.worker.phone.lbs.LocationUtils;
import org.herod.worker.phone.lbs.MKSearchHelper;
import org.herod.worker.phone.lbs.MKSearchListenerWrapper;
import org.herod.worker.phone.model.MapAddress;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.RouteOverlay;
import com.baidu.mapapi.search.MKDrivingRouteResult;
import com.baidu.mapapi.search.MKRoutePlan;
import com.baidu.platform.comapi.basestruct.GeoPoint;

/**
 * 
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 */
public class MapActivity extends BaseActivity implements OnClickListener,
		ViewFindable {
	BMapManager mBMapMan = null;
	MapView mMapView = null;
	private MapAddress destAddress;
	private List<MapAddress> wpAddresses;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mBMapMan = new BMapManager(getApplication());
		mBMapMan.init(getString(R.string.BaiDuMapKey), null);
		setContentView(R.layout.activity_map);
		mMapView = (MapView) findViewById(R.id.bmapsView);
		mMapView.setBuiltInZoomControls(true);
		findViewById(R.id.location).setOnClickListener(this);
		findViewById(R.id.route).setOnClickListener(this);
		findViewById(R.id.routeInfo).setVisibility(View.GONE);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void onResume() {
		mMapView.onResume();
		if (mBMapMan != null) {
			mBMapMan.start();
		}
		super.onResume();
		Location currentLocation = LocationManager.getInstance(this)
				.getLatestLocation();
		// setCenter(currentLocation);

		Bundle extras = getIntent().getExtras();
		destAddress = (MapAddress) extras.getSerializable(DEST_ADDRESS);
		wpAddresses = (List<MapAddress>) extras.getSerializable(WP_ADDRESSES);
		if (destAddress != null) {
			setCenter(destAddress.getAddress().getLocation());
		} else {
			setCenter(currentLocation);
		}
		List<LocationItem> locationItems = new ArrayList<LocationItem>();
		locationItems.add(new LocationItem(this, new MapAddress("我的位置",
				currentLocation)));
		locationItems.add(new LocationItem(this, destAddress));
		if (wpAddresses != null) {
			for (MapAddress address : wpAddresses) {
				locationItems.add(new LocationItem(this, address));
			}
		}
		showLocation(locationItems);

	}

	private void setCenter(Location center) {
		MapController mMapController = mMapView.getController();
		GeoPoint point = LocationUtils.createGeoPoint(center);
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
	public void onClick(View v) {
		if (v.getId() == R.id.location) {
			Location currentLocation = LocationManager.getInstance(this)
					.getLatestLocation();
			setCenter(currentLocation);
		} else if (v.getId() == R.id.route) {
			Location currentLocation = LocationManager.getInstance(this)
					.getLatestLocation();
			setCenter(currentLocation);

			new MKSearchHelper(mBMapMan).doSearchFromCurrentLocation(this,
					new MKSearchListener(), destAddress, wpAddresses);
		}
	}

	protected void showLocation(List<LocationItem> locationItems) {
		LocationOverlay locationOverlay = new LocationOverlay(this,
				R.drawable.location_current, mMapView);
		for (LocationItem item : locationItems) {
			locationOverlay.addItem(item);
		}
		mMapView.getOverlays().add(locationOverlay);
		mMapView.refresh();
	}

	private void showRouteInfo(MKRoutePlan plan, RouteType type) {
		findViewById(R.id.routeInfo).setVisibility(View.VISIBLE);
		TextViewUtils.setText(this, R.id.type, type.getName());
		TextViewUtils.setText(this, R.id.time,
				(int) Math.ceil(plan.getTime() / 60.0d));
		TextViewUtils.setText(this, R.id.distance, plan.getDistance());
	}

	class MKSearchListener extends MKSearchListenerWrapper {
		@Override
		public void onGetDrivingRouteResult(MKDrivingRouteResult result,
				int iError) {
			if (result == null) {
				return;
			}
			RouteOverlay routeOverlay = new RouteOverlay(MapActivity.this,
					mMapView);
			MKRoutePlan plan = result.getPlan(0);
			routeOverlay.setData(plan.getRoute(0));
			mMapView.getOverlays().add(routeOverlay);
			mMapView.refresh();

			showRouteInfo(plan, RouteType.Driving);
		}
	}

	private enum RouteType {
		Walking("步行"), Driving("驾车");
		private String name;

		private RouteType(String name) {
			this.name = name;
		}

		private String getName() {
			return name;
		}
	}

}
