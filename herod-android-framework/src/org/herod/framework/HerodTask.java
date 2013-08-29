package org.herod.framework;

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
		try {
			return runnable.runOnBackground(params);
		} catch (Exception e) {
			onException(e);
			return null;
		}
	}

	protected void onException(Exception e) {
	}

	@Override
	protected void onPostExecute(Result result) {
		postExecutor.onPostExecute(result);
	}

	public static <Params, Result> void execute(
			AsyncTaskable<Params, Result> asyncTaskable, Params... params) {
		new HerodTask<Params, Result>(asyncTaskable).execute(params);
	}

	public static <Params, Result> void execute(
			BackgroudRunnable<Params, Result> runnable,
			PostExecutor<Result> postExecutor, Params... params) {
		new HerodTask<Params, Result>(runnable, postExecutor).execute(params);
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
