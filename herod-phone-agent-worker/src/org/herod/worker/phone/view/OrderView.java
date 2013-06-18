/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.worker.phone.view;

import org.herod.framework.ci.InjectViewHelper;
import org.herod.framework.ci.annotation.InjectView;
import org.herod.framework.utils.DateUtils;
import org.herod.worker.phone.R;
import org.herod.worker.phone.fragment.ConfirmDialogFragment;
import org.herod.worker.phone.fragment.ConfirmDialogFragment.OnOkButtonClickListener;
import org.herod.worker.phone.model.Order;
import org.herod.worker.phone.model.OrderItem;
import org.herod.worker.phone.view.OrderItemView.GoodsQuantityChangedListener;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
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
	@InjectView(R.id.orderItemsListView)
	private LinearLayout orderItemsContainer;

	@InjectView(R.id.serialNumber)
	private TextView serialNumberView;
	@InjectView(R.id.submitTime)
	private TextView submitTimeView;

	private Order order;
	private OrderItemView summationView;
	private FragmentActivity activity;

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
		if (context instanceof FragmentActivity) {
			this.activity = (FragmentActivity) context;
		}
		LayoutInflater.from(getContext()).inflate(R.layout.order, this);
		new InjectViewHelper().injectViews(this);
	}

	@Override
	public void onGoodsQuantityChanged() {
		updateOrderSummationInfo();
	}

	public void setOrder(Order order) {
		this.order = order;
		setText(serialNumberView, order.getSerialNumber());
		setText(submitTimeView,
				DateUtils.format("MM-dd HH:mm", order.getSubmitTime()));
		orderItemsContainer.removeAllViews();
		setText(R.id.shopName, order.getShopName());
		setText(R.id.buyerName, order.getBuyerName());

		setText(R.id.shopTips, createShopTips(order));
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

	private String createShopTips(Order order) {
		StringBuilder sb = new StringBuilder();
		double charge = order.getShopMinChargeForFreeDelivery();
		if (charge > 0) {
			sb.append("消费满").append(charge).append("免跑腿费");
		} else {
			sb.append("免跑腿费");
		}
		return sb.toString();
	}

	private void updateOrderSummationInfo() {
		summationView.setQuantity(order.getTotalQuantity());
		summationView.setUnitPrice(order.getTotalAmount());
		setText(R.id.costOfRunErrands, order.getCostOfRunErrands());
		setText(R.id.totalWithCostOfRunErrands,
				Double.toString(order.getTotalAmountWithCostOfRunErrands()));
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

	private void setText(int id, Object data) {
		if (data != null) {
			((TextView) findViewById(id)).setText(data.toString());
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
			// TODO
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
