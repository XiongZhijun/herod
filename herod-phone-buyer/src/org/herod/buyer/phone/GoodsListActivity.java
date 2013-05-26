/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.buyer.phone;

import java.util.List;
import java.util.Map;

import org.herod.buyer.phone.HerodTask.AsyncTaskable;
import org.herod.buyer.phone.fragments.GoodsListFragment;
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
public class GoodsListActivity extends BaseActivity implements
		AsyncTaskable<Object, List<Map<String, Object>>> {
	private ViewPager mPager;
	private TabPageIndicator mIndicator;
	private long shopId;
	private String shopName;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_goods_list);
		shopId = getIntent().getLongExtra("shopId", 0);
		shopName = getIntent().getStringExtra("shopName");
		setTitle(shopName);
		showActionButton(R.id.queryButton, R.id.historyOrdersButton,
				R.id.shoppingCartButton, R.id.backButton);

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
			Bundle args = new Bundle();
			args.putLong("shopId", shopId);
			fragment.setArguments(args);
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
