package org.herod.buyer.phone;

import android.os.AsyncTask;

public class HerodTask<Params, Result> extends
		AsyncTask<Params, Object, Result> {
	private BackgroudRunnable<Params, Result> runnable;
	private PostExecutor<Result> postExecutor;

	public HerodTask(AsyncTaskable<Params, Result> asyncTaskable) {
		this(asyncTaskable, asyncTaskable);
	}

	public HerodTask(BackgroudRunnable<Params, Result> runnable,
			PostExecutor<Result> postExecutor) {
		super();
		this.runnable = runnable;
		this.postExecutor = postExecutor;
	}

	@Override
	protected Result doInBackground(Params... params) {
		return runnable.runOnBackground(params);
	}

	@Override
	protected void onPostExecute(Result result) {
		postExecutor.onPostExecute(result);
	}

	public static interface BackgroudRunnable<Params, Result> {
		Result runOnBackground(Params... params);
	}

	public static interface PostExecutor<Result> {
		void onPostExecute(Result result);
	}

	public static interface AsyncTaskable<Params, Result> extends
			BackgroudRunnable<Params, Result>, PostExecutor<Result> {

	}
}