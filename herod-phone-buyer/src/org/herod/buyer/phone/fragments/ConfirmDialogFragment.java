/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.buyer.phone.fragments;

import org.herod.buyer.phone.R;
import org.herod.framework.ci.InjectViewHelper;
import org.herod.framework.ci.annotation.InjectView;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
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
	private int imageResId;
	private OnButtonClickListener onButtonClickListener;

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
		message = getArguments().getString("message");
		imageResId = getArguments().getInt("image");
		((TextView) view.findViewById(R.id.message)).setText(message);
		((ImageView) view.findViewById(R.id.image))
				.setImageResource(imageResId);
		view.findViewById(R.id.okButton).setOnClickListener(this);
		view.findViewById(R.id.cancelButton).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (onButtonClickListener != null) {
			onButtonClickListener.onClick(v.getId());
		}
		dismiss();
	}

	public static interface OnButtonClickListener {
		void onClick(int id);
	}

	public static ConfirmDialogFragment newInstance(int image, String message,
			OnButtonClickListener onButtonClickListener) {
		ConfirmDialogFragment fragment = new ConfirmDialogFragment();
		Bundle args = new Bundle();
		args.putInt("image", image);
		args.putString("message", message);
		fragment.setArguments(args);
		fragment.onButtonClickListener = onButtonClickListener;
		return fragment;
	}
}
