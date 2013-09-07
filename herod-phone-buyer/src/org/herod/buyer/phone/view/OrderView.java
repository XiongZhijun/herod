/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.buyer.phone.view;

import static org.herod.buyer.phone.R.id.comment;
import static org.herod.buyer.phone.R.id.costOfRunErrands;
import static org.herod.buyer.phone.R.id.serialNumber;
import static org.herod.buyer.phone.R.id.shopName;
import static org.herod.buyer.phone.R.id.shopTips;
import static org.herod.buyer.phone.R.id.status;
import static org.herod.buyer.phone.R.id.submitTime;
import static org.herod.buyer.phone.R.id.totalQuantity;
import static org.herod.buyer.phone.R.id.totalWithCostOfRunErrands;
import static org.herod.order.common.Constants.COMMENT;
import static org.herod.order.common.Constants.COST_OF_RUN_ERRANDS;
import static org.herod.order.common.Constants.MM_DD_HH_MM;
import static org.herod.order.common.Constants.SERIAL_NUMBER;
import static org.herod.order.common.Constants.SHOP_NAME;
import static org.herod.order.common.Constants.SHOP_TIPS;
import static org.herod.order.common.Constants.STATUS;
import static org.herod.order.common.Constants.SUBMIT_TIME;
import static org.herod.order.common.Constants.TIMESTAMP;
import static org.herod.order.common.Constants.TOTAL_AMOUNT_WITH_COST_OF_RUN_ERRANDS;
import static org.herod.order.common.Constants.TOTAL_QUANTITY;

import org.herod.buyer.phone.AbstractOrdersActivity;
import org.herod.buyer.phone.BuyerContext;
import org.herod.buyer.phone.GoodsListActivity;
import org.herod.buyer.phone.R;
import org.herod.buyer.phone.ShoppingCartCache;
import org.herod.buyer.phone.fragments.ConfirmDialogFragment;
import org.herod.buyer.phone.fragments.ConfirmDialogFragment.OnOkButtonClickListener;
import org.herod.buyer.phone.view.OrderItemView.GoodsQuantityChangedListener;
import org.herod.framework.MapWrapper;
import org.herod.framework.ViewFindable;
import org.herod.framework.ci.InjectViewHelper;
import org.herod.framework.ci.annotation.InjectView;
import org.herod.framework.form.FormHelper;
import org.herod.framework.form.FormHelper.FormHelperBuilder;
import org.herod.framework.utils.TextViewUtils;
import org.herod.order.common.model.Order;
import org.herod.order.common.model.OrderItem;
import org.herod.order.common.model.OrderStatus;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

/**
 * 
 * 
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 * 
 */
public class OrderView extends LinearLayout implements
		GoodsQuantityChangedListener, ViewFindable {
	private static final String[] FORM_FROM = new String[] { SHOP_NAME,
			SHOP_TIPS, STATUS, SERIAL_NUMBER, SUBMIT_TIME, COMMENT,
			COST_OF_RUN_ERRANDS, TOTAL_AMOUNT_WITH_COST_OF_RUN_ERRANDS,
			TOTAL_QUANTITY };
	private static final int[] FORM_TO = new int[] { shopName, shopTips,
			status, serialNumber, submitTime, comment, costOfRunErrands,
			totalWithCostOfRunErrands, totalQuantity };
	private static FormHelper formHelper = new FormHelperBuilder(FORM_FROM,
			FORM_TO, Order.class).addDateSerializer(MM_DD_HH_MM).build();

	@InjectView(R.id.orderItemsListView)
	private LinearLayout orderItemsContainer;

	private Order order;
	private AbstractOrdersActivity activity;

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
		findViewById(R.id.shopName).setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				long shopId = order.getShopId();
				MapWrapper<String, Object> shop = BuyerContext.getShopService()
						.findShopById(shopId);
				GoodsListActivity.start(activity, shopId, order.getShopName(),
						order.getShopPhone(), shop.getLong(TIMESTAMP), true);
			}
		});
	}

	@Override
	public void onGoodsQuantityChanged() {
		updateOrderSummationInfo();
	}

	public void setOrder(Order order) {
		this.order = order;
		if (order.getStatus() == OrderStatus.Unsubmit) {
			findViewById(R.id.cancelOrderButton).setVisibility(View.VISIBLE);
		} else {
			findViewById(R.id.cancelOrderButton).setVisibility(View.INVISIBLE);
		}
		formHelper.setValues(order, this);

		updateOrderSummationInfo();
		orderItemsContainer.removeAllViews();

		for (OrderItem item : order.getOrderItems()) {
			addLineToOrderItemListView(orderItemsContainer);
			OrderItemView child = creasteOrderItemView(order, item);
			LayoutParams param = new LayoutParams(LayoutParams.MATCH_PARENT,
					LayoutParams.WRAP_CONTENT);
			orderItemsContainer.addView(child, param);
		}
	}

	private OrderItemView creasteOrderItemView(Order order, OrderItem item) {
		OrderItemView child = new OrderItemView(getContext());
		child.setOrderAndOrderItem(order, item);
		child.setActivity(activity);
		child.setGoodsQuantityChangedListener(this);
		return child;
	}

	private void updateOrderSummationInfo() {
		double costOfRunErrands = order.getShopCostOfRunErrands();
		double minChargeForFreeDelivery = order
				.getShopMinChargeForFreeDelivery();
		if (order.getTotalAmount() < minChargeForFreeDelivery) {
			order.setCostOfRunErrands(costOfRunErrands);
		} else {
			order.setCostOfRunErrands(0);
		}
		TextViewUtils.setText(this, R.id.costOfRunErrands,
				order.getCostOfRunErrands());
		TextViewUtils.setText(this, R.id.totalWithCostOfRunErrands,
				order.getTotalAmountWithCostOfRunErrands());
		TextViewUtils.setText(this, R.id.totalQuantity,
				order.getTotalQuantity());
	}

	private void addLineToOrderItemListView(LinearLayout orderItemsListView) {
		View line = new View(getContext());
		line.setBackgroundColor(0xFFE5E5E5);
		orderItemsListView.addView(line, new LayoutParams(
				LayoutParams.MATCH_PARENT, 1));
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

}
