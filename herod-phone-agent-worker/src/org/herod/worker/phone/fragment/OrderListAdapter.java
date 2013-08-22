/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.worker.phone.fragment;

import java.util.ArrayList;
import java.util.List;

import org.herod.order.common.model.Order;
import org.herod.worker.phone.handler.HerodHandler;
import org.herod.worker.phone.view.OrderView;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 * 
 */
public class OrderListAdapter extends BaseAdapter {

	private List<Order> orders = new ArrayList<Order>();
	private Context context;
	private Handler handler;
	private OrderListFragment fragment;

	public OrderListAdapter(OrderListFragment fragment, List<Order> orders,
			Handler handler) {
		this.orders = orders;
		this.context = fragment.getActivity();
		this.fragment = fragment;
		this.handler = new HerodHandler(handler);
	}

	@Override
	public int getCount() {
		return orders.size();
	}

	@Override
	public Object getItem(int position) {
		return orders.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		OrderView view;
		if (convertView == null) {
			view = new OrderView(context);
			view.setFragment(fragment);
			view.setHandler(handler);
		} else {
			view = (OrderView) convertView;
		}
		bindView((OrderView) view, position);
		return view;
	}

	protected void bindView(OrderView view, int position) {
		Order order = orders.get(position);
		view.setOrder(order);
	}

}
