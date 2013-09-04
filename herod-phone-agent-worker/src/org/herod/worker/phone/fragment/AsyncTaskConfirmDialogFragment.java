/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved.
 */
package org.herod.worker.phone.fragment;

import org.herod.framework.BundleBuilder;
import org.herod.framework.HerodTask.BackgroudRunnable;
import org.herod.framework.HerodTask.PostExecutor;
import org.herod.framework.utils.ToastUtils;
import org.herod.order.common.model.Result;
import org.herod.worker.phone.AgentWorkerTask;
import org.herod.worker.phone.MainActivity;
import org.herod.worker.phone.fragment.ConfirmDialogFragment.OnOkButtonClickListener;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;
import static org.herod.worker.phone.Constants.*;

/**
 * 
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 */
public class AsyncTaskConfirmDialogFragment extends ConfirmDialogFragment
		implements OnOkButtonClickListener, PostExecutor<Result> {
	private static final String FAILED_MESSAGE = "failedMessage";
	private static final String SUCCESS_MESSAGE = "successMessage";
	private Handler handler;
	private BackgroudRunnable<Object, Result> backgroundRunnable;

	public AsyncTaskConfirmDialogFragment() {
		setOnOkButtonClickListener(this);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onOk() {
		new AgentWorkerTask<Object, Result>(getActivity(), backgroundRunnable,
				this).execute();
	}

	@Override
	public void onPostExecute(Result result) {
		String message;
		if (result != null && result.isSuccess()) {
			handler.sendMessage(handler
					.obtainMessage(MainActivity.MESSAGE_KEY_REFRESH_ORDER_LIST));
			message = getSuccessMessage();
		} else {
			message = getFailedMessage();
		}
		ToastUtils.showToast(message, Toast.LENGTH_SHORT);
	}

	public static void show(FragmentActivity activity, Handler handler,
			BackgroudRunnable<Object, Result> backgroundRunnable,
			String... messages) {
		AsyncTaskConfirmDialogFragment fragment = new AsyncTaskConfirmDialogFragment();
		fragment.handler = handler;
		fragment.backgroundRunnable = backgroundRunnable;
		BundleBuilder bundleBuilder = new BundleBuilder();
		if (messages.length > 0)
			bundleBuilder.putString(MESSAGE, messages[0]);
		if (messages.length > 1)
			bundleBuilder.putString(SUCCESS_MESSAGE, messages[1]);
		if (messages.length > 2)
			bundleBuilder.putString(FAILED_MESSAGE, messages[2]);
		fragment.setArguments(bundleBuilder.build());
		fragment.show(activity.getSupportFragmentManager(), null);
	}

	private String getSuccessMessage() {
		return getArguments().getString(SUCCESS_MESSAGE);
	}

	private String getFailedMessage() {
		return getArguments().getString(FAILED_MESSAGE);
	}

}
