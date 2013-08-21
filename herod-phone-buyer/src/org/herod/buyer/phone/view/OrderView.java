/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.buyer.phone.view;

import org.herod.buyer.phone.AbstractOrdersActivity;
import org.herod.buyer.phone.R;
import org.herod.buyer.phone.ShoppingCartCache;
import org.herod.buyer.phone.fragments.ConfirmDialogFragment;
import org.herod.buyer.phone.fragments.ConfirmDialogFragment.OnOkButtonClickListener;
import org.herod.buyer.phone.view.OrderItemView.GoodsQuantityChangedListener;
import org.herod.framework.ViewFindable;
import org.herod.framework.ci.InjectViewHelper;
import org.herod.framework.ci.annotation.InjectView;
import org.herod.framework.utils.DateUtils;
import org.herod.framework.utils.ResourcesUtils;
import org.herod.framework.utils.TextViewUtils;
import org.herod.order.common.model.Order;
import org.herod.order.common.model.OrderItem;
import org.herod.order.common.model.OrderStatus;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
		GoodsQuantityChangedListener, ViewFindable {
	@InjectView(R.id.shopName)
	private TextView shopNameView;
	@InjectView(R.id.shopPhone)
	private TextView shopPhoneView;
	@InjectView(R.id.shopTips)
	private TextView shopTipsView;
	@InjectView(R.id.orderItemsListView)
	private LinearLayout orderItemsContainer;
	@InjectView(R.id.costOfRunErrands)
	private TextView costOfRunErrandsView;
	@InjectView(R.id.totalWithCostOfRunErrands)
	private TextView totalWithCostOfRunErrandsView;

	@InjectView(R.id.serialNumber)
	private TextView serialNumberView;
	@InjectView(R.id.status)
	private TextView statusView;
	@InjectView(R.id.submitTime)
	private TextView submitTimeView;

	private Order order;
	private OrderItemView summationView;
	private AbstractOrdersActivity activity;

	public OrderView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}

	public OrderView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	public OrderView(Context context) {
		super(context);
		initView(context);
	}

	private void initView(Context context) {
		if (context instanceof AbstractOrdersActivity) {
			this.activity = (AbstractOrdersActivity) context;
		}
		LayoutInflater.from(getContext()).inflate(R.layout.shopping_cart_order,
				this);
		new InjectViewHelper().injectViews(this);
		findViewById(R.id.cancelOrderButton).setOnClickListener(
				new CancelOrderListener());
		shopPhoneView.setOnClickListener(new CallPhoneListener());
	}

	@Override
	public void onGoodsQuantityChanged() {
		updateOrderSummationInfo();
	}

	public void setOrder(Order order) {
		this.order = order;
		if (order.getStatus() == OrderStatus.Unsubmit) {
			findViewById(R.id.cancelOrderButton).setVisibility(View.VISIBLE);
			findViewById(R.id.historyInfo).setVisibility(View.GONE);
		} else {
			findViewById(R.id.historyInfo).setVisibility(View.VISIBLE);
			findViewById(R.id.cancelOrderButton).setVisibility(View.INVISIBLE);
			setText(statusView,
					ResourcesUtils.getEnumShowName(order.getStatus()));
			setText(serialNumberView, order.getSerialNumber());
			setText(submitTimeView,
					DateUtils.format("MM-dd HH:mm", order.getSubmitTime()));
		}
		orderItemsContainer.removeAllViews();
		setText(shopNameView, order.getShopName());
		setText(shopPhoneView, order.getShopPhone());
		setText(shopTipsView, createShopTips(order));
		TextViewUtils.setText(this, R.id.status, order.getStatus());
		summationView = new OrderItemView(getContext());
		summationView.disableButtons();
		summationView.setGoodsName("合计");
		updateOrderSummationInfo();

		for (OrderItem item : order.getOrderItems()) {
			addLineToOrderItemListView(orderItemsContainer);
			OrderItemView child = new OrderItemView(getContext());
			child.setOrderAndOrderItem(order, item);
			child.setActivity(activity);
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

	private String createShopTips(Order order) {
		StringBuilder sb = new StringBuilder();
		double charge = order.getShopMinChargeForFreeDelivery();
		if (charge > 0) {
			sb.append("消费满").append(charge).append("免配送费");
		} else {
			sb.append("免配送费");
		}
		return sb.toString();
	}

	private void updateOrderSummationInfo() {
		if (summationView != null && order != null) {
			summationView.setQuantity(order.getTotalQuantity());
			summationView.setUnitPrice(order.getTotalAmount());
			showOrderTotalInfo();
		}
	}

	private void showOrderTotalInfo() {
		double costOfRunErrands = order.getShopCostOfRunErrands();
		double minChargeForFreeDelivery = order
				.getShopMinChargeForFreeDelivery();
		if (order.getTotalAmount() < minChargeForFreeDelivery) {
			order.setCostOfRunErrands(costOfRunErrands);
		} else {
			order.setCostOfRunErrands(0);
		}
		setText(costOfRunErrandsView, order.getCostOfRunErrands());
		totalWithCostOfRunErrandsView.setText(Double.toString(order
				.getTotalAmountWithCostOfRunErrands()));
	}

	private void addLineToOrderItemListView(LinearLayout orderItemsListView) {
		View line = new View(getContext());
		line.setBackgroundColor(0xFFE5E5E5);
		orderItemsListView.addView(line, new LayoutParams(
				LayoutParams.MATCH_PARENT, 1));
	}

	private void setText(View view, Object data) {
		if (data != null) {
			((TextView) view).setText(data.toString());
		}
	}

	private class CancelOrderListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			ConfirmDialogFragment.showDialog(activity, "确定删除该订单？",
					new OnDeleteOrderByOkListener());
		}

	}

	private class OnDeleteOrderByOkListener implements OnOkButtonClickListener {
		@Override
		public void onOk() {
			ShoppingCartCache.getInstance().removeOrder(order.getShopId());
			if (activity != null) {
				activity.refreshOrders();
			}
		}
	}

	private class CallPhoneListener implements OnClickListener {
		public void onClick(View v) {
			String phone = order.getShopPhone();
			Uri uri = Uri.parse("tel:" + phone);
			Intent it = new Intent(Intent.ACTION_DIAL, uri);
			activity.startActivity(it);
		}

	}

}
