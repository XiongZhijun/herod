package org.herod.buyer.phone;

import java.util.List;

import org.herod.buyer.phone.adapter.OrderListAdapter;
import org.herod.buyer.phone.fragments.ConfirmDialogFragment;
import org.herod.buyer.phone.fragments.ConfirmDialogFragment.OnOkButtonClickListener;
import org.herod.buyer.phone.fragments.SubmitOrderInfoFragment;
import org.herod.order.common.model.Order;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;

public class ShoppingCartActivity extends AbstractOrdersActivity {
	private ListView orderListView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shopping_cart);

		setTitle("购物车");

		orderListView = (ListView) findViewById(R.id.ordersListView);
		findViewById(R.id.emptyTips).setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
		refreshOrders();
	}

	public void submitOrders(View v) {
		SubmitOrderInfoFragment fragment = new SubmitOrderInfoFragment();
		fragment.show(getSupportFragmentManager(), null);
	}

	public void clearShoppingCart(View v) {
		ConfirmDialogFragment.showDialog(this, "确定清空购物车？",
				new OnClearShoppingCartOkListener());
	}

	public void refreshOrders() {
		List<Order> orders = ShoppingCartCache.getInstance().getAllOrders();
		ListAdapter adapter = new OrderListAdapter(this, orders);
		orderListView.setAdapter(adapter);
		if (orders.size() > 0) {
			findViewById(R.id.clearButton).setVisibility(View.VISIBLE);
			findViewById(R.id.submitButton).setVisibility(View.VISIBLE);
			findViewById(R.id.emptyTips).setVisibility(View.GONE);
			orderListView.setVisibility(View.VISIBLE);
		} else {
			findViewById(R.id.clearButton).setVisibility(View.GONE);
			findViewById(R.id.submitButton).setVisibility(View.GONE);
			findViewById(R.id.emptyTips).setVisibility(View.VISIBLE);
			orderListView.setVisibility(View.GONE);
		}
	}

	private class OnClearShoppingCartOkListener implements
			OnOkButtonClickListener {
		public void onOk() {
			ShoppingCartCache.getInstance().clearOrders();
			refreshOrders();
		}

	}

	protected int getMenuConfigResource() {
		return R.menu.shopping_cart;
	}
}