/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.worker.phone.fragment;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.herod.framework.ViewFindable;
import org.herod.framework.ci.InjectViewHelper;
import org.herod.framework.ci.annotation.InjectView;
import org.herod.framework.widget.VerifyEditText;
import org.herod.worker.phone.R;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
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
public abstract class FormFragment extends DialogFragment implements
		ViewFindable, OnClickListener {
	@InjectView(R.id.title)
	private TextView titleTextView;
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
		return inflater.inflate(getLayout(), container, false);
	}

	protected abstract int getLayout();

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		new InjectViewHelper().injectViews(view);
		view.findViewById(R.id.okButton).setOnClickListener(this);
		view.findViewById(R.id.cancelButton).setOnClickListener(this);
	}

	@Override
	public void onResume() {
		super.onResume();
		Map<Integer, String> fromToMap = getFormShowFromToMap();
		Bundle arguments = getArguments();
		for (Entry<Integer, String> entry : fromToMap.entrySet()) {
			View view = findViewById(entry.getKey());
			String argKey = entry.getValue();
			if (view instanceof TextView) {
				((TextView) view).setText(arguments.getString(argKey));
			}
		}
	}

	protected abstract Map<Integer, String> getFormShowFromToMap();

	protected abstract Map<Integer, String> getFormInputShowFromToMap();

	protected Map<String, String> getFormInputDatas() {
		Map<Integer, String> fromToMap = getFormInputShowFromToMap();
		Map<String, String> formInputDatas = new HashMap<String, String>();
		for (Entry<Integer, String> entry : fromToMap.entrySet()) {
			View view = findViewById(entry.getKey());
			if (view instanceof TextView) {
				String input = ((TextView) view).getText().toString();
				formInputDatas.put(entry.getValue(), input);
			}
		}
		return formInputDatas;
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.okButton) {
			if (onOkButtonClickListener != null) {
				if (verifyInputDatas()) {
					onOkButtonClickListener.onOk(getFormInputDatas());
				}
			} else {
				dismiss();
			}
		} else if (v.getId() == R.id.cancelButton) {
			if (onCancelButtonClickListener != null) {
				onCancelButtonClickListener.onCancel();
			} else {
				dismiss();
			}
		}

	}

	protected boolean verifyInputDatas() {
		Map<Integer, String> fromToMap = getFormInputShowFromToMap();
		boolean valid = true;
		for (int id : fromToMap.keySet()) {
			View view = findViewById(id);
			if (view instanceof VerifyEditText
					&& ((VerifyEditText) view).isInvalid()) {
				valid = false;
			}
		}
		return valid;
	}

	@Override
	public View findViewById(int id) {
		return getView().findViewById(id);
	}

	public void setTitle(String title) {
		titleTextView.setText(title);
	}

	public void setTitle(int title) {
		titleTextView.setText(title);
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
		void onOk(Map<String, String> formDatas);
	}

	public static interface OnCancelButtonClickListener {
		void onCancel();
	}
}
