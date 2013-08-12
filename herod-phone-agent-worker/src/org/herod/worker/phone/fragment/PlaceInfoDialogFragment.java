/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.worker.phone.fragment;

import org.herod.worker.phone.MapActivity;
import org.herod.worker.phone.R;
import org.herod.worker.phone.model.Address;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
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
		phone = getArguments().getString("phone");
		locationName = getArguments().getString("locationName");
		address = (Address) getArguments().getSerializable("address");
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
		intent.putExtra("destAddress", address);
		startActivity(intent);

	}

	private void onPhoneClickListener() {
		Intent phoneIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"
				+ phone));
		startActivity(phoneIntent);
	}
}
