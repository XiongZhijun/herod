package org.herod.worker.phone.fragment;

import static org.herod.worker.phone.Constants.INDEX;
import static org.herod.worker.phone.Constants.TITLE;
import static org.herod.worker.phone.Constants.TYPE;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.herod.framework.BaseFragment;
import org.herod.framework.HerodTask.AsyncTaskable;
import org.herod.framework.ViewFindable;
import org.herod.framework.widget.XListView;
import org.herod.framework.widget.XListView.IXListViewListener;
import org.herod.order.common.RefreshButtonHelper;
import org.herod.order.common.model.Order;
import org.herod.worker.phone.AgentWorkerRepeatedlyTask;
import org.herod.worker.phone.MainActivity;
import org.herod.worker.phone.R;
import org.herod.worker.phone.WorkerContext;
import org.herod.worker.phone.handler.HerodHandler;
import org.herod.worker.phone.view.OrderTabPageIndicator;
import org.springframework.util.CollectionUtils;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class OrderListFragment extends BaseFragment implements ViewFindable,
		AsyncTaskable<Object, List<Order>>, IXListViewListener {
	
	private static final String HH_MM_SS = "HH:mm:ss";
	private DateFormat dateFormat = new SimpleDateFormat(HH_MM_SS);
	private XListView ordersListView;
	private Handler handler;
	private OrderListAdapter orderListAdapter;
	private RefreshButtonHelper refreshButtonHelper;
	private AgentWorkerRepeatedlyTask<Object, List<Order>> loadOrdersTask;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		loadOrdersTask = new AgentWorkerRepeatedlyTask<Object, List<Order>>(
				this);
		this.handler = new HerodHandler(getMainActivity().getHandler());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_orders_list, container,
				false);
		ordersListView = (XListView) view.findViewById(R.id.ordersListView);
		ordersListView.setPullRefreshEnable(true);
		ordersListView.setPullLoadEnable(false);
		ordersListView.setXListViewListener(this);
		return view;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		refreshButtonHelper = new RefreshButtonHelper(this, loadOrdersTask,
				R.id.refreshButton, R.id.ordersListView);
	}

	@Override
	public void onResume() {
		super.onResume();
		refreshOrderList();
	}

	public void refreshOrderList() {
		if (loadOrdersTask != null) {
			loadOrdersTask.execute(getActivity());
		}
	}

	@Override
	public List<Order> runOnBackground(Object... params) {
		FragmentType type = getType();
		switch (type) {
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
			refreshButtonHelper.enableRefresh();
			return;
		}
		refreshButtonHelper.disableRefresh();
		ordersListView.setVisibility(View.VISIBLE);
		orderListAdapter = new OrderListAdapter(this, orders, handler);
		ordersListView.setAdapter(orderListAdapter);
		getIndicator().setTabQauntity(getIndex(), orders.size());
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
	}

	private int getIndex() {
		return getArguments().getInt(INDEX);
	}

	public String getTitle() {
		return getArguments().getString(TITLE);
	}

	private FragmentType getType() {
		FragmentType type = (FragmentType) getArguments().getSerializable(
				TYPE);
		return type;
	}

	protected OrderTabPageIndicator getIndicator() {
		MainActivity activity = getMainActivity();
		return activity.getIndicator();
	}

	private MainActivity getMainActivity() {
		return (MainActivity) getActivity();
	}

	public static enum FragmentType {
		WaitAccept, WaitComplete
	}

}
