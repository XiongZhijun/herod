/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved.
 */
package org.herod.order.common;

import org.herod.framework.BaseFragment;
import org.herod.framework.HerodTask.AsyncTaskable;
import org.herod.framework.RepeatedlyTask;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

/**
 * 
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 */
public abstract class ListViewFragment<V extends AbsListView, P, Result>
		extends BaseFragment implements AsyncTaskable<P, Result> {

	private V listView;
	private View emptyView;
	private View progressBar;
	private RepeatedlyTask<P, Result> repeatedlyTask;
	private RefreshButtonHelper refreshButtonHelper;
	private View refreshButton;

	@SuppressWarnings("unchecked")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(getLayoutResources(), container, false);
		listView = (V) view.findViewById(R.id.listView);
		progressBar = view.findViewById(R.id.progressBar);
		refreshButton = view.findViewById(R.id.refreshButton);
		emptyView = view.findViewById(R.id.emptyView);
		listView.setEmptyView(emptyView);
		initListView(listView);
		return view;
	}

	protected int getLayoutResources() {
		return R.layout.fragment_list_view;
	}

	protected abstract void initListView(V listView);

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		repeatedlyTask = createRepeatedlyTask(this);
		refreshButtonHelper = new RefreshButtonHelper(this, repeatedlyTask,
				R.id.refreshButton, R.id.listView);
	}

	protected abstract RepeatedlyTask<P, Result> createRepeatedlyTask(
			AsyncTaskable<P, Result> asyncTaskable);

	@Override
	public void onResume() {
		super.onResume();
	}

	public void loadDataFromRemote() {
		setVisibility(emptyView, View.GONE);
		setVisibility(refreshButton, View.GONE);
		setVisibility(progressBar, View.VISIBLE);
		setVisibility(listView, View.GONE);
		repeatedlyTask.execute(getActivity());
	}

	@Override
	public void onPostExecute(Result data) {
		setVisibility(progressBar, View.GONE);
		if (refreshButtonHelper.checkNullResult(data)) {
			return;
		}
		onPostExecute0(data);
	}

	public V getListView() {
		return listView;
	}

	protected abstract void onPostExecute0(Result data);

	protected static void setVisibility(View view, int visibility) {
		if (view != null) {
			view.setVisibility(visibility);
		}
	}

}
