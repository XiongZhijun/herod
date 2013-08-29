/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved.
 */
package org.herod.framework;

import org.herod.framework.HerodTask.AsyncTaskable;
import org.herod.framework.HerodTask.BackgroudRunnable;
import org.herod.framework.HerodTask.PostExecutor;

/**
 * 
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 */
public class RepeatedlyTask<Params, Result> {

	protected BackgroudRunnable<Params, Result> runnable;
	protected PostExecutor<Result> postExecutor;
	protected ParamsLoader<Params> paramsLoader;

	public RepeatedlyTask(
			RepeatedlyAsyncTaskable<Params, Result> repeatedlyAsyncTaskable) {
		this(repeatedlyAsyncTaskable, repeatedlyAsyncTaskable);
	}

	public RepeatedlyTask(AsyncTaskable<Params, Result> asyncTaskable,
			Params... params) {
		this(asyncTaskable, asyncTaskable, params);
	}

	public RepeatedlyTask(BackgroudRunnable<Params, Result> runnable,
			PostExecutor<Result> postExecutor, final Params... params) {
		this(runnable, postExecutor, new ParamsLoader<Params>() {
			public Params[] getParams() {
				return params;
			}
		});
	}

	public RepeatedlyTask(AsyncTaskable<Params, Result> asyncTaskable,
			ParamsLoader<Params> paramsLoaders) {
		this(asyncTaskable, asyncTaskable, paramsLoaders);
	}

	public RepeatedlyTask(BackgroudRunnable<Params, Result> runnable,
			PostExecutor<Result> postExecutor, ParamsLoader<Params> paramsLoader) {
		super();
		this.runnable = runnable;
		this.postExecutor = postExecutor;
		this.paramsLoader = paramsLoader;
	}

	public void execute() {
		new HerodTask<Params, Result>(runnable, postExecutor)
				.execute(paramsLoader.getParams());
	}

	public static interface ParamsLoader<Params> {
		Params[] getParams();
	}

	public static interface RepeatedlyAsyncTaskable<Params, Result> extends
			AsyncTaskable<Params, Result>, ParamsLoader<Params> {

	}
}
