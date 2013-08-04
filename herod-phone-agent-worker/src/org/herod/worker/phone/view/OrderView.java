/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.worker.phone.view;

import java.util.ArrayList;
import java.util.List;

import org.herod.framework.HerodTask;
import org.herod.framework.HerodTask.AsyncTaskable;
import org.herod.framework.ci.InjectViewHelper;
import org.herod.framework.ci.annotation.InjectView;
import org.herod.framework.utils.DateUtils;
import org.herod.worker.phone.MainActivity;
import org.herod.worker.phone.R;
import org.herod.worker.phone.Result;
import org.herod.worker.phone.WorkerContext;
import org.herod.worker.phone.fragment.ConfirmDialogFragment;
import org.herod.worker.phone.fragment.ConfirmDialogFragment.OnOkButtonClickListener;
import org.herod.worker.phone.fragment.PlaceInfoDialogFragment;
import org.herod.worker.phone.model.Order;
import org.herod.worker.phone.model.OrderItem;
import org.herod.worker.phone.view.OrderItemView.GoodsQuantityChangedListener;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
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

	private Handler handler;

	private OrderEditor orderEditor;

	private List<OrderItemView> orderItemViews;

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
		orderEditor = new OrderEditor(order);
		setText(R.id.serialNumber, order.getSerialNumber());
		setText(R.id.submitTime,
				DateUtils.format("MM-dd HH:mm", order.getSubmitTime()));
		orderItemsContainer.removeAllViews();
		setText(R.id.shopName, order.getShopName());
		setText(R.id.buyerName, order.getBuyerName());
		setText(R.id.comment, order.getComment());

		setText(R.id.shopTips, createShopTips(order));
		summationView = new OrderItemView(getContext());
		summationView.setCanEdit(false);
		summationView.setGoodsName("合计");
		updateOrderSummationInfo();
		orderItemViews = new ArrayList<OrderItemView>();

		for (OrderItem item : order.getOrderItems()) {
			addLineToOrderItemListView(orderItemsContainer);
			OrderItemView child = new OrderItemView(getContext());
			child.setOrderEditor(orderEditor);
			child.setOrderAndOrderItem(order, item);
			child.setGoodsQuantityChangedListener(this);
			LayoutParams param = new LayoutParams(LayoutParams.MATCH_PARENT,
					LayoutParams.WRAP_CONTENT);
			orderItemViews.add(child);
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
			onCancelEditButtonClick();
		} else if (v.getId() == R.id.confirmButton) {
			onConfirmEditButtonClick();
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
		new HerodTask<Object, Result>(new AsyncTaskable<Object, Result>() {
			public Result runOnBackground(Object... params) {
				return WorkerContext.getWorkerService().acceptOrder(
						order.getSerialNumber());
			}

			@Override
			public void onPostExecute(Result result) {
				String message;
				if (result != null && result.isSuccess()) {
					handler.sendMessage(handler
							.obtainMessage(MainActivity.MESSAGE_KEY_REFRESH_ORDER_LIST));
					message = "成功受理订单！";
				} else {
					message = "受理订单失败，请重试！";
				}
				Toast.makeText(getContext(), message, Toast.LENGTH_SHORT)
						.show();
			}
		}).execute();

	}

	private void onCancelEditButtonClick() {
		disableOperationButtons();
		Order order = orderEditor.restore();
		setOrder(order);
	}

	private void onConfirmEditButtonClick() {
		disableOperationButtons();
	}

	private void disableOperationButtons() {
		setVisibility(View.VISIBLE, R.id.acceptOrderButton,
				R.id.editOrderButton, R.id.deleteOrderButton);
		setVisibility(View.GONE, R.id.cancelButton, R.id.confirmButton);
		for (OrderItemView orderItemView : orderItemViews) {
			orderItemView.disableEditButtons();
		}
	}

	private void onEditOrderButtonClick() {
		setVisibility(View.GONE, R.id.acceptOrderButton, R.id.editOrderButton,
				R.id.deleteOrderButton);
		setVisibility(View.VISIBLE, R.id.cancelButton, R.id.confirmButton);
		for (OrderItemView orderItemView : orderItemViews) {
			orderItemView.enableEditButtons();
		}
	}

	public void setHandler(Handler handler) {
		this.handler = handler;
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
