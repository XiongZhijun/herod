package org.herod.worker.phone;

import static org.herod.event.EventCodes.ACCEPT_COMMAND;
import static org.herod.event.EventCodes.CANCEL_COMMAND;
import static org.herod.event.EventCodes.COMPLETE_COMMAND;
import static org.herod.event.EventCodes.HEARTBEAT_COMMAND;
import static org.herod.event.EventCodes.REJECT_COMMAND;
import static org.herod.event.EventCodes.SUBMIT_COMMAND;
import static org.herod.event.EventCodes.UPDATE_COMMAND;
import static org.herod.event.EventFields.ACCEPTTED_COUNT;
import static org.herod.event.EventFields.SUBMITTED_COUNT;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.herod.android.communication.TcpClientService.LocalBinder;
import org.herod.event.Event;
import org.herod.framework.lbs.LocationManager;
import org.herod.framework.lbs.SimpleLocationPlan;
import org.herod.framework.lbs.SimpleLocationPlan.OnLocationSuccessListener;
import org.herod.order.common.BaseActivity;
import org.herod.worker.phone.event.EventActionUtils;
import org.herod.worker.phone.event.EventClientService;
import org.herod.worker.phone.fragment.OrderListFragment;
import org.herod.worker.phone.fragment.OrderListFragment.FragmentType;
import org.herod.worker.phone.handler.HerodHandler;
import org.herod.worker.phone.view.OrderTabPageIndicator;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler.Callback;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Menu;
import android.view.MenuItem;

import com.baidu.location.BDLocation;
import com.nostra13.universalimageloader.utils.ImageLoaderUtils;

public class MainActivity extends BaseActivity implements Callback,
		OnLocationSuccessListener, OnPageChangeListener {
	public static final int MESSAGE_KEY_REFRESH_ORDER_LIST = 1;
	private List<OrderListFragment> orderListFragments;
	private HerodHandler handler;
	private ViewPager orderListFragmentPager;
	private FragmentPagerAdapter orderListFragmentAdapter;
	private OrderTabPageIndicator indicator;
	private int currentFragmentIndex = 0;
	private BroadcastReceiver eventReceiver = new EventReceiver();
	private ServiceConnection registServiceConnection = new ServiceConnection() {
		public void onServiceDisconnected(ComponentName name) {
		}

		public void onServiceConnected(ComponentName name, IBinder service) {
			EventClientService eventService = (EventClientService) ((LocalBinder) service)
					.getService();
			eventService.registToServer();
			unbindService(registServiceConnection);
		}
	};
	private ServiceConnection reconnectServiceConnection = new ServiceConnection() {
		public void onServiceDisconnected(ComponentName name) {
		}

		public void onServiceConnected(ComponentName name, IBinder service) {
			EventClientService eventService = (EventClientService) ((LocalBinder) service)
					.getService();
			eventService.reconnect();
			unbindService(reconnectServiceConnection);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ImageLoaderUtils.initImageLoader(this);
		SimpleLocationPlan locationPlan = new SimpleLocationPlan(this);
		LocationManager.getInstance(this).executeWithPlan(locationPlan);
		handler = new HerodHandler(this);
		indicator = (OrderTabPageIndicator) findViewById(R.id.indicator);
		orderListFragmentPager = (ViewPager) findViewById(R.id.pager);
		orderListFragments = createOrderListFragments(indicator);
		orderListFragmentAdapter = new OrderGroupAdapter(
				getSupportFragmentManager());
		orderListFragmentPager.setAdapter(orderListFragmentAdapter);
		indicator.setViewPager(orderListFragmentPager);
		indicator.setOnPageChangeListener(this);

		bindService(new Intent(this, EventClientService.class),
				registServiceConnection, Context.BIND_AUTO_CREATE);

		registerEventReceiver();
	}

	protected List<OrderListFragment> createOrderListFragments(
			OrderTabPageIndicator indicator) {
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
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(eventReceiver);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.logout) {
			WorkerContext.setLoginToken(null);
			bindService(new Intent(this, EventClientService.class),
					reconnectServiceConnection, BIND_AUTO_CREATE);
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
			refreshCurrentFragment();
			return true;
		default:
			return false;
		}

	}

	@Override
	public void onLocationSuccess(BDLocation location) {

	}

	@Override
	protected int getMenuConfigResource() {
		return R.menu.main;
	}

	private void refreshCurrentFragment() {
		OrderListFragment currentFragment = (OrderListFragment) orderListFragmentAdapter
				.getItem(currentFragmentIndex);
		currentFragment.refreshOrderList();
	}

	private void registerEventReceiver() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(EventActionUtils.getEventAction(HEARTBEAT_COMMAND));
		filter.addAction(EventActionUtils.getEventAction(SUBMIT_COMMAND));
		filter.addAction(EventActionUtils.getEventAction(ACCEPT_COMMAND));
		filter.addAction(EventActionUtils.getEventAction(COMPLETE_COMMAND));
		filter.addAction(EventActionUtils.getEventAction(CANCEL_COMMAND));
		filter.addAction(EventActionUtils.getEventAction(REJECT_COMMAND));
		filter.addAction(EventActionUtils.getEventAction(UPDATE_COMMAND));
		registerReceiver(eventReceiver, filter);
	}

	class EventReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			Event event = (Event) intent
					.getSerializableExtra(EventClientService.EVENT);
			int submittedCounts = event.getInt(SUBMITTED_COUNT);
			int accepttedCounts = event.getInt(ACCEPTTED_COUNT);
			indicator.setTabQauntity(0, submittedCounts);
			indicator.setTabQauntity(1, accepttedCounts);

			if (canRefreshCurrentFragment(event.getCode())) {
				refreshCurrentFragment();
			}
		}

		private boolean canRefreshCurrentFragment(String code) {
			return code.equals(HEARTBEAT_COMMAND) || currentFragmentIndex == 0
					&& WAIT_ACCEPT_TYPE_MUST_REFRESH_COMMANDS.contains(code)
					|| currentFragmentIndex == 1
					&& WAIT_COMPLETE_TYPE_MUST_REFRESH_COMMANDS.contains(code);
		}
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
	}

	@Override
	public void onPageSelected(int index) {
		this.currentFragmentIndex = index;
		refreshCurrentFragment();
	}

	private static final List<String> WAIT_ACCEPT_TYPE_MUST_REFRESH_COMMANDS = Arrays
			.asList(REJECT_COMMAND, SUBMIT_COMMAND);

	private static final List<String> WAIT_COMPLETE_TYPE_MUST_REFRESH_COMMANDS = Arrays
			.asList(HEARTBEAT_COMMAND);

}
