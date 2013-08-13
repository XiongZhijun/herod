/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved.
 */
package org.herod.worker.phone;

import org.herod.framework.HerodTask;
import org.herod.framework.rest.AuthenticationException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

/**
 * 
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 */
@SuppressWarnings("hiding")
public class AgentWorkerTask<P, R> extends HerodTask<P, R> {

	private Context context;

	public AgentWorkerTask(Context context, AsyncTaskable<P, R> asyncTaskable) {
		super(asyncTaskable);
		this.context = context;
	}

	public AgentWorkerTask(Context context, BackgroudRunnable<P, R> runnable,
			PostExecutor<R> postExecutor) {
		super(runnable, postExecutor);
		this.context = context;
	}

	protected void onException(Exception e) {
		if (e instanceof AuthenticationException
				&& !(context instanceof LoginActivity)) {
			new Handler(Looper.getMainLooper()).post(new Runnable() {
				@Override
				public void run() {
					context.startActivity(new Intent(context,
							LoginActivity.class));
					Toast.makeText(context, "当前用户没有登录，请重新登录！",
							Toast.LENGTH_LONG).show();
					WorkerContext.setLoginToken(null);
					if (context instanceof Activity
							&& !((Activity) context).isFinishing()) {
						((Activity) context).finish();
					}
				}
			});

		}
	}
}
