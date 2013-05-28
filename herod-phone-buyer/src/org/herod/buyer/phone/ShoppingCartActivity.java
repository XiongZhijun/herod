package org.herod.buyer.phone;

import java.util.List;

import org.herod.buyer.phone.adapter.OrderListAdapter;
import org.herod.buyer.phone.fragments.ConfirmDialogFragment;
import org.herod.buyer.phone.fragments.ConfirmDialogFragment.OnButtonClickListener;
import org.herod.buyer.phone.model.Order;

import android.os.Bundle;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class ShoppingCartActivity extends BaseActivity {
	private ListView orderListView;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shopping_cart);

		setTitle("购物车");
		showActionButton(R.id.historyOrdersButton, R.id.backButton);

		orderListView = (ListView) findViewById(R.id.ordersListView);

		refreshOrders();
	}

	public void submitOrders(View v) {
		Toast.makeText(this, "下单", Toast.LENGTH_SHORT).show();
	}

	public void clearShoppingCart(View v) {
		ConfirmDialogFragment dialog = ConfirmDialogFragment.newInstance(
				R.drawable.alarm, "确定清空购物车？", new OnButtonClickListener() {
					public void onClick(int id) {
						if (id == R.id.okButton) {
							ShoppingCartCache.getInstance().clearOrders();
							refreshOrders();
						}
					}
				});
		dialog.show(getSupportFragmentManager(), null);

	}

	public void refreshOrders() {
		List<Order> orders = ShoppingCartCache.getInstance().getAllOrders();
		ListAdapter adapter = new OrderListAdapter(this, orders);
		orderListView.setAdapter(adapter);
		findViewById(R.id.clearButton).setEnabled(orders.size() > 0);
		findViewById(R.id.submitButton).setEnabled(orders.size() > 0);
	}
}