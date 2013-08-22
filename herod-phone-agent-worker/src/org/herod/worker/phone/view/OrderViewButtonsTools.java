/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved.
 */
package org.herod.worker.phone.view;

import java.util.ArrayList;
import java.util.List;

import org.herod.order.common.model.Order;
import org.herod.order.common.model.OrderStatus;
import org.herod.worker.phone.R;

import android.view.View;

/**
 * 
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 */
public class OrderViewButtonsTools {

	private static final int[] ALL_BUTTONS = new int[] {
			R.id.acceptOrderButton, R.id.completeOrderButton,
			R.id.editOrderButton, R.id.cancelOrderButton,
			R.id.cancelEditButton, R.id.confirmEditButton,
			R.id.addNewItemButton };

	public static void refreshButtonStatus(Order order, OrderView orderView,
			List<OrderItemView> orderItemViews) {
		boolean isInEdit = OrderEditorManager.getInstance().isInEdit(
				order.getSerialNumber());
		List<Integer> showButtons = new ArrayList<Integer>();
		if (isInEdit) {
			showButtons.add(R.id.cancelEditButton);
			showButtons.add(R.id.confirmEditButton);
			showButtons.add(R.id.addNewItemButton);
			for (OrderItemView view : orderItemViews) {
				view.enableEditButtons();
			}
		} else {
			if (order.getStatus() == OrderStatus.Submitted) {
				showButtons.add(R.id.acceptOrderButton);
			} else if (order.getStatus() == OrderStatus.Acceptted) {
				showButtons.add(R.id.completeOrderButton);
			}
			showButtons.add(R.id.editOrderButton);
			showButtons.add(R.id.cancelOrderButton);
			for (OrderItemView view : orderItemViews) {
				view.disableEditButtons();
			}
		}
		for (int buttonId : ALL_BUTTONS) {
			View view = orderView.findViewById(buttonId);
			if (showButtons.contains(buttonId)) {
				view.setVisibility(View.VISIBLE);
			} else {
				view.setVisibility(View.GONE);
			}
		}
	}
}
