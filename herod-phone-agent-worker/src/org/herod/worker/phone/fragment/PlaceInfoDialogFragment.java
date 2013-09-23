/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.worker.phone.fragment;

import static org.herod.order.common.Constants.NAME;
import static org.herod.worker.phone.Constants.LOCATION_NAME;
import static org.herod.worker.phone.Constants.ORDER;
import static org.herod.worker.phone.Constants.PHONE;
import static org.herod.worker.phone.Constants.TYPE;

import org.herod.framework.BundleBuilder;
import org.herod.framework.ViewFindable;
import org.herod.framework.utils.DeviceUtils;
import org.herod.framework.utils.StringUtils;
import org.herod.framework.utils.TextViewUtils;
import org.herod.order.common.model.Address;
import org.herod.order.common.model.Order;
import org.herod.worker.phone.MapActivity;
import org.herod.worker.phone.MapActivity.MapType;
import org.herod.worker.phone.R;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

/**
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 * 
 */
public class PlaceInfoDialogFragment extends DialogFragment implements
		OnClickListener, ViewFindable {
	private String phone;
	private String locationName;
	private MapType type;
	private Order order;
	private String name;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setStyle(DialogFragment.STYLE_NO_TITLE,
				android.R.style.Theme_Holo_Light_Dialog);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.place_info_dialog_fragment, container,
				false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		view.findViewById(R.id.phone).setOnClickListener(this);
		view.findViewById(R.id.location).setOnClickListener(this);
		Bundle args = getArguments();
		name = args.getString(NAME);
		if (StringUtils.isBlank(name)) {
			view.findViewById(R.id.name).setVisibility(View.GONE);
		} else {
			view.findViewById(R.id.name).setVisibility(View.VISIBLE);
			TextViewUtils.setText(this, R.id.name, name);
		}
		phone = args.getString(PHONE);
		locationName = args.getString(LOCATION_NAME);
		order = (Order) args.getSerializable(ORDER);
		type = (MapType) args.getSerializable(TYPE);
		TextViewUtils.setText(this, R.id.phone, phone);
		TextViewUtils.setText(this, R.id.location, locationName);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.phone) {
			onPhoneClickListener();
		} else if (v.getId() == R.id.location) {
			onLocationClickListener();
		}
	}

	private void onLocationClickListener() {
		MapActivity.showMapActivity(getActivity(), type, order);
	}

	private void onPhoneClickListener() {
		DeviceUtils.dial(getActivity(), phone);
	}

	public static void showFragment(FragmentActivity activity, Order order,
			Address address, String phone, MapType type) {
		PlaceInfoDialogFragment fragment = new PlaceInfoDialogFragment();
		BundleBuilder bundleBuilder = new BundleBuilder();
		bundleBuilder.putString(PHONE, phone)
				.putString(LOCATION_NAME, address.getAddress())
				.putSerializable(ORDER, order).putSerializable(TYPE, type);
		if (type == MapType.Buyer) {
			bundleBuilder.putString(NAME, order.getBuyerName());
		}
		Bundle args = bundleBuilder.build();
		fragment.setArguments(args);
		fragment.show(activity.getSupportFragmentManager(), null);
	}

	@Override
	public View findViewById(int id) {
		return getView().findViewById(id);
	}

}
