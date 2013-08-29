/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.buyer.phone;

import org.herod.framework.ViewFindable;
import org.herod.order.common.AbstractGoodsListFragment.QuantityChangedListener;

import android.app.ActionBar;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.TextView;

/**
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 * 
 */
public class BaseActivity extends FragmentActivity implements
		QuantityChangedListener, ViewFindable {
	private Menu menu;
	protected SearchView searchview;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		showBackAction(canBack());
	}

	private void showBackAction(boolean canBack) {
		ActionBar actionBar = getActionBar();
		if (actionBar != null) {
			actionBar.setHomeButtonEnabled(canBack);
			actionBar.setDisplayHomeAsUpEnabled(canBack);
			actionBar.setDisplayShowTitleEnabled(true);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		updateTotalQuantity();
		if (searchview != null && !searchview.isIconified()) {
			searchview.setIconified(true);
		}
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
		int menuConfigResource = getMenuConfigResource();
		if (menuConfigResource <= 0) {
			return false;
		}
		getMenuInflater().inflate(menuConfigResource, menu);
		this.menu = menu;
		initSearch(menu);
		return true;
	}

	private void initSearch(Menu menu) {
		MenuItem search = menu.findItem(R.id.search);
		if (search == null) {
			return;
		}
		searchview = (SearchView) search.getActionView();
		searchview.setMaxWidth(10000);
		searchview.setIconifiedByDefault(isSearchViewIconified());
		searchview.setOnQueryTextListener(new OnQueryTextListener() {
			public boolean onQueryTextSubmit(String query) {
				searchview.setIconified(true);
				return false;
			}

			public boolean onQueryTextChange(String newText) {
				return false;
			}
		});
		searchview.setOnSearchClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (!searchview.isIconified()) {
					showBackAction(true);
				}
			}
		});
		SearchManager mSearchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		SearchableInfo info = mSearchManager
				.getSearchableInfo(getComponentName());
		searchview.setSearchableInfo(info);
	}

	protected boolean isSearchViewIconified() {
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
		if (actionView == null) {
			return;
		}
		View totalQuantityView = actionView.findViewById(R.id.totalQuantity);
		if (totalQuantityView != null) {
			((TextView) totalQuantityView).setText(quantity + "");
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_MENU && searchview != null
				&& !searchview.isIconified()) {
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onBackPressed() {
		if (searchview != null && !searchview.isIconified()) {
			searchview.setIconified(true);
			searchview.setIconified(true);
			showBackAction(canBack());
			return;
		}
		super.onBackPressed();
	}

	protected boolean canBack() {
		return true;
	}

}
