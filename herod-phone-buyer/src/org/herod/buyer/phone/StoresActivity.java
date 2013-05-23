/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.buyer.phone;

import java.util.List;
import java.util.Map;

import org.herod.buyer.phone.HerodTask.AsyncTaskable;
import org.herod.buyer.phone.fragments.ShopListFragment;
import org.herod.framework.widget.ActionBar;
import org.herod.framework.widget.ActionBar.IntentAction;
import org.herod.framework.widget.TitlePageIndicator;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

/**
 * 
 * 
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 * 
 */
public class StoresActivity extends FragmentActivity implements
		AsyncTaskable<Object, List<Map<String, Object>>> {

	private ViewPager mPager;
	private TitlePageIndicator mIndicator;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_stores);

		ActionBar actionBar = (ActionBar) findViewById(R.id.actionbar);
		actionBar.setHomeAction(new IntentAction(this, HomeActivity
				.createIntent(this), R.drawable.ic_title_home_default));
		actionBar.setDisplayHomeAsUpEnabled(true);

		mPager = (ViewPager) findViewById(R.id.pager);
		mIndicator = (TitlePageIndicator) findViewById(R.id.indicator);

		new HerodTask<Object, List<Map<String, Object>>>(this).execute();
	}

	@Override
	protected void onResume() {
		super.onResume();

	}

	public List<Map<String, Object>> runOnBackground(Object... params) {
		return BuyerContext.getBuyerService().findShopTypes();
	}

	public void onPostExecute(List<Map<String, Object>> result) {
		ShopsFragmentAdapter mAdapter = new ShopsFragmentAdapter(result);
		mPager.setAdapter(mAdapter);
		mIndicator.setViewPager(mPager);
		int position = getIntent().getIntExtra("position", 0);
		mPager.setCurrentItem(position);
	}

	class ShopsFragmentAdapter extends FragmentPagerAdapter {
		private List<Map<String, Object>> shopTypes;

		public ShopsFragmentAdapter(List<Map<String, Object>> shopTypes) {
			super(getSupportFragmentManager());
			this.shopTypes = shopTypes;
		}

		public ShopsFragmentAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			return new ShopListFragment();
		}

		@Override
		public int getCount() {
			return shopTypes.size();
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return (CharSequence) shopTypes.get(position).get("name");
		}

	}

}
