/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.buyer.phone.fragments;

import org.herod.buyer.phone.R;
import org.herod.buyer.phone.ShoppingCartCache;

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
public class SubmitOrderInfoFragment extends DialogFragment implements
		OnClickListener {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setStyle(DialogFragment.STYLE_NO_TITLE,
				android.R.style.Theme_Holo_Light_Dialog);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_submit_order_info, container,
				false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		int totalQuantity = ShoppingCartCache.getInstance().getTotalQuantity();
		double totalAmount = ShoppingCartCache.getInstance().getTotalAmount();
		setText(R.id.totalQuantity, totalQuantity);
		setText(R.id.totalAmount, totalAmount);
		view.findViewById(R.id.submitOrdersButton).setOnClickListener(this);
		view.findViewById(R.id.cancelButton).setOnClickListener(this);
	}

	protected void setText(int viewId, Object text) {
		((TextView) getView().findViewById(viewId)).setText(text.toString());
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.cancelButton) {
			dismiss();
		}
	}
}
