/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved.
 */
package org.herod.worker.phone.fragment;

import static org.herod.order.common.Constants.MESSAGE;
import static org.herod.worker.phone.RequestCodes.REQUEST_ORDER_ASYNC_OPERATE;
import static org.herod.worker.phone.RequestCodes.RESULT_SUCCESS;

import org.herod.framework.BundleBuilder;
import org.herod.framework.HerodTask.BackgroudRunnable;
import org.herod.framework.HerodTask.PostExecutor;
import org.herod.framework.utils.ToastUtils;
import org.herod.order.common.model.Result;
import org.herod.worker.phone.AgentWorkerTask;
import org.herod.worker.phone.fragment.ConfirmDialogFragment.OnOkButtonClickListener;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.Toast;

/**
 * 
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 */
public class AsyncTaskConfirmDialogFragment extends ConfirmDialogFragment
		implements OnOkButtonClickListener, PostExecutor<Result> {
	private static final String FAILED_MESSAGE = "failedMessage";
	private static final String SUCCESS_MESSAGE = "successMessage";
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
			getTargetFragment().onActivityResult(REQUEST_ORDER_ASYNC_OPERATE,
					RESULT_SUCCESS, null);
			message = getSuccessMessage();
		} else {
			message = getFailedMessage();
		}
		ToastUtils.showToast(message, Toast.LENGTH_SHORT);
	}

	public static void show(Fragment targetFragment,
			BackgroudRunnable<Object, Result> backgroundRunnable,
			String... messages) {
		AsyncTaskConfirmDialogFragment fragment = new AsyncTaskConfirmDialogFragment();
		fragment.setTargetFragment(targetFragment, REQUEST_ORDER_ASYNC_OPERATE);
		fragment.backgroundRunnable = backgroundRunnable;
		BundleBuilder bundleBuilder = new BundleBuilder();
		if (messages.length > 0)
			bundleBuilder.putString(MESSAGE, messages[0]);
		if (messages.length > 1)
			bundleBuilder.putString(SUCCESS_MESSAGE, messages[1]);
		if (messages.length > 2)
			bundleBuilder.putString(FAILED_MESSAGE, messages[2]);
		fragment.setArguments(bundleBuilder.build());
		fragment.show(targetFragment.getFragmentManager(), null);
	}

	private String getSuccessMessage() {
		return getArguments().getString(SUCCESS_MESSAGE);
	}

	private String getFailedMessage() {
		return getArguments().getString(FAILED_MESSAGE);
	}

}
