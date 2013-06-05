/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.buyer.phone;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

/**
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 * 
 */
public class BaseActivity extends FragmentActivity {
	private Menu menu;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActionBar actionBar = getActionBar();
		if (actionBar != null) {
			actionBar.setHomeButtonEnabled(canBack());
			actionBar.setDisplayHomeAsUpEnabled(canBack());
			actionBar.setDisplayShowTitleEnabled(true);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		updateTotalQuantity();
	}

	public void back(MenuItem item) {
		onBackPressed();
	}

	public void queryGoods(MenuItem item) {
	}

	public void showHistoryOrders(MenuItem item) {
		startActivity(new Intent(this, HisttoryOrdersActivity.class));
	}

	public void showShoppingCart(MenuItem item) {
		startActivity(new Intent(this, ShoppingCartActivity.class));
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(getMenuConfigResource(), menu);
		this.menu = menu;
		return true;
	}

	protected int getMenuConfigResource() {
		return R.menu.home;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			back(item);
			return true;
		}
		return false;
	}

	public void updateTotalQuantity() {
		int quantity = ShoppingCartCache.getInstance().getTotalQuantity();
		if (menu == null) {
			return;
		}
		MenuItem item = menu.findItem(R.id.shoppingCart);
		if (item == null) {
			return;
		}
		View actionView = item.getActionView();
		View totalQuantityView = actionView.findViewById(R.id.totalQuantity);
		if (totalQuantityView != null) {
			((TextView) totalQuantityView).setText(quantity + "");
		}
	}

	protected boolean canBack() {
		return true;
	}

}
