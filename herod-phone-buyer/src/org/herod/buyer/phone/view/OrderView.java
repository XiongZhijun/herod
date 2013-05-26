/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.buyer.phone.view;

import java.util.Map;

import org.herod.buyer.phone.BuyerContext;
import org.herod.buyer.phone.BuyerService;
import org.herod.buyer.phone.R;
import org.herod.buyer.phone.model.Order;
import org.herod.buyer.phone.model.OrderItem;
import org.herod.buyer.phone.view.OrderItemView.GoodsQuantityChangedListener;
import org.herod.framework.ci.InjectViewHelper;
import org.herod.framework.ci.annotation.InjectView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 
 * 
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 * 
 */
public class OrderView extends LinearLayout implements
		GoodsQuantityChangedListener {
	@InjectView(R.id.shopName)
	private TextView shopNameView;
	@InjectView(R.id.orderItemsListView)
	private LinearLayout orderItemsContainer;
	@InjectView(R.id.totalAmount)
	private TextView totalAmountView;
	@InjectView(R.id.costOfRunErrands)
	private TextView costOfRunErrandsView;
	private Order order;
	private BuyerService buyerService;
	private OrderItemView summationView;

	public OrderView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView();
	}

	public OrderView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}

	public OrderView(Context context) {
		super(context);
		initView();
	}

	private void initView() {
		LayoutInflater.from(getContext()).inflate(R.layout.shopping_cart_order,
				this);
		new InjectViewHelper().injectViews(this);
		buyerService = BuyerContext.getBuyerService();
	}

	@Override
	public void onGoodsQuantityChanged() {
		updateOrderSummationInfo();
	}

	public void setOrder(Order order) {
		this.order = order;
		orderItemsContainer.removeAllViews();
		Map<String, Object> shop = buyerService.findShopById(order.getShopId());
		setText(shopNameView, shop.get("name"));

		summationView = new OrderItemView(getContext());
		summationView.disableButtons();
		summationView.setGoodsName("合计");
		updateOrderSummationInfo();

		for (OrderItem item : order.getOrderItems()) {
			addLineToOrderItemListView(orderItemsContainer);
			OrderItemView child = new OrderItemView(getContext());
			child.setOrderAndOrderItem(order, item);
			child.setGoodsQuantityChangedListener(this);
			LayoutParams param = new LayoutParams(LayoutParams.MATCH_PARENT,
					LayoutParams.WRAP_CONTENT);
			orderItemsContainer.addView(child, param);
		}
		addLineToOrderItemListView(orderItemsContainer);
		LayoutParams param = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);
		orderItemsContainer.addView(summationView, param);
	}

	private void updateOrderSummationInfo() {
		if (summationView != null && order != null) {
			summationView.setQuantity(order.getTotalQuantity());
			summationView.setUnitPrice(order.getTotalAmount());
			totalAmountView.setText(Double.toString(order.getTotalAmount()));
		}
	}

	private void addLineToOrderItemListView(LinearLayout orderItemsListView) {
		View line = new View(getContext());
		line.setBackgroundColor(0xFFE5E5E5);
		orderItemsListView.addView(line, new LayoutParams(
				LayoutParams.MATCH_PARENT, 1));
	}

	private void setText(View view, Object data) {
		((TextView) view).setText(data.toString());
	}

}
