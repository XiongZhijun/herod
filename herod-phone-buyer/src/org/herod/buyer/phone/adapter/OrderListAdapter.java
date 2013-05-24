/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.buyer.phone.adapter;

import java.util.ArrayList;
import java.util.List;

import org.herod.buyer.phone.R;
import org.herod.buyer.phone.model.Order;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

/**
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 * 
 */
public class OrderListAdapter extends BaseAdapter {

	private List<Order> orders = new ArrayList<Order>();
	private LayoutInflater inflater;
	private Context context;

	public OrderListAdapter(Context context, List<Order> orders) {
		this.orders = orders;
		this.context = context;
		inflater = LayoutInflater.from(context);
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
			view = inflater.inflate(R.layout.shopping_cart_order, null);
		} else {
			view = convertView;
		}
		bindView(view);
		return view;
	}

	protected void bindView(View view) {
		LinearLayout orderItemsListView = (LinearLayout) view
				.findViewById(R.id.orderItemsListView);
		orderItemsListView.removeAllViews();
		for (int i = 0; i < 5; i++) {
			View line = new View(context);
			line.setBackgroundColor(0xFFE5E5E5);
			orderItemsListView.addView(line, new LayoutParams(
					LayoutParams.MATCH_PARENT, 1));

			View child = inflater.inflate(R.layout.shopping_cart_order_item,
					null);
			LayoutParams param = new LayoutParams(LayoutParams.MATCH_PARENT,
					LayoutParams.WRAP_CONTENT);
			orderItemsListView.addView(child, param);

		}
	}

}
