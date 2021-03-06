/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved.
 */
package org.herod.worker.phone;

import java.util.ArrayList;
import java.util.List;

import org.herod.worker.phone.fragment.OrderListFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * 
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 */
public class OrderGroupAdapter extends FragmentPagerAdapter {
	private List<OrderListFragment> orderListFragments = new ArrayList<OrderListFragment>();

	public OrderGroupAdapter(FragmentManager fm,
			List<OrderListFragment> orderListFragments) {
		super(fm);
		if (orderListFragments != null) {
			this.orderListFragments.addAll(orderListFragments);
		}
	}

	@Override
	public Fragment getItem(int position) {
		return orderListFragments.get(position);
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return orderListFragments.get(position).getTitle();
	}

	@Override
	public int getCount() {
		return orderListFragments.size();
	}

	public void clear() {
		orderListFragments.clear();
		notifyDataSetChanged();
	}
}
