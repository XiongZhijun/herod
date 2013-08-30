/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved.
 */
package org.herod.framework;

import org.herod.framework.HerodTask.AsyncTaskable;
import org.herod.framework.HerodTask.BackgroudRunnable;
import org.herod.framework.HerodTask.PostExecutor;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * 
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 */
public class RepeatedlyTask<Params, Result> {

	protected BackgroudRunnable<Params, Result> runnable;
	protected PostExecutor<Result> postExecutor;
	protected ParamsLoader<Params> paramsLoader;
	private ProgressDialog dialog;
	private boolean showProgressDialog = true;

	public RepeatedlyTask(AsyncTaskable<Params, Result> asyncTaskable,
			Params... params) {
		this(asyncTaskable, asyncTaskable, params);
	}

	@SuppressWarnings("unchecked")
	public RepeatedlyTask(AsyncTaskable<Params, Result> asyncTaskable,
			boolean showProgressDialog) {
		this(asyncTaskable, asyncTaskable);
		this.showProgressDialog = showProgressDialog;
	}

	RepeatedlyTask(BackgroudRunnable<Params, Result> runnable,
			PostExecutor<Result> postExecutor, final Params... params) {
		this(runnable, postExecutor, new ParamsLoader<Params>() {
			public Params[] getParams() {
				return params;
			}
		});
	}

	RepeatedlyTask(BackgroudRunnable<Params, Result> runnable,
			final PostExecutor<Result> postExecutor,
			ParamsLoader<Params> paramsLoader) {
		super();
		this.runnable = runnable;
		this.postExecutor = new PostExecutor<Result>() {
			public void onPostExecute(Result result) {
				dismissDialog();
				postExecutor.onPostExecute(result);
			}

		};
		this.paramsLoader = paramsLoader;
	}

	public void execute(Context context) {
		showDialog(context);
		new HerodTask<Params, Result>(runnable, postExecutor)
				.execute(paramsLoader.getParams());
	}

	private void dismissDialog() {
		if (dialog != null && dialog.isShowing()) {
			dialog.dismiss();
		}
	}

	private void showDialog(Context context) {
		if (!showProgressDialog) {
			return;
		}
		if (dialog != null && dialog.isShowing()) {
			dialog.dismiss();
		}
		dialog = ProgressDialog.show(context, "提示", "数据读取中……");
	}

	public static interface ParamsLoader<Params> {
		Params[] getParams();
	}

	public static interface RepeatedlyAsyncTaskable<Params, Result> extends
			AsyncTaskable<Params, Result>, ParamsLoader<Params> {

	}
}
