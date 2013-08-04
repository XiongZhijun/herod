/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.worker.phone.view;

import org.herod.framework.ci.InjectViewHelper;
import org.herod.framework.ci.annotation.InjectView;
import org.herod.worker.phone.R;
import org.herod.worker.phone.model.Order;
import org.herod.worker.phone.model.OrderItem;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 
 * 
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 * 
 */
public class OrderItemView extends RelativeLayout implements OnClickListener {
	@InjectView(R.id.goodsName)
	private TextView goodsNameView;
	@InjectView(R.id.unitPrice)
	private TextView sellingPriceView;
	@InjectView(R.id.quantity)
	private TextView quantityView;
	@InjectView(R.id.addButton)
	private View addButton;
	@InjectView(R.id.reduceButton)
	private View reduceButton;
	@InjectView(R.id.deletedLine)
	private View deletedLine;
	private GoodsQuantityChangedListener goodsQuantityChangedListener;
	private OrderItem orderItem;
	private boolean canEdit = true;

	public OrderItemView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView();
	}

	public OrderItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}

	public OrderItemView(Context context) {
		super(context);
		initView();
	}

	private void initView() {
		LayoutInflater.from(getContext()).inflate(R.layout.order_item, this);
		new InjectViewHelper().injectViews(this);
		addButton.setOnClickListener(this);
		reduceButton.setOnClickListener(this);
	}

	public void setOrderAndOrderItem(Order order, OrderItem orderItem) {
		this.orderItem = orderItem;
		setGoodsName(orderItem.getGoodsName());
		setQuantity(orderItem.getQuantity());
		setSellingPrice(orderItem.getSellingPrice());
	}

	public void setSellingPrice(double unitPrice) {
		sellingPriceView.setText(Double.toString(unitPrice));
	}

	public void setQuantity(int quantity) {
		quantityView.setText(Integer.toString(quantity));
		if (goodsQuantityChangedListener != null) {
			goodsQuantityChangedListener.onGoodsQuantityChanged();
		}
	}

	public void setGoodsName(String goodsName) {
		goodsNameView.setText(goodsName);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.addButton) {
			increaseQuantity();
		} else if (v.getId() == R.id.reduceButton) {
			decreaseQuantity();
		}
	}

	protected void decreaseQuantity() {
		OrderEditor orderEditor = OrderEditorManager.getInstance()
				.findOrderEditor(orderItem.getOrderSerialNumber());
		if (orderEditor != null) {
			int newQuantity = orderEditor.decreaseItem(orderItem
					.getSerialNumber());
			setQuantity(newQuantity);
		}
	}

	protected void increaseQuantity() {
		OrderEditor orderEditor = OrderEditorManager.getInstance()
				.findOrderEditor(orderItem.getOrderSerialNumber());
		if (orderEditor != null) {
			int newQuantity = orderEditor.increaseItem(orderItem
					.getSerialNumber());
			setQuantity(newQuantity);
		}
	}

	public void setGoodsQuantityChangedListener(
			GoodsQuantityChangedListener goodsQuantityChangedListener) {
		this.goodsQuantityChangedListener = goodsQuantityChangedListener;
	}

	public static interface GoodsQuantityChangedListener {
		void onGoodsQuantityChanged();
	}

	public void disableEditButtons() {
		addButton.setVisibility(View.INVISIBLE);
		reduceButton.setVisibility(View.INVISIBLE);
	}

	public void enableEditButtons() {
		if (canEdit) {
			addButton.setVisibility(View.VISIBLE);
			reduceButton.setVisibility(View.VISIBLE);
		}
	}

	public void setCanEdit(boolean canEdit) {
		this.canEdit = canEdit;
		if (!canEdit) {
			disableEditButtons();
		}
	}
}
