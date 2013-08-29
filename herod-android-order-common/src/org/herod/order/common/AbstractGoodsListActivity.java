/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.order.common;

import java.util.List;

import org.herod.framework.HerodTask.AsyncTaskable;
import org.herod.framework.MapWrapper;
import org.herod.framework.RepeatedlyTask;
import org.herod.framework.widget.TabPageIndicator;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

/**
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 * 
 */
public abstract class AbstractGoodsListActivity extends BaseActivity implements
		AsyncTaskable<Object, List<MapWrapper<String, Object>>> {
	private ViewPager mPager;
	private TabPageIndicator mIndicator;
	private long shopId;
	private String shopName;
	private RefreshButtonHelper refreshButtonHelper;
	private RepeatedlyTask<Object, List<MapWrapper<String, Object>>> loadGoodsTypeTask;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_goods_list);
		shopId = getIntent().getLongExtra("shopId", 0);
		shopName = getIntent().getStringExtra("shopName");
		setTitle(shopName);

		mPager = (ViewPager) findViewById(R.id.pager);
		mIndicator = (TabPageIndicator) findViewById(R.id.indicator);
		loadGoodsTypeTask = new RepeatedlyTask<Object, List<MapWrapper<String, Object>>>(
				this);
		refreshButtonHelper = new RefreshButtonHelper(this, loadGoodsTypeTask,
				R.id.refreshButton, R.id.pager, R.id.indicator);
		loadGoodsTypeTask.execute();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	public List<MapWrapper<String, Object>> runOnBackground(Object... params) {
		return findGoodsTypesByShop(shopId);
	}

	protected abstract List<MapWrapper<String, Object>> findGoodsTypesByShop(
			long shopId);

	public void onPostExecute(List<MapWrapper<String, Object>> result) {
		if (refreshButtonHelper.checkNullResult(result)) {
			return;
		}
		GoodsFragmentAdapter mAdapter = new GoodsFragmentAdapter(result);
		mPager.setAdapter(mAdapter);
		mIndicator.setViewPager(mPager);
	}

	protected abstract AbstractGoodsTypeGoodsListFragment createGoodsListFragment();

	class GoodsFragmentAdapter extends FragmentPagerAdapter {
		private List<MapWrapper<String, Object>> goodsTypes;

		public GoodsFragmentAdapter(List<MapWrapper<String, Object>> goodsTypes) {
			super(getSupportFragmentManager());
			this.goodsTypes = goodsTypes;
		}

		public GoodsFragmentAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			AbstractGoodsTypeGoodsListFragment fragment = createGoodsListFragment();
			Bundle args = new Bundle();
			args.putAll(getIntent().getExtras());
			args.putLong("goodsTypeId",
					goodsTypes.get(position).getLong(Constants.ID));
			fragment.setArguments(args);
			return fragment;
		}

		@Override
		public int getCount() {
			return goodsTypes != null ? goodsTypes.size() : 0;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return goodsTypes.get(position).getString("name");
		}
	}

}
