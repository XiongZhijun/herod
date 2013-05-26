package org.herod.buyer.phone;

import java.util.List;

import org.herod.buyer.phone.adapter.OrderListAdapter;
import org.herod.buyer.phone.model.Order;

import android.os.Bundle;
import android.widget.ListAdapter;
import android.widget.ListView;

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

		List<Order> orders = ShoppingCartCache.getInstance().getAllOrders();
		ListAdapter adapter = new OrderListAdapter(this,orders );
		orderListView.setAdapter(adapter);
	}


}