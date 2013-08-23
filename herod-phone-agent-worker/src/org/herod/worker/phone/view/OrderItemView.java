/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.worker.phone.view;

import org.herod.framework.ViewFindable;
import org.herod.framework.ci.InjectViewHelper;
import org.herod.framework.ci.annotation.InjectView;
import org.herod.framework.utils.TextViewUtils;
import org.herod.order.common.model.OrderItem;
import org.herod.worker.phone.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;

/**
 * 
 * 
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 * 
 */
public class OrderItemView extends RelativeLayout implements OnClickListener,
		ViewFindable {
	@InjectView(R.id.addButton)
	private View addButton;
	@InjectView(R.id.reduceButton)
	private View reduceButton;
	@InjectView(R.id.deletedLine)
	private View deletedLine;
	private GoodsQuantityChangedListener goodsQuantityChangedListener;
	private OrderItem orderItem;

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

	public void setOrderAndOrderItem(OrderItem orderItem) {
		this.orderItem = orderItem;
		setGoodsName(orderItem.getGoodsName());
		int quantity = OrderEditorManager.getInstance().getOrderItemQuantity(
				orderItem);
		setQuantity(quantity);
		setSellingPrice(orderItem.getSellingPrice());
	}

	public void setSellingPrice(double unitPrice) {
		TextViewUtils.setText(this, R.id.unitPrice, unitPrice);
	}

	public void setQuantity(int quantity) {
		TextViewUtils.setText(this, R.id.quantity, quantity);
		if (quantity > 0) {
			deletedLine.setVisibility(View.GONE);
		} else {
			deletedLine.setVisibility(View.VISIBLE);
		}
		if (goodsQuantityChangedListener != null) {
			goodsQuantityChangedListener.onGoodsQuantityChanged();
		}
	}

	public void setGoodsName(String goodsName) {
		TextViewUtils.setText(this, R.id.goodsName, goodsName);
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
				.getOrderEditor();
		if (orderEditor != null) {
			int newQuantity = orderEditor.decreaseItem(orderItem
					.getSerialNumber());
			setQuantity(newQuantity);
		}
	}

	protected void increaseQuantity() {
		OrderEditor orderEditor = OrderEditorManager.getInstance()
				.getOrderEditor();
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
		addButton.setVisibility(View.VISIBLE);
		reduceButton.setVisibility(View.VISIBLE);
	}

}
