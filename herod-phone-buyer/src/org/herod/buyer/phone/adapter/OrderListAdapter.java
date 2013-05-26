/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.buyer.phone.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.herod.buyer.phone.BuyerContext;
import org.herod.buyer.phone.BuyerService;
import org.herod.buyer.phone.R;
import org.herod.buyer.phone.model.Order;
import org.herod.buyer.phone.model.OrderItem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 * 
 */
public class OrderListAdapter extends BaseAdapter {

	private List<Order> orders = new ArrayList<Order>();
	private LayoutInflater inflater;
	private Context context;
	private BuyerService buyerService;

	public OrderListAdapter(Context context, List<Order> orders) {
		this.orders = orders;
		this.context = context;
		inflater = LayoutInflater.from(context);
		buyerService = BuyerContext.getBuyerService();
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
		bindView(view, position);
		return view;
	}

	protected void bindView(View view, int position) {
		Order order = orders.get(position);
		long shopId = order.getShopId();
		Map<String, Object> shop = buyerService.findShopById(shopId);
		setText(view, R.id.shopName, shop.get("name"));
		LinearLayout orderItemsListView = (LinearLayout) view
				.findViewById(R.id.orderItemsListView);
		orderItemsListView.removeAllViews();
		for (OrderItem item : order.getOrderItems()) {
			addLineToOrderItemListView(orderItemsListView);
			View child = createOrderItemView(item);
			LayoutParams param = new LayoutParams(LayoutParams.MATCH_PARENT,
					LayoutParams.WRAP_CONTENT);
			orderItemsListView.addView(child, param);
		}
	}

	private View createOrderItemView(OrderItem item) {
		View child = inflater.inflate(R.layout.shopping_cart_order_item, null);
		setText(child, R.id.goodsName, item.getGoodsName());
		setText(child, R.id.unitPrice, item.getUnitPrice());
		setText(child, R.id.quantity, item.getQuantity());
		return child;
	}

	private void setText(View view, int id, Object data) {
		((TextView) view.findViewById(id)).setText(data.toString());
	}

	private void addLineToOrderItemListView(LinearLayout orderItemsListView) {
		View line = new View(context);
		line.setBackgroundColor(0xFFE5E5E5);
		orderItemsListView.addView(line, new LayoutParams(
				LayoutParams.MATCH_PARENT, 1));
	}

}
