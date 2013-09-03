/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.order.common;

import static org.herod.order.common.Constants.GOODS_TYPE_ID;
import static org.herod.order.common.Constants.NAME;
import static org.herod.order.common.Constants.SHOP_ID;
import static org.herod.order.common.Constants.SHOP_NAME;

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
		shopId = getIntent().getLongExtra(SHOP_ID, 0);
		shopName = getIntent().getStringExtra(SHOP_NAME);
		setTitle(shopName);

		mPager = (ViewPager) findViewById(R.id.pager);
		mIndicator = (TabPageIndicator) findViewById(R.id.indicator);
		loadGoodsTypeTask = new RepeatedlyTask<Object, List<MapWrapper<String, Object>>>(
				this);
		refreshButtonHelper = new RefreshButtonHelper(this, loadGoodsTypeTask,
				R.id.refreshButton, R.id.pager, R.id.indicator);
		loadGoodsTypeTask.execute(this);
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
			MapWrapper<String, Object> goodsType = goodsTypes.get(position);
			args.putLong(GOODS_TYPE_ID, goodsType.getLong(Constants.ID));
			args.putLong(Constants.TIMESTAMP,
					goodsType.getLong(Constants.TIMESTAMP));
			fragment.setArguments(args);
			return fragment;
		}

		@Override
		public int getCount() {
			return goodsTypes != null ? goodsTypes.size() : 0;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return goodsTypes.get(position).getString(NAME);
		}
	}

}
