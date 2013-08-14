package org.herod.worker.phone;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.herod.framework.lbs.LocationManager;
import org.herod.framework.lbs.SimpleLocationPlan;
import org.herod.framework.lbs.SimpleLocationPlan.OnLocationSuccessListener;
import org.herod.framework.utils.StringUtils;
import org.herod.framework.widget.TabPageIndicator;
import org.herod.order.common.BaseActivity;
import org.herod.order.common.model.OrderItem;
import org.herod.worker.phone.fragment.OrderListFragment;
import org.herod.worker.phone.fragment.OrderListFragment.FragmentType;
import org.herod.worker.phone.view.OrderEditor;
import org.herod.worker.phone.view.OrderEditorManager;
import org.herod.worker.phone.view.OrderTabPageIndicator;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

import com.baidu.location.BDLocation;
import com.nostra13.universalimageloader.utils.ImageLoaderUtils;

public class MainActivity extends BaseActivity implements Callback,
		OnLocationSuccessListener {
	private List<OrderListFragment> orderListFragments;
	private Handler handler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ImageLoaderUtils.initImageLoader(this);
		SimpleLocationPlan locationPlan = new SimpleLocationPlan(this);
		LocationManager.getInstance(this).executeWithPlan(locationPlan);
		handler = new Handler(this);
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
		fragment.setHandler(handler);
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

	protected boolean canBack() {
		return false;
	}

	@Override
	public void onBackPressed() {
		finish();
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		String serialNumber = data.getStringExtra("serialNumber");
		HashMap<Long, OrderItem> newOrderItemsMap = (HashMap<Long, OrderItem>) data
				.getSerializableExtra("newOrderItemsMap");
		OrderEditor orderEditor = OrderEditorManager.getInstance().findOrderEditor(serialNumber);
//		if(orderEditor != null) {
//			orderEditor.
//		}
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

	@Override
	public boolean handleMessage(Message msg) {
		switch (msg.what) {
		case MESSAGE_KEY_REFRESH_ORDER_LIST:
			for (OrderListFragment fragment : orderListFragments) {
				fragment.refreshOrderList();
			}
			break;
		default:
			break;
		}
		return true;
	}

	@Override
	public void onLocationSuccess(BDLocation location) {

	}

	public static final int MESSAGE_KEY_REFRESH_ORDER_LIST = 1;

	@Override
	protected int getMenuConfigResource() {
		return R.menu.main;
	}

}
