/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved.
 */
package org.herod.worker.phone.lbs;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.Button;

import com.baidu.mapapi.map.ItemizedOverlay;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MapView.LayoutParams;
import com.baidu.mapapi.map.OverlayItem;
import com.baidu.mapapi.map.PopupClickListener;
import com.baidu.platform.comapi.basestruct.GeoPoint;

/**
 * 
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 */
public class LocationOverlay extends ItemizedOverlay<OverlayItem> implements
		PopupClickListener {

	private Button button;

	public LocationOverlay(Context context, int defaultMarker, MapView mapView) {
		this(context, context.getResources().getDrawable(defaultMarker),
				mapView);
	}

	public LocationOverlay(Context context, Drawable defaultMarker,
			MapView mapView) {
		super(defaultMarker, mapView);
		button = new Button(context);
	}

	@Override
	public boolean onTap(int index) {
		OverlayItem item = getItem(index);
		button.setText(item.getTitle());
		// 创建布局参数
		LayoutParams layoutParam = new MapView.LayoutParams(
		// 控件宽,继承自ViewGroup.LayoutParams
				MapView.LayoutParams.WRAP_CONTENT,
				// 控件高,继承自ViewGroup.LayoutParams
				MapView.LayoutParams.WRAP_CONTENT,
				// 使控件固定在某个地理位置
				item.getPoint(), 0, -32,
				// 控件对齐方式
				MapView.LayoutParams.BOTTOM_CENTER);
		// 添加View到MapView中
		mMapView.addView(button, layoutParam);
		return true;
	}

	@Override
	public boolean onTap(GeoPoint pt, MapView mMapView) {
		mMapView.removeView(button);
		return false;
	}

	@Override
	public void onClickedPopup(int arg0) {
		// TODO Auto-generated method stub

	}

}