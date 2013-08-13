/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.worker.phone.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.herod.framework.HerodTask.AsyncTaskable;
import org.herod.framework.ViewFindable;
import org.herod.framework.ci.InjectViewHelper;
import org.herod.framework.ci.annotation.InjectView;
import org.herod.framework.utils.DateUtils;
import org.herod.framework.utils.TextViewUtils;
import org.herod.worker.phone.AgentWorkerTask;
import org.herod.worker.phone.MainActivity;
import org.herod.worker.phone.MapActivity;
import org.herod.worker.phone.R;
import org.herod.worker.phone.Result;
import org.herod.worker.phone.WorkerContext;
import org.herod.worker.phone.fragment.CancelOrderDialogFragment;
import org.herod.worker.phone.fragment.ConfirmDialogFragment;
import org.herod.worker.phone.fragment.FormFragment.OnCancelButtonClickListener;
import org.herod.worker.phone.fragment.FormFragment.OnOkButtonClickListener;
import org.herod.worker.phone.fragment.PlaceInfoDialogFragment;
import org.herod.worker.phone.fragment.UpdateOrderDialogFragment;
import org.herod.worker.phone.model.Address;
import org.herod.worker.phone.model.Order;
import org.herod.worker.phone.model.OrderItem;
import org.herod.worker.phone.model.OrderUpdateInfo;
import org.herod.worker.phone.view.OrderItemView.GoodsQuantityChangedListener;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * 
 * 
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 * 
 */
public class OrderView extends LinearLayout implements
		GoodsQuantityChangedListener, OnClickListener, ViewFindable {
	@InjectView(R.id.orderItemsListView)
	private LinearLayout orderItemsContainer;

	private Order order;
	private OrderItemView summationView;
	private FragmentActivity activity;

	private Handler handler;
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
		findViewById(R.id.completeOrderButton).setOnClickListener(this);
		findViewById(R.id.cancelOrderButton).setOnClickListener(this);
		findViewById(R.id.cancelEditButton).setOnClickListener(this);
		findViewById(R.id.confirmEditButton).setOnClickListener(this);
		findViewById(R.id.shopName).setOnClickListener(this);
		findViewById(R.id.buyerName).setOnClickListener(this);
		findViewById(R.id.route).setOnClickListener(this);

	}

	@Override
	public void onGoodsQuantityChanged() {
		updateOrderSummationInfo();
	}

	public void setOrder(Order order) {
		this.order = order;
		handleButtons(order);
		TextViewUtils.setText(this, R.id.serialNumber, order.getSerialNumber());
		TextViewUtils.setText(this, R.id.submitTime,
				DateUtils.format("MM-dd HH:mm", order.getSubmitTime()));
		orderItemsContainer.removeAllViews();
		TextViewUtils.setText(this, R.id.shopName, order.getShopName());
		TextViewUtils.setText(this, R.id.buyerName, order.getBuyerName());
		TextViewUtils.setText(this, R.id.comment, order.getComment());

		TextViewUtils.setText(this, R.id.shopTips, createShopTips(order));
		summationView = new OrderItemView(getContext());
		summationView.setCanEdit(false);
		summationView.setGoodsName("合计");
		updateOrderSummationInfo();
		orderItemViews = new ArrayList<OrderItemView>();

		for (OrderItem item : order.getOrderItems()) {
			addLineToOrderItemListView(orderItemsContainer);
			OrderItemView child = new OrderItemView(getContext());
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

	private void handleButtons(Order order) {
		switch (order.getStatus()) {
		case Submitted:
			findViewById(R.id.acceptOrderButton).setVisibility(View.VISIBLE);
			findViewById(R.id.completeOrderButton).setVisibility(View.GONE);
			break;
		case Acceptted:
			findViewById(R.id.acceptOrderButton).setVisibility(View.GONE);
			findViewById(R.id.completeOrderButton).setVisibility(View.VISIBLE);
		default:
			break;
		}
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
		TextViewUtils.setText(this, R.id.costOfRunErrands,
				order.getCostOfRunErrands());
		TextViewUtils.setText(this, R.id.totalWithCostOfRunErrands,
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
		} else if (v.getId() == R.id.cancelOrderButton) {
			onCancelOrderButtonClick();
		} else if (v.getId() == R.id.cancelEditButton) {
			onCancelEditButtonClick();
		} else if (v.getId() == R.id.confirmEditButton) {
			onConfirmEditButtonClick();
		} else if (v.getId() == R.id.shopName) {
			onShopNameClickListener();
		} else if (v.getId() == R.id.buyerName) {
			onBuyerNameClickListener();
		} else if (v.getId() == R.id.completeOrderButton) {
			onCompleteOrderButtonClick();
		} else if (v.getId() == R.id.route) {
			Intent intent = new Intent(getContext(), MapActivity.class);
			intent.putExtra("destAddress", order.getDeliveryAddress());
			ArrayList<Address> wpAddresses = new ArrayList<Address>();
			wpAddresses.add(order.getShopAddress());
			intent.putExtra("wpAddresses", wpAddresses);
			getContext().startActivity(intent);
		}
	}

	private void onBuyerNameClickListener() {
		PlaceInfoDialogFragment fragment = new PlaceInfoDialogFragment();
		Bundle args = new Bundle();
		args.putString("phone", order.getBuyerPhone());
		args.putString("locationName", order.getDeliveryAddress().getAddress());
		args.putSerializable("address", order.getDeliveryAddress());
		fragment.setArguments(args);
		fragment.show(activity.getSupportFragmentManager(), null);
	}

	private void onShopNameClickListener() {
		PlaceInfoDialogFragment fragment = new PlaceInfoDialogFragment();
		Bundle args = new Bundle();
		args.putString("phone", order.getShopPhone());
		args.putString("locationName", order.getShopAddress().getAddress());
		args.putSerializable("address", order.getShopAddress());
		fragment.setArguments(args);
		fragment.show(activity.getSupportFragmentManager(), null);
	}

	private void onCancelOrderButtonClick() {
		CancelOrderDialogFragment.showDialog(activity,
				new OnCancelOrderByOkListener());
	}

	private class OnCancelOrderByOkListener implements OnOkButtonClickListener {
		@Override
		public void onOk(final Map<String, String> formDatas) {
			new AgentWorkerTask<Object, Result>(getContext(),
					new AbstracAsyncTaskable("取消订单成功！", "取消订单失败，请重试！") {
						public Result runOnBackground(Object... params) {
							return WorkerContext.getWorkerService()
									.cancelOrder(order.getSerialNumber(),
											formDatas.get("reason"));
						}
					}).execute();
		}
	}

	private void onCompleteOrderButtonClick() {
		ConfirmDialogFragment.showDialog(activity, "确定完成该订单？",
				new OnCompleteOrderByOkListener());

	}

	class OnCompleteOrderByOkListener
			implements
			org.herod.worker.phone.fragment.ConfirmDialogFragment.OnOkButtonClickListener {
		public void onOk() {
			new AgentWorkerTask<Object, Result>(getContext(),
					new AbstracAsyncTaskable("成功完成订单！", "完成订单失败，请重试！") {
						public Result runOnBackground(Object... params) {
							return WorkerContext.getWorkerService()
									.completeOrder(order.getSerialNumber());
						}
					}).execute();
		}
	}

	private void onAcceptOrderButtonClick() {
		new AgentWorkerTask<Object, Result>(getContext(),
				new AbstracAsyncTaskable("成功受理订单！", "受理订单失败，请重试！") {
					public Result runOnBackground(Object... params) {
						return WorkerContext.getWorkerService().acceptOrder(
								order.getSerialNumber());
					}
				}).execute();

	}

	private void onCancelEditButtonClick() {
		disableOperationButtons();
		setOrder(order);
	}

	private void onConfirmEditButtonClick() {
		UpdateOrderDialogFragment.showDialog(activity, order.getComment(),
				new OnUpdateOrderByOkListener(),
				new OnCancelButtonClickListener() {
					public void onCancel() {
						disableOperationButtons();
					}
				});
	}

	class OnUpdateOrderByOkListener implements OnOkButtonClickListener {
		public void onOk(final Map<String, String> formDatas) {
			new AgentWorkerTask<Object, Result>(getContext(),
					new AbstracAsyncTaskable("修改订单成功！", "修改订单失败，请重试！") {
						public Result runOnBackground(Object... params) {
							OrderEditor orderEditor = OrderEditorManager
									.getInstance().findOrderEditor(
											order.getSerialNumber());
							if (orderEditor == null) {
								return null;
							}
							OrderUpdateInfo updateInfo = orderEditor
									.toUpdateInfo(formDatas.get("comment"),
											formDatas.get("reason"));
							return WorkerContext.getWorkerService()
									.updateOrder(updateInfo);
						}

						public void onPostExecute(Result result) {
							super.onPostExecute(result);
							if (result != null && result.isSuccess()) {
								OrderEditorManager.getInstance()
										.removeOrderEditor(
												order.getSerialNumber());
							}
							// TODO 失败之后要考虑：1、order
							// view已经恢复到原始状态了，需要根据更新的状态进行界面更新。
						}
					}).execute();
		}

	}

	private void disableOperationButtons() {
		setVisibility(View.VISIBLE, R.id.acceptOrderButton,
				R.id.editOrderButton, R.id.cancelOrderButton,
				R.id.completeOrderButton);
		setVisibility(View.GONE, R.id.cancelEditButton, R.id.confirmEditButton);
		handleButtons(order);
		for (OrderItemView orderItemView : orderItemViews) {
			orderItemView.disableEditButtons();
		}
		OrderEditorManager.getInstance().removeOrderEditor(
				order.getSerialNumber());
	}

	private void onEditOrderButtonClick() {
		setVisibility(View.GONE, R.id.acceptOrderButton, R.id.editOrderButton,
				R.id.cancelOrderButton, R.id.completeOrderButton);
		setVisibility(View.VISIBLE, R.id.cancelEditButton,
				R.id.confirmEditButton);
		for (OrderItemView orderItemView : orderItemViews) {
			orderItemView.enableEditButtons();
		}

		OrderEditorManager.getInstance().addOrderEditor(order);
	}

	public void setHandler(Handler handler) {
		this.handler = handler;
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

	abstract class AbstracAsyncTaskable implements
			AsyncTaskable<Object, Result> {
		private String successMessage;
		private String failedMessage;

		public AbstracAsyncTaskable(String successMessage, String failedMessage) {
			super();
			this.successMessage = successMessage;
			this.failedMessage = failedMessage;
		}

		@Override
		public void onPostExecute(Result result) {
			String message;
			if (result != null && result.isSuccess()) {
				handler.sendMessage(handler
						.obtainMessage(MainActivity.MESSAGE_KEY_REFRESH_ORDER_LIST));
				message = successMessage;
			} else {
				message = failedMessage;
			}
			Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
		}
	}

}
