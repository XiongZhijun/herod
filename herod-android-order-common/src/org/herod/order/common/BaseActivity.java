/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.order.common;

import org.herod.framework.ViewFindable;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;

/**
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 * 
 */
public abstract class BaseActivity extends FragmentActivity implements
		ViewFindable {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		showBackAction(canBack());
	}

	protected void showBackAction(boolean canBack) {
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
	}

	public void back(MenuItem item) {
		onBackPressed();
	}

	public void queryGoods(MenuItem item) {
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		int menuConfigResource = getMenuConfigResource();
		if (menuConfigResource <= 0) {
			return false;
		}
		getMenuInflater().inflate(menuConfigResource, menu);
		return true;
	}

	protected int getMenuConfigResource() {
		return 0;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			back(item);
			return true;
		}
		return false;
	}

	protected boolean canBack() {
		return true;
	}

}
