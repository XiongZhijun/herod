/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.buyer.phone;

import org.herod.buyer.phone.fragments.SearchGoodsListFragment;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.widget.SearchView;

/**
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 * 
 */
public class GoodsSearchActivity extends BaseActivity {

	private SearchGoodsListFragment goodsListFragment;
	private String queryString;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_goods_search);
		goodsListFragment = (SearchGoodsListFragment) getSupportFragmentManager()
				.findFragmentById(R.id.goodsListFragment);
		handleIntent(getIntent());
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (searchview != null) {
			searchview.setQuery(queryString, false);
		}
	}

	@Override
	protected void onNewIntent(Intent intent) {
		setIntent(intent);
		handleIntent(intent);
	}

	private void handleIntent(Intent intent) {
		if (!Intent.ACTION_SEARCH.equals(intent.getAction())) {
			return;
		}
		queryString = intent.getStringExtra(SearchManager.QUERY);
		Bundle args = new Bundle();
		args.putString("goodsName", queryString);
		if (searchview != null) {
			searchview.setQuery(queryString, false);
		}
		goodsListFragment.query(queryString);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		boolean result = super.onCreateOptionsMenu(menu);
		SearchView actionView = (SearchView) menu.findItem(R.id.search)
				.getActionView();
		actionView.setIconified(false);
		actionView.setQuery(queryString, false);
		return result;
	}

	protected int getMenuConfigResource() {
		return R.menu.search;
	}

	protected boolean isSearchViewIconified() {
		return true;
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		if (searchview.isIconified()) {
			setTitle(queryString);
		}
	}
}
