package org.herod.worker.phone.fragment;

import java.util.Collections;
import java.util.List;

import org.herod.framework.HerodTask;
import org.herod.framework.HerodTask.AsyncTaskable;
import org.herod.worker.phone.R;
import org.herod.worker.phone.WorkerContext;
import org.herod.worker.phone.fragment.OrderListFragment.FragmentType;
import org.herod.worker.phone.model.Order;
import org.herod.worker.phone.view.OrderTabPageIndicator;

import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class OrderListFragment extends Fragment implements
		AsyncTaskable<FragmentType, List<Order>> {
	private ListView ordersListView;
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
		ordersListView = (ListView) view.findViewById(R.id.ordersListView);
		return view;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
	}

	@Override
	public void onResume() {
		super.onResume();
		refreshOrderList();
	}

	public void refreshOrderList() {
		new HerodTask<FragmentType, List<Order>>(this).execute(type);
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
		ordersListView.setAdapter(new OrderListAdapter(getActivity(), orders,
				handler));
		indicator.setTabQauntity(index, orders.size());
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
