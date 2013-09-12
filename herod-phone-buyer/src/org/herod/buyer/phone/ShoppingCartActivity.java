package org.herod.buyer.phone;

import java.util.List;

import org.herod.buyer.phone.adapter.OrderListAdapter;
import org.herod.buyer.phone.fragments.ConfirmDialogFragment;
import org.herod.buyer.phone.fragments.ConfirmDialogFragment.OnOkButtonClickListener;
import org.herod.buyer.phone.fragments.SubmitOrderInfoFragment;
import org.herod.order.common.model.Order;
import org.springframework.util.CollectionUtils;

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

	public void submitOrders(final View v) {
		final ShoppingCartCache cache = ShoppingCartCache.getInstance();
		final List<Order> outOfServiceTimeOrders = cache
				.getOutOfServiceTimeOrders();
		if (CollectionUtils.isEmpty(outOfServiceTimeOrders)) {
			SubmitOrderInfoFragment fragment = new SubmitOrderInfoFragment();
			fragment.show(getSupportFragmentManager(), null);
			return;
		}
		StringBuilder message = new StringBuilder();
		for (int i = 0; i < outOfServiceTimeOrders.size(); i++) {
			if (i > 0) {
				message.append("，");
			}
			message.append("“")
					.append(outOfServiceTimeOrders.get(i).getShopName())
					.append("”");
		}
		message.append("等商店不在服务时间范围内，删除这些订单后才能提交，是否删除这些订单！");
		ConfirmDialogFragment.showDialog(this, message.toString(),
				new OnOkButtonClickListener() {
					public void onOk() {
						cache.removeOrders(outOfServiceTimeOrders);
						refreshOrders();
						submitOrders(v);
					}
				});

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