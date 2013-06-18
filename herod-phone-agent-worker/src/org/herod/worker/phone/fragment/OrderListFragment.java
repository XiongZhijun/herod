package org.herod.worker.phone.fragment;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.herod.worker.phone.R;
import org.herod.worker.phone.model.Order;
import org.herod.worker.phone.model.OrderItem;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class OrderListFragment extends Fragment {

	private ListView ordersListView;

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
		List<Order> orders = createOrders();
		ordersListView.setAdapter(new OrderListAdapter(getActivity(), orders));
	}

	private List<Order> createOrders() {
		List<Order> orders = new ArrayList<Order>();
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		for (int i = 0; i < 2; i++) {
			Order order = new Order();
			Date now = new Date();
			order.setSerialNumber(dateFormat.format(now) + "-0000" + i);
			order.setShopName("外婆家");
			order.setShopPhone("15967119516");
			order.setBuyerName("张三");
			order.setBuyerPhone("15967119516");
			order.setSubmitTime(now);
			order.addOrderItem(createOrderItem());
			order.addOrderItem(createOrderItem());
			order.setCostOfRunErrands(5);
			orders.add(order);
		}
		return orders;
	}

	private OrderItem createOrderItem() {
		OrderItem orderItem = new OrderItem();
		orderItem.setGoodsName("麻婆豆腐");
		orderItem.setQuantity(2);
		orderItem.setUnitPrice(10);
		return orderItem;
	}
}
