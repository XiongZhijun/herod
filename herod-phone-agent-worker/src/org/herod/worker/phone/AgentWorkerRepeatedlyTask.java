/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved.
 */
package org.herod.worker.phone;

import org.herod.framework.HerodTask.AsyncTaskable;
import org.herod.framework.RepeatedlyTask;

import android.content.Context;

/**
 * 
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 */
public class AgentWorkerRepeatedlyTask<Params, Result> extends
		RepeatedlyTask<Params, Result> {
	public AgentWorkerRepeatedlyTask(
			AsyncTaskable<Params, Result> asyncTaskable, Params... params) {
		super(asyncTaskable, params);
	}

	@Override
	public void execute(Context context) {
		new AgentWorkerTask<Params, Result>(context, runnable, postExecutor)
				.execute(paramsLoader.getParams());
	}

}
