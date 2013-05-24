package org.herod.buyer.phone;

import android.os.Bundle;

public class ShoppingCartActivity extends BaseActivity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shopping_cart);

		setTitle("购物车");
		showActionButton(R.id.historyOrdersButton, R.id.backButton);
	}

}