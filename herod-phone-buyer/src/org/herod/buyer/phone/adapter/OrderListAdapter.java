/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.buyer.phone.adapter;

import java.util.ArrayList;
import java.util.List;

import org.herod.buyer.phone.AbstractOrdersActivity;
import org.herod.buyer.phone.view.OrderView;
import org.herod.order.common.model.Order;

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
	private AbstractOrdersActivity activity;

	public OrderListAdapter(AbstractOrdersActivity activity, List<Order> orders) {
		this.orders = orders;
		this.activity = activity;
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
		View view;
		if (convertView == null) {
			view = new OrderView(activity);
		} else {
			view = convertView;
		}
		bindView((OrderView) view, position);
		return view;
	}

	protected void bindView(OrderView view, int position) {
		Order order = orders.get(position);
		view.setOrder(order);
	}

}
