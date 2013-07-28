package org.herod.worker.phone;

import java.util.ArrayList;
import java.util.List;

import org.herod.framework.utils.StringUtils;
import org.herod.framework.widget.TabPageIndicator;
import org.herod.worker.phone.fragment.OrderListFragment;
import org.herod.worker.phone.fragment.OrderListFragment.FragmentType;
import org.herod.worker.phone.view.OrderTabPageIndicator;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends FragmentActivity {
	private List<OrderListFragment> orderListFragments;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		orderListFragments = createOrderListFragments();
		FragmentPagerAdapter adapter = new OrderGroupAdapter(
				getSupportFragmentManager());

		ViewPager pager = (ViewPager) findViewById(R.id.pager);
		pager.setAdapter(adapter);
		TabPageIndicator indicator = (TabPageIndicator) findViewById(R.id.indicator);
		indicator.setViewPager(pager);
	}

	protected List<OrderListFragment> createOrderListFragments() {
		OrderTabPageIndicator indicator = (OrderTabPageIndicator) findViewById(R.id.indicator);
		List<OrderListFragment> fragments = new ArrayList<OrderListFragment>();
		fragments.add(createFragment("待受理订单", FragmentType.WaitAccept,
				indicator, 0));
		fragments.add(createFragment("待完成订单", FragmentType.WaitComplete,
				indicator, 1));
		return fragments;
	}

	private OrderListFragment createFragment(String title, FragmentType type,
			OrderTabPageIndicator indicator, int index) {
		OrderListFragment fragment = new OrderListFragment();
		fragment.setTitle(title);
		fragment.setType(type);
		fragment.setIndex(index);
		fragment.setTabPageIndicator(indicator);
		return fragment;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.logout) {
			WorkerContext.setLoginToken(StringUtils.EMPTY);
			startActivity(new Intent(this, LoginActivity.class));
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {
		finish();
	}

	class OrderGroupAdapter extends FragmentPagerAdapter {
		public OrderGroupAdapter(FragmentManager fm) {
			super(fm);
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
	}

}
