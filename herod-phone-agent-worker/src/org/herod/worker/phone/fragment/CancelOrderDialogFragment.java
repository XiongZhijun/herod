/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.worker.phone.fragment;

import java.util.Collections;
import java.util.Map;

import org.herod.worker.phone.R;

import android.support.v4.app.FragmentActivity;

/**
 * 
 * 
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 * 
 */
public class CancelOrderDialogFragment extends FormFragment {

	@Override
	protected int getLayout() {
		return R.layout.dialog_cancel_order;
	}

	@Override
	protected Map<Integer, String> getFormShowFromToMap() {
		return Collections.emptyMap();
	}

	@Override
	protected Map<Integer, String> getFormInputShowFromToMap() {
		return Collections.singletonMap(R.id.reason, "reason");
	}

	public static void showDialog(FragmentActivity activity,
			OnOkButtonClickListener onOkButtonClickListener) {
		CancelOrderDialogFragment fragment = new CancelOrderDialogFragment();
		fragment.setOnOkButtonClickListener(onOkButtonClickListener);
		fragment.show(activity.getSupportFragmentManager(), null);
	}
}
