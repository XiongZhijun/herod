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
import org.herod.worker.phone.GoodsListActivity;
import org.herod.worker.phone.R;
import org.herod.worker.phone.WorkerContext;
import org.herod.worker.phone.fragment.OrderListFragment.FragmentType;
import org.herod.worker.phone.view.OrderTabPageIndicator;
import org.herod.worker.phone.view.OrderView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class OrderListFragment extends Fragment implements
		AsyncTaskable<FragmentType, List<Order>>, IXListViewListener {
	public static final int NEW_ORDER_ITEMS = 1;
	private DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
	private XListView ordersListView;
	private String title;
	private FragmentType type;
	private int index = 0;
	private OrderTabPageIndicator indicator;
	private Handler handler;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
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
		if (orders == null) {
			return;
		}
		ordersListView.setAdapter(new OrderListAdapter(this, orders, handler));
		indicator.setTabQauntity(index, orders.size());
		ordersListView.stopRefresh();
		ordersListView.setRefreshTime(dateFormat.format(new Date()));
	}

	public void startGoodsActivity(OrderView orderView, long shopId,
			String shopName, String serialNumber) {
		Intent intent = new Intent(getActivity(), GoodsListActivity.class);
		intent.putExtra("shopId", shopId);
		intent.putExtra("shopName", shopName);
		intent.putExtra("serialNumber", serialNumber);
		startActivityForResult(intent, NEW_ORDER_ITEMS);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		// super.onActivityResult(requestCode, resultCode, data);
		// String serialNumber = data.getStringExtra("serialNumber");
		// HashMap<Long, OrderItem> newOrderItemsMap = (HashMap<Long,
		// OrderItem>) data
		// .getSerializableExtra("newOrderItemsMap");
		// OrderEditor orderEditor = OrderEditorManager.getInstance()
		// .findOrderEditor(serialNumber);
		// // if(orderEditor != null) {
		// // orderEditor.
		// // }
		Toast.makeText(getActivity(), "on activity result", Toast.LENGTH_SHORT)
				.show();
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
		this.handler = handler;
	}

	public static enum FragmentType {
		WaitAccept, WaitComplete
	}

}
