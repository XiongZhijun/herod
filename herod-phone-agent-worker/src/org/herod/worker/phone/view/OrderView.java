/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.worker.phone.view;

import org.herod.framework.ci.InjectViewHelper;
import org.herod.framework.ci.annotation.InjectView;
import org.herod.framework.utils.DateUtils;
import org.herod.worker.phone.R;
import org.herod.worker.phone.fragment.ConfirmDialogFragment;
import org.herod.worker.phone.fragment.PlaceInfoDialogFragment;
import org.herod.worker.phone.fragment.ConfirmDialogFragment.OnOkButtonClickListener;
import org.herod.worker.phone.model.Order;
import org.herod.worker.phone.model.OrderItem;
import org.herod.worker.phone.view.OrderItemView.GoodsQuantityChangedListener;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * 
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 * 
 */
public class OrderView extends LinearLayout implements
		GoodsQuantityChangedListener, OnClickListener {
	@InjectView(R.id.orderItemsListView)
	private LinearLayout orderItemsContainer;

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
		findViewById(R.id.editOrderButton).setOnClickListener(this);
		findViewById(R.id.acceptOrderButton).setOnClickListener(this);
		findViewById(R.id.deleteOrderButton).setOnClickListener(this);
		findViewById(R.id.cancelButton).setOnClickListener(this);
		findViewById(R.id.confirmButton).setOnClickListener(this);
		findViewById(R.id.shopName).setOnClickListener(this);
		findViewById(R.id.buyerName).setOnClickListener(this);
	}

	@Override
	public void onGoodsQuantityChanged() {
		updateOrderSummationInfo();
	}

	public void setOrder(Order order) {
		this.order = order;
		setText(R.id.serialNumber, order.getSerialNumber());
		setText(R.id.submitTime,
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
		summationView.setSellingPrice(order.getTotalAmount());
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

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.editOrderButton) {
			onEditOrderButtonClick();
		} else if (v.getId() == R.id.acceptOrderButton) {
			onAcceptOrderButtonClick();
		} else if (v.getId() == R.id.deleteOrderButton) {
			onDeleteOrderButtonClick();
		} else if (v.getId() == R.id.cancelButton) {
			onCancelButtonClick();
		} else if (v.getId() == R.id.confirmButton) {
			onConfirmButtonClick();
		} else if (v.getId() == R.id.shopName) {
			onShopNameClickListener();
		} else if (v.getId() == R.id.buyerName) {
			onBuyerNameClickListener();
		}
	}

	private void onBuyerNameClickListener() {
		PlaceInfoDialogFragment fragment = new PlaceInfoDialogFragment();
		Bundle args = new Bundle();
		args.putString("phone", order.getBuyerPhone());
		args.putString("locationName", order.getDeliveryAddress().getAddress());
		fragment.setArguments(args);
		fragment.show(activity.getSupportFragmentManager(), null);
	}

	private void onShopNameClickListener() {
		PlaceInfoDialogFragment fragment = new PlaceInfoDialogFragment();
		Bundle args = new Bundle();
		args.putString("phone", order.getShopPhone());
		args.putString("locationName", order.getShopName());
		fragment.setArguments(args);
		fragment.show(activity.getSupportFragmentManager(), null);

	}

	private void onDeleteOrderButtonClick() {
		ConfirmDialogFragment.showDialog(activity, "确定删除该订单？",
				new OnDeleteOrderByOkListener());
	}

	private class OnDeleteOrderByOkListener implements OnOkButtonClickListener {
		@Override
		public void onOk() {
			// TODO
			Toast.makeText(getContext(), "成功删除订单！", Toast.LENGTH_SHORT).show();
		}
	}

	private void onAcceptOrderButtonClick() {
		// TODO Auto-generated method stub

	}

	private void onCancelButtonClick() {
		enableOperationButtons();
		// TODO
	}

	private void onConfirmButtonClick() {
		enableOperationButtons();
		// TODO
	}

	private void enableOperationButtons() {
		setVisibility(View.VISIBLE, R.id.acceptOrderButton,
				R.id.editOrderButton, R.id.deleteOrderButton);
		setVisibility(View.GONE, R.id.cancelButton, R.id.confirmButton);
		for (int i = 0; i < orderItemsContainer.getChildCount(); i++) {
			View view = orderItemsContainer.getChildAt(i);
			setVisibility(View.INVISIBLE, view.findViewById(R.id.addButton));
			setVisibility(View.INVISIBLE, view.findViewById(R.id.reduceButton));
		}
	}

	private void onEditOrderButtonClick() {
		setVisibility(View.GONE, R.id.acceptOrderButton, R.id.editOrderButton,
				R.id.deleteOrderButton);
		setVisibility(View.VISIBLE, R.id.cancelButton, R.id.confirmButton);
		for (int i = 0; i < orderItemsContainer.getChildCount(); i++) {
			View view = orderItemsContainer.getChildAt(i);
			setVisibility(View.VISIBLE, view.findViewById(R.id.addButton));
			setVisibility(View.VISIBLE, view.findViewById(R.id.reduceButton));
		}
	}

	private void setText(int id, Object data) {
		if (data != null) {
			((TextView) findViewById(id)).setText(data.toString());
		}
	}

	private void setVisibility(int visibility, int... ids) {
		for (int id : ids) {
			View view = findViewById(id);
			setVisibility(visibility, view);
		}
	}

	private void setVisibility(int visibility, View... views) {
		for (View view : views) {
			if (view != null) {
				view.setVisibility(visibility);
			}
		}
	}

}
