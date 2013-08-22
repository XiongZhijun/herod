/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved.
 */
package org.herod.worker.phone.fragment;

import org.herod.framework.HerodTask.BackgroudRunnable;
import org.herod.framework.HerodTask.PostExecutor;
import org.herod.order.common.model.Result;
import org.herod.worker.phone.AgentWorkerTask;
import org.herod.worker.phone.MainActivity;
import org.herod.worker.phone.fragment.ConfirmDialogFragment.OnOkButtonClickListener;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

/**
 * 
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 */
public class AsyncTaskConfirmDialogFragment extends ConfirmDialogFragment
		implements OnOkButtonClickListener, PostExecutor<Result> {
	private String successMessage;
	private String failedMessage;
	private Handler handler;
	private BackgroudRunnable<Object, Result> backgroundRunnable;
	private Context applicationContext;

	public AsyncTaskConfirmDialogFragment() {
		setOnOkButtonClickListener(this);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		applicationContext = getActivity().getApplicationContext();
	}

	@Override
	public void onOk() {
		new AgentWorkerTask<Object, Result>(applicationContext,
				backgroundRunnable, this).execute();
	}

	@Override
	public void onPostExecute(Result result) {
		String message;
		if (result != null && result.isSuccess()) {
			handler.sendMessage(handler
					.obtainMessage(MainActivity.MESSAGE_KEY_REFRESH_ORDER_LIST));
			message = successMessage;
		} else {
			message = failedMessage;
		}
		Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show();
	}

	public static void show(FragmentActivity activity, Handler handler,
			BackgroudRunnable<Object, Result> backgroundRunnable,
			String... messages) {
		AsyncTaskConfirmDialogFragment fragment = new AsyncTaskConfirmDialogFragment();
		fragment.handler = handler;
		fragment.backgroundRunnable = backgroundRunnable;
		if (messages.length > 0)
			fragment.setMessage(messages[0]);
		if (messages.length > 1)
			fragment.successMessage = messages[1];
		if (messages.length > 2)
			fragment.failedMessage = messages[2];
		fragment.show(activity.getSupportFragmentManager(), null);
	}
}
