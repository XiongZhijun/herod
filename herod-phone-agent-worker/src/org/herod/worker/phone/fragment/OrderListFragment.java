package org.herod.worker.phone.fragment;

import static org.herod.order.common.Constants.HH_MM_SS;
import static org.herod.worker.phone.Constants.INDEX;
import static org.herod.worker.phone.RequestCodes.REQUEST_CANCEL_ORDER;
import static org.herod.worker.phone.RequestCodes.REQUEST_NEW_ORDER_ITEMS;
import static org.herod.worker.phone.RequestCodes.REQUEST_ORDER_ASYNC_OPERATE;
import static org.herod.worker.phone.RequestCodes.REQUEST_UPDATE_ORDER;
import static org.herod.worker.phone.RequestCodes.RESULT_SUCCESS;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.herod.framework.BaseFragment;
import org.herod.framework.BundleBuilder;
import org.herod.framework.HerodTask.AsyncTaskable;
import org.herod.framework.HerodTask.BackgroudRunnable;
import org.herod.framework.HerodTask.PostExecutor;
import org.herod.framework.ViewFindable;
import org.herod.framework.utils.ToastUtils;
import org.herod.framework.widget.XListView;
import org.herod.framework.widget.XListView.IXListViewListener;
import org.herod.order.common.RefreshButtonHelper;
import org.herod.order.common.model.Order;
import org.herod.worker.phone.AgentWorkerRepeatedlyTask;
import org.herod.worker.phone.AgentWorkerTask;
import org.herod.worker.phone.R;
import org.herod.worker.phone.TabPageIndicatorable;
import org.herod.worker.phone.WorkerContext;
import org.herod.worker.phone.view.OrderTabPageIndicator;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public abstract class OrderListFragment extends BaseFragment implements
		ViewFindable, AsyncTaskable<Object, List<Order>>, IXListViewListener {

	private DateFormat dateFormat = new SimpleDateFormat(HH_MM_SS);
	protected XListView ordersListView;
	protected OrderListAdapter orderListAdapter;
	private RefreshButtonHelper refreshButtonHelper;
	private AgentWorkerRepeatedlyTask<Object, List<Order>> loadOrdersTask;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		loadOrdersTask = new AgentWorkerRepeatedlyTask<Object, List<Order>>(
				this);
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
	public void onPostExecute(List<Order> orders) {
		if (refreshButtonHelper.checkNullResult(orders)) {
			updateQuantity(0);
			return;
		}
		refreshButtonHelper.disableRefresh();
		ordersListView.setVisibility(View.VISIBLE);
		orderListAdapter = new OrderListAdapter(this, orders);
		ordersListView.setAdapter(orderListAdapter);
		updateQuantity(orders.size());
		ordersListView.stopRefresh();
		ordersListView.setRefreshTime(dateFormat.format(new Date()));
	}

	protected void updateQuantity(int quantity) {
		OrderTabPageIndicator indicator = getIndicator();
		if (indicator != null) {
			indicator.setTabQauntity(getIndex(), quantity);
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (orderListAdapter != null) {
			orderListAdapter.notifyDataSetChanged();
		}
		switch (requestCode) {
		case REQUEST_NEW_ORDER_ITEMS:
			if (orderListAdapter != null)
				orderListAdapter.notifyDataSetChanged();
			return;
		case REQUEST_CANCEL_ORDER:
		case REQUEST_ORDER_ASYNC_OPERATE:
		case REQUEST_UPDATE_ORDER:
			if (resultCode == RESULT_SUCCESS) {
				refreshOrderList();
			}
			break;
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

	public abstract String getTitle();

	protected OrderTabPageIndicator getIndicator() {
		FragmentActivity activity = getActivity();
		if (activity != null && activity instanceof TabPageIndicatorable) {
			return ((TabPageIndicatorable) activity).getIndicator();
		}
		return null;
	}

	public static OrderListFragment createWaitAcceptOrderListFragment(int index) {
		OrderListFragment fragment = new WaitAcceptOrderListFragment();
		initArgs(index, fragment);
		return fragment;
	}

	public static OrderListFragment createWaitCompleteOrderListFragment(
			int index) {
		OrderListFragment fragment = new WaitCompleteOrderListFragment();
		initArgs(index, fragment);
		return fragment;
	}

	public static OrderListFragment createCompletedOrderListFragment(int index) {
		OrderListFragment fragment = new CompletedOrderListFragment();
		initArgs(index, fragment);
		return fragment;
	}

	public static OrderListFragment createCanceledOrderListFragment(int index) {
		OrderListFragment fragment = new CanceledOrderListFragment();
		initArgs(index, fragment);
		return fragment;
	}

	private static void initArgs(int index, OrderListFragment fragment) {
		Bundle args = new BundleBuilder().putInt(INDEX, index).build();
		fragment.setArguments(args);
	}

	public static class WaitAcceptOrderListFragment extends OrderListFragment {
		public List<Order> runOnBackground(Object... params) {
			return WorkerContext.getWorkerService().findWaitAcceptOrders();
		}

		public String getTitle() {
			return "待受理订单";
		}
	}

	public static class WaitCompleteOrderListFragment extends OrderListFragment {
		public List<Order> runOnBackground(Object... params) {
			return WorkerContext.getWorkerService().findWaitCompleteOrders();
		}

		public String getTitle() {
			return "待完成订单";
		}
	}

	public static class CompletedOrderListFragment extends
			HistoryOrderListFragment {
		public String getTitle() {
			return "已完成订单";
		}

		protected List<Order> loadFromServer(int begin, int count) {
			return WorkerContext.getWorkerService().findCompletedOrders(begin,
					count);
		}
	}

	public static class CanceledOrderListFragment extends
			HistoryOrderListFragment {
		public String getTitle() {
			return "已取消订单";
		}

		protected List<Order> loadFromServer(int begin, int count) {
			return WorkerContext.getWorkerService().findCanceledOrders(begin,
					count);
		}
	}

	public static abstract class HistoryOrderListFragment extends
			OrderListFragment {

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View view = super.onCreateView(inflater, container,
					savedInstanceState);
			ordersListView.setPullLoadEnable(true);
			return view;
		}

		public void refreshOrderList() {
			super.refreshOrderList();
		}

		public void onLoadMore() {
			if (orderListAdapter == null) {
				return;
			}
			new AgentWorkerTask<Object, List<Order>>(getActivity(),
					new BackgroudRunnable<Object, List<Order>>() {
						public List<Order> runOnBackground(Object... params) {
							return loadFromServer(getBegin(), PAGE_COUNT);
						}
					}, new LoadMoreTask()).execute();
		}

		public List<Order> runOnBackground(Object... params) {
			return loadFromServer(0, PAGE_COUNT);
		}

		protected abstract List<Order> loadFromServer(int begin, int count);

		protected int getBegin() {
			int count = orderListAdapter != null ? orderListAdapter.getCount()
					: 0;
			return count;
		}

		protected void updateQuantity(List<Order> orders) {
		}
	}

	private class LoadMoreTask implements PostExecutor<List<Order>> {
		public void onPostExecute(List<Order> orders) {
			ordersListView.stopLoadMore();
			if (orders == null) {
				ToastUtils.showToast("读取更多订单失败，请重试！", Toast.LENGTH_SHORT);
				return;
			}
			if (orders.size() == 0) {
				ToastUtils.showToast("没有更多订单了！", Toast.LENGTH_SHORT);
			}
			if (orderListAdapter != null) {
				orderListAdapter.addOrders(orders);
			}
			if (ordersListView != null) {
				ordersListView.setPullLoadEnable(orders.size() >= PAGE_COUNT);
			}
		}
	}

	static final int PAGE_COUNT = 20;
}
