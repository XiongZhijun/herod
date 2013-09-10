/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved.
 */
package org.herod.worker.phone;

import static org.herod.worker.phone.fragment.OrderListFragment.createCanceledOrderListFragment;
import static org.herod.worker.phone.fragment.OrderListFragment.createCompletedOrderListFragment;

import java.util.ArrayList;
import java.util.List;

import org.herod.framework.widget.TabPageIndicator;
import org.herod.order.common.BaseActivity;
import org.herod.worker.phone.fragment.OrderListFragment;
import org.herod.worker.phone.handler.HerodHandler;

import android.os.Bundle;
import android.os.Handler.Callback;
import android.os.Message;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;

/**
 * 
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 */
public class HistoryOrderActivity extends BaseActivity implements Callback,
		OnPageChangeListener, Handlerable {
	public static final int MESSAGE_KEY_REFRESH_ORDER_LIST = 1;
	private List<OrderListFragment> orderListFragments;
	private HerodHandler handler;
	private ViewPager orderListFragmentPager;
	private FragmentPagerAdapter orderListFragmentAdapter;
	private TabPageIndicator indicator;
	private int currentFragmentIndex = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_history_orders);
		handler = new HerodHandler(this);
		indicator = (TabPageIndicator) findViewById(R.id.indicator);
		orderListFragmentPager = (ViewPager) findViewById(R.id.pager);
		orderListFragments = createOrderListFragments();
		orderListFragmentAdapter = new OrderGroupAdapter(
				getSupportFragmentManager(), orderListFragments);
		orderListFragmentPager.setAdapter(orderListFragmentAdapter);
		indicator.setViewPager(orderListFragmentPager);
		indicator.setOnPageChangeListener(this);
		setTitle("历史订单");
	}

	protected List<OrderListFragment> createOrderListFragments() {
		List<OrderListFragment> fragments = new ArrayList<OrderListFragment>();
		fragments.add(createCompletedOrderListFragment(0));
		fragments.add(createCanceledOrderListFragment(1));
		return fragments;
	}

	@Override
	protected void onDestroy() {
		orderListFragments.clear();
		orderListFragmentAdapter.notifyDataSetChanged();
		super.onDestroy();
	}

	protected boolean canBack() {
		return true;
	}

	@Override
	public void onBackPressed() {
		finish();
	}

	public HerodHandler getHandler() {
		return handler;
	}

	@Override
	public boolean handleMessage(Message msg) {
		switch (msg.what) {
		case MESSAGE_KEY_REFRESH_ORDER_LIST:
			refreshCurrentFragment();
			return true;
		default:
			return false;
		}
	}

	private void refreshCurrentFragment() {
		OrderListFragment currentFragment = (OrderListFragment) orderListFragmentAdapter
				.getItem(currentFragmentIndex);
		currentFragment.refreshOrderList();
	}

	@Override
	public void onPageSelected(int index) {
		this.currentFragmentIndex = index;
		refreshCurrentFragment();
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
	}

}