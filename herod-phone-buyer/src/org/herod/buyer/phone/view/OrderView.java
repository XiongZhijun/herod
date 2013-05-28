/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.buyer.phone.view;

import java.util.Map;

import org.herod.buyer.phone.BuyerContext;
import org.herod.buyer.phone.Constants;
import org.herod.buyer.phone.MapUtils;
import org.herod.buyer.phone.R;
import org.herod.buyer.phone.ShopService;
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
	@InjectView(R.id.shopTips)
	private TextView shopTipsView;
	@InjectView(R.id.orderItemsListView)
	private LinearLayout orderItemsContainer;
	@InjectView(R.id.totalAmount)
	private TextView totalAmountView;
	@InjectView(R.id.costOfRunErrands)
	private TextView costOfRunErrandsView;
	@InjectView(R.id.totalWithCostOfRunErrands)
	private TextView totalWithCostOfRunErrandsView;
	private Order order;
	private ShopService shopService;
	private OrderItemView summationView;
	private Map<String, Object> shop;

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
		shopService = BuyerContext.getShopService();
	}

	@Override
	public void onGoodsQuantityChanged() {
		updateOrderSummationInfo();
	}

	public void setOrder(Order order) {
		this.order = order;
		this.shop = shopService.findShopById(order.getShopId());
		orderItemsContainer.removeAllViews();
		setText(shopNameView, shop.get("name"));
		setText(shopTipsView, createShopTips(shop));
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

	private String createShopTips(Map<String, Object> shop) {
		StringBuilder sb = new StringBuilder();
		double charge = MapUtils.getDouble(shop,
				Constants.MIN_CHARGE_FOR_FREE_DELIVERY);
		if (charge > 0) {
			sb.append("消费满").append(charge).append("免跑腿费");
		} else {
			sb.append("免跑腿费");
		}
		return sb.toString();
	}

	private void updateOrderSummationInfo() {
		if (summationView != null && order != null && shop != null) {
			summationView.setQuantity(order.getTotalQuantity());
			summationView.setUnitPrice(order.getTotalAmount());
			showOrderTotalInfo();
		}
	}

	private void showOrderTotalInfo() {
		double costOfRunErrands = MapUtils.getDouble(shop,
				Constants.COST_OF_RUN_ERRANDS);
		double minChargeForFreeDelivery = MapUtils.getDouble(shop,
				Constants.MIN_CHARGE_FOR_FREE_DELIVERY);
		if (order.getTotalAmount() < minChargeForFreeDelivery) {
			order.setCostOfRunErrands(costOfRunErrands);
		} else {
			order.setCostOfRunErrands(0);
		}
		setText(costOfRunErrandsView, order.getCostOfRunErrands());
		totalAmountView.setText(Double.toString(order.getTotalAmount()));
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
		((TextView) view).setText(data.toString());
	}

}
