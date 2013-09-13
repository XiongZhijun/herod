/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.buyer.phone;

import java.util.ArrayList;
import java.util.List;

import org.herod.buyer.phone.fragments.HistoryOrdersFragment;
import org.herod.framework.widget.TabPageIndicator;
import org.herod.order.common.model.OrderStatus;

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
public class HistoryOrdersActivity extends BuyerBaseActivity {
	private static final String[] FRAGMENT_TITLES = new String[] { "未完成订单",
			"已完成订单", "已取消订单" };
	private static final OrderStatus[][] FRAGMENT_ORDER_STATUS = new OrderStatus[][] {
			{ OrderStatus.Submitted, OrderStatus.Acceptted,
					OrderStatus.Rejected }, { OrderStatus.Completed },
			{ OrderStatus.Cancelled } };
	private TabPageIndicator indicator;
	private ViewPager pager;
	private List<Fragment> fragments;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_history_orders);
		setTitle("历史订单");
		indicator = (TabPageIndicator) findViewById(R.id.indicator);
		pager = (ViewPager) findViewById(R.id.pager);
		fragments = createFragments();
		FragmentGroupAdapter adapter = new FragmentGroupAdapter(
				getSupportFragmentManager());
		pager.setAdapter(adapter);
		indicator.setViewPager(pager);
	}

	private List<Fragment> createFragments() {
		List<Fragment> fragments = new ArrayList<Fragment>();
		for (int i = 0; i < FRAGMENT_TITLES.length; i++) {
			fragments.add(HistoryOrdersFragment
					.createFragment(FRAGMENT_ORDER_STATUS[i]));
		}
		return fragments;
	}

	@Override
	protected void onDestroy() {
		if (fragments != null) {
			fragments.clear();
		}
		super.onDestroy();
	}

	protected int getMenuConfigResource() {
		return R.menu.history_orders;
	}

	class FragmentGroupAdapter extends FragmentPagerAdapter {

		public FragmentGroupAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int index) {
			return fragments.get(index);
		}

		@Override
		public int getCount() {
			return fragments != null ? fragments.size() : 0;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return FRAGMENT_TITLES[position];
		}
	}

}
