/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.buyer.phone;

import java.util.List;

import org.herod.buyer.phone.fragments.ShopListFragment;
import org.herod.framework.HerodTask.AsyncTaskable;
import org.herod.framework.MapWrapper;
import org.herod.framework.RepeatedlyTask;
import org.herod.framework.widget.TitlePageIndicator;
import org.herod.order.common.Constants;
import org.herod.order.common.RefreshButtonHelper;

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
public class StoresActivity extends BuyerBaseActivity implements
		AsyncTaskable<Object, List<MapWrapper<String, Object>>> {

	private ViewPager mPager;
	private TitlePageIndicator mIndicator;
	private RefreshButtonHelper refreshButtonHelper;
	private RepeatedlyTask<Object, List<MapWrapper<String, Object>>> loadShopTypesTask;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_stores);

		setTitle(R.string.app_name);

		mPager = (ViewPager) findViewById(R.id.pager);
		mIndicator = (TitlePageIndicator) findViewById(R.id.indicator);

		loadShopTypesTask = new RepeatedlyTask<Object, List<MapWrapper<String, Object>>>(
				this);
		refreshButtonHelper = new RefreshButtonHelper(this, loadShopTypesTask,
				R.id.refreshButton, R.id.pager, R.id.indicator);

		loadShopTypesTask.execute(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	public List<MapWrapper<String, Object>> runOnBackground(Object... params) {
		return BuyerContext.getBuyerService().findShopTypes();
	}

	public void onPostExecute(List<MapWrapper<String, Object>> result) {
		if (refreshButtonHelper.checkNullResult(result)) {
			return;
		}
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
			ShopListFragment shopListFragment = new ShopListFragment();
			Bundle args = new Bundle();
			args.putLong("shopTypeId",
					shopTypes.get(position).getLong(Constants.ID));
			shopListFragment.setArguments(args);
			return shopListFragment;
		}

		@Override
		public int getCount() {
			return shopTypes != null ? shopTypes.size() : 0;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return shopTypes.get(position).getString("name");
		}

	}

}
