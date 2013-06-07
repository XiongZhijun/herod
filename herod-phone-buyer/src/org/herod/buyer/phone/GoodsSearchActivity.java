/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.buyer.phone;

import org.herod.buyer.phone.fragments.SearchGoodsListFragment;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 * 
 */
public class GoodsSearchActivity extends BaseActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_goods_search);
	}

	@Override
	protected void onResume() {
		super.onResume();
		SearchGoodsListFragment goodsListFragment = (SearchGoodsListFragment) getSupportFragmentManager()
				.findFragmentById(R.id.goodsListFragment);
		Intent intent = getIntent();
		if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
			String query = intent.getStringExtra(SearchManager.QUERY);
			Bundle args = new Bundle();
			args.putString("goodsName", query);
			goodsListFragment.query(query);
		}
	}

	protected int getMenuConfigResource() {
		return R.menu.search;
	}
}
