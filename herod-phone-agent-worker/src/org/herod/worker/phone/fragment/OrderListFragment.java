package org.herod.worker.phone.fragment;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.herod.framework.HerodTask.AsyncTaskable;
import org.herod.framework.widget.XListView;
import org.herod.framework.widget.XListView.IXListViewListener;
import org.herod.order.common.model.Order;
import org.herod.worker.phone.AgentWorkerTask;
import org.herod.worker.phone.R;
import org.herod.worker.phone.WorkerContext;
import org.herod.worker.phone.fragment.OrderListFragment.FragmentType;
import org.herod.worker.phone.handler.HerodHandler;
import org.herod.worker.phone.view.OrderTabPageIndicator;
import org.springframework.util.CollectionUtils;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

public class OrderListFragment extends Fragment implements
		AsyncTaskable<FragmentType, List<Order>>, IXListViewListener,
		OnClickListener {
	private DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
	private XListView ordersListView;
	private View refreshButton;
	private String title;
	private FragmentType type;
	private int index = 0;
	private OrderTabPageIndicator indicator;
	private Handler handler;
	private OrderListAdapter orderListAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_orders_list, container,
				false);
		refreshButton = view.findViewById(R.id.refreshButton);
		refreshButton.setOnClickListener(this);
		ordersListView = (XListView) view.findViewById(R.id.ordersListView);
		ordersListView.setPullRefreshEnable(true);
		ordersListView.setPullLoadEnable(false);
		ordersListView.setXListViewListener(this);
		return view;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		refreshOrderList();
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	public void refreshOrderList() {
		new AgentWorkerTask<FragmentType, List<Order>>(getActivity(), this)
				.execute(type);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.refreshButton)
			refreshOrderList();
	}

	@Override
	public List<Order> runOnBackground(FragmentType... params) {
		switch (params[0]) {
		case WaitAccept:
			return WorkerContext.getWorkerService().findWaitAcceptOrders();
		case WaitComplete:
			return WorkerContext.getWorkerService().findWaitCompleteOrders();
		default:
			return Collections.emptyList();
		}
	}

	@Override
	public void onPostExecute(List<Order> orders) {
		if (CollectionUtils.isEmpty(orders)) {
			refreshButton.setVisibility(View.VISIBLE);
			ordersListView.setVisibility(View.GONE);
			return;
		}
		refreshButton.setVisibility(View.GONE);
		ordersListView.setVisibility(View.VISIBLE);
		orderListAdapter = new OrderListAdapter(this, orders, handler);
		ordersListView.setAdapter(orderListAdapter);
		indicator.setTabQauntity(index, orders.size());
		ordersListView.stopRefresh();
		ordersListView.setRefreshTime(dateFormat.format(new Date()));
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (orderListAdapter != null) {
			orderListAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public void onRefresh() {
		refreshOrderList();
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub

	}

	public void setTabPageIndicator(OrderTabPageIndicator indicator) {
		this.indicator = indicator;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

	public void setType(FragmentType type) {
		this.type = type;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public void setHandler(Handler handler) {
		this.handler = new HerodHandler(handler);
	}

	public static enum FragmentType {
		WaitAccept, WaitComplete
	}

}
