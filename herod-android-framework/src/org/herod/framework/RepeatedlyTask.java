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

	private BackgroudRunnable<Params, Result> runnable;
	private PostExecutor<Result> postExecutor;
	private Params[] params;

	public RepeatedlyTask(AsyncTaskable<Params, Result> asyncTaskable,
			Params... params) {
		this(asyncTaskable, asyncTaskable, params);
	}

	public RepeatedlyTask(BackgroudRunnable<Params, Result> runnable,
			PostExecutor<Result> postExecutor, Params... params) {
		super();
		this.runnable = runnable;
		this.postExecutor = postExecutor;
		this.params = params;
	}

	public void execute() {
		new HerodTask<Params, Result>(runnable, postExecutor).execute(params);
	}
}
