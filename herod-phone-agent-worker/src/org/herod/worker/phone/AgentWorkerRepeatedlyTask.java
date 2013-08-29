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

	private Context context;

	public AgentWorkerRepeatedlyTask(Context context,
			AsyncTaskable<Params, Result> asyncTaskable, Params... params) {
		super(asyncTaskable, params);
		this.context = context;
	}

	@Override
	public void execute() {
		new AgentWorkerTask<Params, Result>(context, runnable, postExecutor)
				.execute(paramsLoader.getParams());
	}

}
