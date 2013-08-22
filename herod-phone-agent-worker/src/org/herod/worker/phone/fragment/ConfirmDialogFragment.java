/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.worker.phone.fragment;

import org.herod.framework.ci.InjectViewHelper;
import org.herod.worker.phone.R;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * 
 * 
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 * 
 */
public class ConfirmDialogFragment extends DialogFragment implements
		OnClickListener {
	private String message;
	private OnOkButtonClickListener onOkButtonClickListener;
	private OnCancelButtonClickListener onCancelButtonClickListener;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setStyle(DialogFragment.STYLE_NO_TITLE,
				android.R.style.Theme_Holo_Light_Dialog);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.dialog_confirm, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		new InjectViewHelper().injectViews(view);
		((TextView) view.findViewById(R.id.message)).setText(message);
		view.findViewById(R.id.okButton).setOnClickListener(this);
		view.findViewById(R.id.cancelButton).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.okButton) {
			if (onOkButtonClickListener != null) {
				onOkButtonClickListener.onOk();
			}
		} else if (v.getId() == R.id.cancelButton) {
			if (onCancelButtonClickListener != null) {
				onCancelButtonClickListener.onCancel();
			}
		}
		dismiss();
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setOnOkButtonClickListener(
			OnOkButtonClickListener onOkButtonClickListener) {
		this.onOkButtonClickListener = onOkButtonClickListener;
	}

	public void setOnCancelButtonClickListener(
			OnCancelButtonClickListener onCancelButtonClickListener) {
		this.onCancelButtonClickListener = onCancelButtonClickListener;
	}

	public static interface OnOkButtonClickListener {
		void onOk();
	}

	public static interface OnCancelButtonClickListener {
		void onCancel();
	}

	public static void showDialog(FragmentActivity activity, String message,
			OnOkButtonClickListener onOkButtonClickListener) {
		ConfirmDialogFragment fragment = new ConfirmDialogFragment();
		fragment.setMessage(message);
		fragment.onOkButtonClickListener = onOkButtonClickListener;
		fragment.show(activity.getSupportFragmentManager(), null);
	}
}
