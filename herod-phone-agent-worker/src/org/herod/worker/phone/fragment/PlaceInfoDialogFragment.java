/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.worker.phone.fragment;

import static org.herod.order.common.Constants.TEL;
import static org.herod.worker.phone.Constants.ADDRESS;
import static org.herod.worker.phone.Constants.DEST_ADDRESS;
import static org.herod.worker.phone.Constants.LOCATION_NAME;
import static org.herod.worker.phone.Constants.PHONE;
import static org.herod.worker.phone.Constants.TYPE;

import org.herod.framework.BundleBuilder;
import org.herod.order.common.model.Address;
import org.herod.worker.phone.MapActivity;
import org.herod.worker.phone.R;
import org.herod.worker.phone.model.MapAddress;
import org.herod.worker.phone.model.MapAddress.AddressType;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 * 
 */
public class PlaceInfoDialogFragment extends DialogFragment implements
		OnClickListener {
	private String phone;
	private String locationName;
	private Address address;
	private Type type;

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
		phone = args.getString(PHONE);
		locationName = args.getString(LOCATION_NAME);
		address = (Address) args.getSerializable(ADDRESS);
		type = (Type) args.getSerializable(TYPE);
		((TextView) view.findViewById(R.id.phone)).setText(phone);
		((TextView) view.findViewById(R.id.location)).setText(locationName);
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
		Intent intent = new Intent(getActivity(), MapActivity.class);
		AddressType addressType = type == Type.Buyer ? AddressType.Buyer
				: AddressType.Shop;
		intent.putExtra(DEST_ADDRESS, new MapAddress(address, addressType));
		startActivity(intent);

	}

	private void onPhoneClickListener() {
		Intent phoneIntent = new Intent(Intent.ACTION_DIAL, Uri.parse(TEL
				+ phone));
		startActivity(phoneIntent);
	}

	public static void showFragment(FragmentActivity activity, Address address,
			String phone, Type type) {
		PlaceInfoDialogFragment fragment = new PlaceInfoDialogFragment();
		Bundle args = new BundleBuilder().putString(PHONE, phone)
				.putString(LOCATION_NAME, address.getAddress())
				.putSerializable(ADDRESS, address).putSerializable(TYPE, type)
				.build();
		fragment.setArguments(args);
		fragment.show(activity.getSupportFragmentManager(), null);
	}

	public static enum Type {
		Buyer, Shop
	}

}
