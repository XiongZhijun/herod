/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.buyer.phone;

import java.util.List;

import org.herod.buyer.phone.HerodTask.AsyncTaskable;
import org.herod.buyer.phone.fragments.ShopListFragment;
import org.herod.framework.MapWrapper;
import org.herod.framework.widget.TitlePageIndicator;

import android.os.Bundle;
import android.support.v4.app.Fragment;
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
public class StoresActivity extends BaseActivity implements
		AsyncTaskable<Object, List<MapWrapper<String, Object>>> {

	private ViewPager mPager;
	private TitlePageIndicator mIndicator;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_stores);

		setTitle(R.string.app_name);

		mPager = (ViewPager) findViewById(R.id.pager);
		mIndicator = (TitlePageIndicator) findViewById(R.id.indicator);

		new HerodTask<Object, List<MapWrapper<String, Object>>>(this).execute();
	}

	@Override
	protected void onResume() {
		super.onResume();

	}

	public List<MapWrapper<String, Object>> runOnBackground(Object... params) {
		return BuyerContext.getBuyerService().findShopTypes();
	}

	public void onPostExecute(List<MapWrapper<String, Object>> result) {
		ShopsFragmentAdapter mAdapter = new ShopsFragmentAdapter(result);
		mPager.setAdapter(mAdapter);
		mIndicator.setViewPager(mPager);
		int position = getIntent().getIntExtra("position", 0);
		mPager.setCurrentItem(position);
	}

	class ShopsFragmentAdapter extends FragmentPagerAdapter {
		private List<MapWrapper<String, Object>> shopTypes;

		public ShopsFragmentAdapter(List<MapWrapper<String, Object>> shopTypes) {
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
			return shopTypes.get(position).getString("name");
		}

	}

}
