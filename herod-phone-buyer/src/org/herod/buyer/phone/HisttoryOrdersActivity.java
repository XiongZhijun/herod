/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.buyer.phone;

import java.util.List;

import org.herod.buyer.phone.adapter.OrderListAdapter;
import org.herod.buyer.phone.db.OrderDao;
import org.herod.buyer.phone.fragments.ConfirmDialogFragment;
import org.herod.buyer.phone.fragments.ConfirmDialogFragment.OnOkButtonClickListener;
import org.herod.buyer.phone.model.Order;
import org.herod.framework.db.DatabaseOpenHelper;

import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * 
 * 
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 * 
 */
public class HisttoryOrdersActivity extends AbstractOrdersActivity {
	private ListView orderListView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_history_orders);
		setTitle("历史订单");

		orderListView = (ListView) findViewById(R.id.ordersListView);

		refreshOrders();

	}

	public void clearHistoryOrders(View v) {
		ConfirmDialogFragment.showDialog(this, "确定删除所有历史记录？",
				new OnClearShoppingCartOkListener());
	}

	public void refreshOrders() {
		SQLiteOpenHelper openHelper = new DatabaseOpenHelper(this);
		OrderDao orderDao = new OrderDao(openHelper);
		List<Order> orders = orderDao.getAllOrders();
		openHelper.close();
		
		ListAdapter adapter = new OrderListAdapter(this, orders);
		orderListView.setAdapter(adapter);
		findViewById(R.id.clearButton).setEnabled(orders.size() > 0);
	}

	private class OnClearShoppingCartOkListener implements
			OnOkButtonClickListener {
		public void onOk() {
			SQLiteOpenHelper openHelper = new DatabaseOpenHelper(getApplicationContext());
			OrderDao orderDao = new OrderDao(openHelper);
			orderDao.deleteAllOrders();
			openHelper.close();
			refreshOrders();
		}

	}
}
