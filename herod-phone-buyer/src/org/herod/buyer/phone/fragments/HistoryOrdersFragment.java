/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved.
 */
package org.herod.buyer.phone.fragments;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.herod.buyer.phone.BuyerContext;
import org.herod.buyer.phone.R;
import org.herod.buyer.phone.adapter.OrderListAdapter;
import org.herod.buyer.phone.db.OrderDao;
import org.herod.buyer.phone.fragments.ConfirmDialogFragment.OnOkButtonClickListener;
import org.herod.framework.BundleBuilder;
import org.herod.framework.HerodTask.AsyncTaskable;
import org.herod.framework.RepeatedlyTask;
import org.herod.framework.db.DatabaseOpenHelper;
import org.herod.order.common.ListViewFragment;
import org.herod.order.common.model.Order;
import org.herod.order.common.model.OrderStatus;

import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * 
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 */
public class HistoryOrdersFragment extends
		ListViewFragment<ListView, Object, List<Order>> implements
		OnClickListener {

	private static final String STATUSES = "statuses";

	@Override
	protected int getLayoutResources() {
		return R.layout.fragment_history_orders;
	}

	@Override
	protected void initListView(ListView listView) {
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		View clearButton = findViewById(R.id.clearButton);
		clearButton.setOnClickListener(this);
		loadDataFromRemote();
	}

	@Override
	protected RepeatedlyTask<Object, List<Order>> createRepeatedlyTask(
			AsyncTaskable<Object, List<Order>> asyncTaskable) {
		return new RepeatedlyTask<Object, List<Order>>(asyncTaskable);
	}

	@Override
	public List<Order> runOnBackground(Object... params) {
		OrderStatus[] statuses = (OrderStatus[]) getArguments()
				.getSerializable(STATUSES);
		SQLiteOpenHelper openHelper = new DatabaseOpenHelper(getActivity());
		OrderDao orderDao = new OrderDao(openHelper);
		if (currentFragmentIsUncompleteOrders(statuses)) {
			refreshUncompleteOrdersStatus(orderDao);
		}
		List<Order> allOrders = orderDao.getAllOrders(statuses);
		openHelper.close();
		return allOrders;
	}

	@Override
	protected void onPostExecute0(List<Order> orders) {
		ListAdapter adapter = new OrderListAdapter(getActivity(), orders);
		getListView().setAdapter(adapter);
		findViewById(R.id.clearButton).setEnabled(orders.size() > 0);
	}

	private boolean currentFragmentIsUncompleteOrders(OrderStatus[] statuses) {
		List<OrderStatus> list = Arrays.asList(statuses);
		return list.contains(OrderStatus.Submitted)
				|| list.contains(OrderStatus.Acceptted)
				|| list.contains(OrderStatus.Rejected);
	}

	private void refreshUncompleteOrdersStatus(OrderDao orderDao) {
		Set<String> uncompleteOrders = orderDao.findAllUncompleteOrders();
		Map<String, Order> ordersMap = BuyerContext.getBuyerService()
				.findOrders(uncompleteOrders);
		orderDao.deleteOrders(ordersMap.keySet());
		orderDao.addOrders(ordersMap.values());
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.clearButton:
			clearHistoryOrders(v);
			break;
		default:
			break;
		}
	}

	public void clearHistoryOrders(View v) {
		ConfirmDialogFragment.showDialog(getActivity(), "确定删除所有历史记录？",
				new OnClearShoppingCartOkListener());
	}

	private class OnClearShoppingCartOkListener implements
			OnOkButtonClickListener {
		public void onOk() {
			SQLiteOpenHelper openHelper = new DatabaseOpenHelper(getActivity()
					.getApplicationContext());
			OrderDao orderDao = new OrderDao(openHelper);
			orderDao.deleteAllOrders();
			openHelper.close();
			loadDataFromRemote();
		}

	}

	public static HistoryOrdersFragment createFragment(OrderStatus[] statuses) {
		HistoryOrdersFragment fragment = new HistoryOrdersFragment();
		Bundle args = new BundleBuilder().putSerializable(STATUSES, statuses)
				.build();
		fragment.setArguments(args);
		return fragment;
	}

}
