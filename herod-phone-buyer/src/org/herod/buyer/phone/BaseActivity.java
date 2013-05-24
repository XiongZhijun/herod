/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.buyer.phone;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.TextView;

/**
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 * 
 */
public class BaseActivity extends FragmentActivity {

	@Override
	protected void onResume() {
		super.onResume();
		updateTotalQuantity();
	}

	public void back(View v) {
		onBackPressed();
	}

	public void queryGoods(View v) {
	}

	public void showHistoryOrders(View v) {
	}

	public void showShoppingCart(View v) {
		startActivity(new Intent(this, ShoppingCartActivity.class));
	}

	public void hideActionButton(int... ids) {
		for (int id : ids) {
			findViewById(id).setVisibility(View.GONE);
		}
	}

	public void showActionButton(int... ids) {
		for (int id : ids) {
			findViewById(id).setVisibility(View.VISIBLE);
		}
	}

	public void updateTotalQuantity() {
		TextView totalQuantityView = (TextView) findViewById(R.id.totalQuantity);
		int quantity = ShoppingCartCache.getInstance().getTotalQuantity();
		totalQuantityView.setText(Integer.toString(quantity));
	}

	public void setTitle(String title) {
		TextView titleView = (TextView) findViewById(R.id.title);
		titleView.setText(title);
	}

	public void setTitle(int title) {
		TextView titleView = (TextView) findViewById(R.id.title);
		titleView.setText(title);
	}

	public void setCanBack(boolean canBack) {
		if (canBack) {
			findViewById(R.id.backButton).setVisibility(View.VISIBLE);
		} else {
			findViewById(R.id.backButton).setVisibility(View.INVISIBLE);
		}
	}

}
