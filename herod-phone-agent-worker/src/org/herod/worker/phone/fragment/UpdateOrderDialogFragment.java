/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.worker.phone.fragment;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.herod.worker.phone.R;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

/**
 * 
 * 
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 * 
 */
public class UpdateOrderDialogFragment extends FormFragment {

	@Override
	protected int getLayout() {
		return R.layout.dialog_update_order;
	}

	@Override
	protected Map<Integer, String> getFormShowFromToMap() {
		return Collections.singletonMap(R.id.comment, "comment");
	}

	@SuppressLint("UseSparseArrays")
	@Override
	protected Map<Integer, String> getFormInputShowFromToMap() {
		Map<Integer, String> fromToMap = new HashMap<Integer, String>();
		fromToMap.put(R.id.comment, "comment");
		fromToMap.put(R.id.reason, "reason");
		return fromToMap;
	}

	public static void showDialog(FragmentActivity activity, String comment,
			OnOkButtonClickListener onOkButtonClickListener,
			OnCancelButtonClickListener onCancelButtonClickListener) {
		UpdateOrderDialogFragment fragment = new UpdateOrderDialogFragment();
		Bundle args = new Bundle();
		args.putString("comment", comment);
		fragment.setArguments(args);
		fragment.setOnOkButtonClickListener(onOkButtonClickListener);
		fragment.setOnCancelButtonClickListener(onCancelButtonClickListener);
		fragment.show(activity.getSupportFragmentManager(), null);
	}
}
