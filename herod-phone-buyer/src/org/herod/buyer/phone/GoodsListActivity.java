/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.buyer.phone;

import java.util.List;
import java.util.Map;

import org.herod.buyer.phone.HerodTask.AsyncTaskable;
import org.herod.buyer.phone.fragments.GoodsListFragment;
import org.herod.framework.widget.ActionBar;
import org.herod.framework.widget.ActionBar.IntentAction;
import org.herod.framework.widget.TabPageIndicator;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

/**
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 * 
 */
public class GoodsListActivity extends FragmentActivity implements
		AsyncTaskable<Object, List<Map<String, Object>>> {
	private ViewPager mPager;
	private TabPageIndicator mIndicator;
	private long shopId;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_goods_list);

		ActionBar actionBar = (ActionBar) findViewById(R.id.actionbar);
		actionBar.setHomeAction(new IntentAction(this, HomeActivity
				.createIntent(this), R.drawable.ic_title_home_default));
		actionBar.setDisplayHomeAsUpEnabled(true);

		mPager = (ViewPager) findViewById(R.id.pager);
		mIndicator = (TabPageIndicator) findViewById(R.id.indicator);

		new HerodTask<Object, List<Map<String, Object>>>(this).execute();
	}

	@Override
	protected void onResume() {
		super.onResume();

	}

	public List<Map<String, Object>> runOnBackground(Object... params) {
		return BuyerContext.getBuyerService().findGoodsTypesByShop(shopId);
	}

	public void onPostExecute(List<Map<String, Object>> result) {
		GoodsFragmentAdapter mAdapter = new GoodsFragmentAdapter(result);
		mPager.setAdapter(mAdapter);
		mIndicator.setViewPager(mPager);
	}

	class GoodsFragmentAdapter extends FragmentPagerAdapter {
		private List<Map<String, Object>> goodsTypes;

		public GoodsFragmentAdapter(List<Map<String, Object>> goodsTypes) {
			super(getSupportFragmentManager());
			this.goodsTypes = goodsTypes;
		}

		public GoodsFragmentAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			GoodsListFragment fragment = new GoodsListFragment();
			fragment.setGoodsType(goodsTypes.get(position));
			return fragment;
		}

		@Override
		public int getCount() {
			return goodsTypes.size();
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return (CharSequence) goodsTypes.get(position).get("name");
		}

	}
}
