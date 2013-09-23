/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.worker.phone.view;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static org.herod.order.common.Constants.COMMENT;
import static org.herod.order.common.Constants.COST_OF_RUN_ERRANDS;
import static org.herod.order.common.Constants.MM_DD_HH_MM;
import static org.herod.order.common.Constants.SERIAL_NUMBER;
import static org.herod.order.common.Constants.SHOP_NAME;
import static org.herod.order.common.Constants.SHOP_TIPS;
import static org.herod.order.common.Constants.STATUS;
import static org.herod.order.common.Constants.SUBMIT_TIME;
import static org.herod.order.common.Constants.TOTAL_AMOUNT_WITH_COST_OF_RUN_ERRANDS;
import static org.herod.order.common.Constants.TOTAL_QUANTITY;
import static org.herod.worker.phone.Constants.BUYER_ADDRESS;
import static org.herod.worker.phone.R.id.acceptOrderButton;
import static org.herod.worker.phone.R.id.addNewItemButton;
import static org.herod.worker.phone.R.id.buyerName;
import static org.herod.worker.phone.R.id.cancelEditButton;
import static org.herod.worker.phone.R.id.cancelOrderButton;
import static org.herod.worker.phone.R.id.comment;
import static org.herod.worker.phone.R.id.completeOrderButton;
import static org.herod.worker.phone.R.id.confirmEditButton;
import static org.herod.worker.phone.R.id.costOfRunErrands;
import static org.herod.worker.phone.R.id.editOrderButton;
import static org.herod.worker.phone.R.id.rejectOrderButton;
import static org.herod.worker.phone.R.id.route;
import static org.herod.worker.phone.R.id.serialNumber;
import static org.herod.worker.phone.R.id.shopName;
import static org.herod.worker.phone.R.id.shopTips;
import static org.herod.worker.phone.R.id.status;
import static org.herod.worker.phone.R.id.submitTime;
import static org.herod.worker.phone.R.id.totalQuantity;
import static org.herod.worker.phone.R.id.totalWithCostOfRunErrands;

import java.util.ArrayList;
import java.util.List;

import org.herod.framework.HerodTask.BackgroudRunnable;
import org.herod.framework.ViewFindable;
import org.herod.framework.ci.InjectViewHelper;
import org.herod.framework.ci.annotation.InjectView;
import org.herod.framework.form.FormHelper;
import org.herod.framework.form.FormHelper.FormHelperBuilder;
import org.herod.framework.utils.StringUtils;
import org.herod.framework.utils.TextViewUtils;
import org.herod.framework.utils.ToastUtils;
import org.herod.framework.utils.ViewUtils;
import org.herod.order.common.model.Order;
import org.herod.order.common.model.OrderItem;
import org.herod.order.common.model.Result;
import org.herod.worker.phone.GoodsListActivity;
import org.herod.worker.phone.MapActivity;
import org.herod.worker.phone.MapActivity.MapType;
import org.herod.worker.phone.R;
import org.herod.worker.phone.WorkerContext;
import org.herod.worker.phone.fragment.AsyncTaskConfirmDialogFragment;
import org.herod.worker.phone.fragment.CancelOrderDialogFragment;
import org.herod.worker.phone.fragment.OrderListFragment;
import org.herod.worker.phone.fragment.PlaceInfoDialogFragment;
import org.herod.worker.phone.fragment.UpdateOrderDialogFragment;
import org.herod.worker.phone.handler.HerodHandler;
import org.herod.worker.phone.view.OrderItemView.GoodsQuantityChangedListener;

import android.content.Context;
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

	private static final int[] FORM_TO = new int[] { serialNumber, submitTime,
			shopName, buyerName, comment, status, shopTips, costOfRunErrands,
			totalWithCostOfRunErrands, totalQuantity };
	private static final String[] FORM_FROM = new String[] { SERIAL_NUMBER,
			SUBMIT_TIME, SHOP_NAME, BUYER_ADDRESS, COMMENT, STATUS, SHOP_TIPS,
			COST_OF_RUN_ERRANDS, TOTAL_AMOUNT_WITH_COST_OF_RUN_ERRANDS,
			TOTAL_QUANTITY };
	private static final int[] NEED_SET_ON_CLICK_LISTENER_VIEW_IDS = new int[] {
			editOrderButton, acceptOrderButton, completeOrderButton,
			cancelOrderButton, cancelEditButton, confirmEditButton,
			addNewItemButton, shopName, buyerName, route, rejectOrderButton };

	private static FormHelper formHelper = new FormHelperBuilder(FORM_FROM,
			FORM_TO, Order.class).addDateSerializer(MM_DD_HH_MM).build();

	@InjectView(R.id.orderItemsListView)
	private LinearLayout orderItemsContainer;
	private FragmentActivity activity;
	private OrderListFragment fragment;
	private Handler handler;

	private Order order;
	private List<OrderItemView> orderItemViews = new ArrayList<OrderItemView>();

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

		ViewUtils.setOnClickListener(this, NEED_SET_ON_CLICK_LISTENER_VIEW_IDS);

	}

	@Override
	public void onGoodsQuantityChanged() {
		TextViewUtils.setText(this, R.id.costOfRunErrands,
				order.getCostOfRunErrands());
		TextViewUtils.setText(this, R.id.totalWithCostOfRunErrands,
				Double.toString(order.getTotalAmountWithCostOfRunErrands()));
	}

	public void setOrder(Order order) {
		this.order = order;
		formHelper.setValues(order, this);

		orderItemsContainer.removeAllViews();
		orderItemViews.clear();
		List<OrderItem> orderItems = new ArrayList<OrderItem>();
		orderItems.addAll(order.getOrderItems());
		orderItems.addAll(OrderEditorManager.getInstance().getNewOrderItems());

		for (OrderItem item : orderItems) {
			addLineToOrderItemListView(orderItemsContainer);
			OrderItemView orderItemView = createOrderItemView(item);
			orderItemViews.add(orderItemView);
			orderItemsContainer.addView(orderItemView, new LayoutParams(
					MATCH_PARENT, WRAP_CONTENT));
		}

		OrderViewButtonsTools.refreshButtonStatus(order, this, orderItemViews);
	}

	private OrderItemView createOrderItemView(OrderItem item) {
		OrderItemView orderItemView = new OrderItemView(getContext());
		orderItemView.setOrderAndOrderItem(item);
		orderItemView.setGoodsQuantityChangedListener(this);
		return orderItemView;
	}

	private void addLineToOrderItemListView(LinearLayout orderItemsListView) {
		View line = new View(getContext());
		line.setBackgroundColor(0xFFE5E5E5);
		orderItemsListView.addView(line, new LayoutParams(MATCH_PARENT, 1));
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
		} else if (v.getId() == R.id.addNewItemButton) {
			GoodsListActivity.show(fragment, order.getShopId(),
					order.getShopName(), order.getSerialNumber());
		} else if (v.getId() == R.id.shopName) {
			onShopNameClickListener();
		} else if (v.getId() == R.id.buyerName) {
			onBuyerNameClickListener();
		} else if (v.getId() == R.id.completeOrderButton) {
			onCompleteOrderButtonClick();
		} else if (v.getId() == R.id.rejectOrderButton) {
			onRejectOrderButtonClick();
		} else if (v.getId() == R.id.route) {
			MapActivity.showMapActivity(getContext(), order);
		}
	}

	private void onBuyerNameClickListener() {
		PlaceInfoDialogFragment.showFragment(activity, order,
				order.getDeliveryAddress(), order.getBuyerPhone(),
				MapType.Buyer);
	}

	private void onShopNameClickListener() {
		PlaceInfoDialogFragment.showFragment(activity, order,
				order.getShopAddress(), order.getShopPhone(), MapType.Shop);
	}

	private void onCancelOrderButtonClick() {
		CancelOrderDialogFragment.showDialog(activity, handler,
				order.getSerialNumber());
	}

	private void onRejectOrderButtonClick() {
		AsyncTaskConfirmDialogFragment.show(activity, handler,
				new BackgroudRunnable<Object, Result>() {
					public Result runOnBackground(Object... params) {
						return WorkerContext.getWorkerService().rejectOrder(
								order.getSerialNumber(), StringUtils.EMPTY);
					}
				}, "确定拒绝并回退该订单？", "成功拒绝并回退订单！", "拒绝回退订单失败，请重试！");
	}

	private void onCompleteOrderButtonClick() {
		AsyncTaskConfirmDialogFragment.show(activity, handler,
				new BackgroudRunnable<Object, Result>() {
					public Result runOnBackground(Object... params) {
						return WorkerContext.getWorkerService().completeOrder(
								order.getSerialNumber());
					}
				}, "确定完成该订单？", "成功完成订单！", "完成订单失败，请重试！");

	}

	private void onAcceptOrderButtonClick() {
		AsyncTaskConfirmDialogFragment.show(activity, handler,
				new BackgroudRunnable<Object, Result>() {
					public Result runOnBackground(Object... params) {
						return WorkerContext.getWorkerService().acceptOrder(
								order.getSerialNumber());
					}
				}, "确定受理该订单？", "成功受理订单！", "受理订单失败，请重试！");
	}

	private void onConfirmEditButtonClick() {
		UpdateOrderDialogFragment.showDialog(activity, handler,
				order.getComment());
	}

	private void onCancelEditButtonClick() {
		OrderEditorManager.getInstance().stopEdit();
		setOrder(order);
	}

	private void onEditOrderButtonClick() {
		boolean startEdit = OrderEditorManager.getInstance().startEdit(order);
		if (!startEdit) {
			ToastUtils.showToast("不能同时对多个订单进行修改！", Toast.LENGTH_SHORT);
			return;
		}
		OrderViewButtonsTools.refreshButtonStatus(order, this, orderItemViews);
	}

	public void setHandler(Handler handler) {
		this.handler = new HerodHandler(handler);
	}

	public void setFragment(OrderListFragment fragment) {
		this.fragment = fragment;
	}

}
