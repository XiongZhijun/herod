/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved.
 */
package org.herod.framework;

import org.herod.framework.HerodTask.AsyncTaskable;
import org.herod.framework.HerodTask.BackgroudRunnable;
import org.herod.framework.HerodTask.PostExecutor;

import android.content.Context;
import android.view.View;

/**
 * 
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 */
public class RepeatedlyTask<Params, Result> {

	protected BackgroudRunnable<Params, Result> runnable;
	protected PostExecutor<Result> postExecutor;
	protected ParamsLoader<Params> paramsLoader;
	private View progressBar;

	public RepeatedlyTask(AsyncTaskable<Params, Result> asyncTaskable,
			Params... params) {
		this(asyncTaskable, asyncTaskable, params);
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
				if (progressBar != null) {
					progressBar.setVisibility(View.GONE);
				}
				postExecutor.onPostExecute(result);
			}

		};
		this.paramsLoader = paramsLoader;
	}

	public void setProgressBar(View progressBar) {
		this.progressBar = progressBar;
	}

	public void execute(Context context) {
		new HerodTask<Params, Result>(runnable, postExecutor)
				.execute(paramsLoader.getParams());
		if (progressBar != null) {
			progressBar.setVisibility(View.VISIBLE);
		}
	}

	public static interface ParamsLoader<Params> {
		Params[] getParams();
	}

	public static interface RepeatedlyAsyncTaskable<Params, Result> extends
			AsyncTaskable<Params, Result>, ParamsLoader<Params> {

	}
}
