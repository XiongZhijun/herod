/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved.
 */
package org.herod.worker.phone;

import static org.herod.worker.phone.Constants.ORDER;
import static org.herod.worker.phone.Constants.TYPE;

import java.util.ArrayList;
import java.util.List;

import org.herod.framework.ViewFindable;
import org.herod.framework.lbs.Location;
import org.herod.framework.lbs.LocationManager;
import org.herod.framework.utils.TextViewUtils;
import org.herod.framework.utils.ToastUtils;
import org.herod.order.common.BaseActivity;
import org.herod.order.common.model.Order;
import org.herod.worker.phone.lbs.LocationItem;
import org.herod.worker.phone.lbs.LocationOverlay;
import org.herod.worker.phone.lbs.LocationUtils;
import org.herod.worker.phone.lbs.MKSearchHelper;
import org.herod.worker.phone.lbs.MKSearchListenerWrapper;
import org.herod.worker.phone.model.MapAddress;
import org.herod.worker.phone.model.MapAddress.AddressType;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

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
	private ProgressDialog routeProgressDialog;
	private Order order;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mBMapMan = new BMapManager(getApplication());
		mBMapMan.init(getString(R.string.BaiDuMapKey), null);
		setContentView(R.layout.activity_map);
		mMapView = (MapView) findViewById(R.id.bmapsView);
		mMapView.setBuiltInZoomControls(true);
		findViewById(R.id.currentLocation).setOnClickListener(this);
		findViewById(R.id.shopLocation).setOnClickListener(this);
		findViewById(R.id.buyerLocation).setOnClickListener(this);
		findViewById(R.id.route).setOnClickListener(this);
		findViewById(R.id.routeInfo).setVisibility(View.GONE);
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
		// setCenter(currentLocation);

		Bundle extras = getIntent().getExtras();
		MapType type = (MapType) extras.getSerializable(TYPE);
		order = (Order) extras.getSerializable(ORDER);
		showButtons(type);
		destAddress = getDestAddress(type, order);
		wpAddresses = getWPAddresses(type, order);
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

	private void showButtons(MapType type) {
		findViewById(R.id.currentLocation).setVisibility(View.VISIBLE);
		switch (type) {
		case All:
			findViewById(R.id.shopLocation).setVisibility(View.VISIBLE);
			findViewById(R.id.buyerLocation).setVisibility(View.VISIBLE);
			break;
		case Shop:
			findViewById(R.id.shopLocation).setVisibility(View.VISIBLE);
			findViewById(R.id.buyerLocation).setVisibility(View.GONE);
			break;
		case Buyer:
			findViewById(R.id.shopLocation).setVisibility(View.GONE);
			findViewById(R.id.buyerLocation).setVisibility(View.VISIBLE);
			break;
		default:
			break;
		}
	}

	private List<MapAddress> getWPAddresses(MapType type, Order order) {
		List<MapAddress> addresses = new ArrayList<MapAddress>();
		if (type == MapType.All) {
			addresses.add(new MapAddress(order.getShopAddress(),
					AddressType.Shop));
		}
		return addresses;
	}

	private MapAddress getDestAddress(MapType type, Order order) {
		switch (type) {
		case All:
		case Buyer:
			return new MapAddress(order.getDeliveryAddress(), AddressType.Buyer);
		case Shop:
			return new MapAddress(order.getShopAddress(), AddressType.Shop);
		default:
			return null;
		}
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
		if (v.getId() == R.id.currentLocation) {
			Location currentLocation = LocationManager.getInstance(this)
					.getLatestLocation();
			setCenter(currentLocation);
		} else if (v.getId() == R.id.shopLocation) {
			setCenter(order.getShopAddress().getLocation());
		} else if (v.getId() == R.id.buyerLocation) {
			setCenter(order.getDeliveryAddress().getLocation());
		} else if (v.getId() == R.id.route) {
			Location currentLocation = LocationManager.getInstance(this)
					.getLatestLocation();
			setCenter(currentLocation);
			resetRouteProgressDialog();
			routeProgressDialog = ProgressDialog.show(this, "提示", "路径规划中……",
					false, true);
			new MKSearchHelper(mBMapMan).doSearchFromCurrentLocation(this,
					new MKSearchListener(), destAddress, wpAddresses);
		}
	}

	private void resetRouteProgressDialog() {
		if (routeProgressDialog != null && routeProgressDialog.isShowing()) {
			routeProgressDialog.dismiss();
			routeProgressDialog = null;
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

	public static void showMapActivity(Context context, MapType type,
			Order order) {
		Intent intent = new Intent(context, MapActivity.class);
		intent.putExtra(TYPE, type);
		intent.putExtra(ORDER, order);
		context.startActivity(intent);
	}

	public static void showMapActivity(Context context, Order order) {
		showMapActivity(context, MapType.All, order);
	}

	class MKSearchListener extends MKSearchListenerWrapper {
		@Override
		public void onGetDrivingRouteResult(MKDrivingRouteResult result,
				int iError) {
			resetRouteProgressDialog();
			if (result == null) {
				ToastUtils.showToast("路径规划失败！", Toast.LENGTH_LONG);
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

	public static enum MapType {
		Buyer, Shop, All
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
