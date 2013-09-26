package org.herod.worker.phone;

import static org.herod.event.EventCodes.HEARTBEAT_COMMAND;
import static org.herod.event.EventCodes.REJECT_COMMAND;
import static org.herod.event.EventCodes.SUBMIT_COMMAND;
import static org.herod.event.EventFields.ACCEPTTED_COUNT;
import static org.herod.event.EventFields.SUBMITTED_COUNT;
import static org.herod.worker.phone.fragment.OrderListFragment.createWaitAcceptOrderListFragment;
import static org.herod.worker.phone.fragment.OrderListFragment.createWaitCompleteOrderListFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.herod.event.Event;
import org.herod.framework.ci.InjectViewHelper;
import org.herod.framework.ci.annotation.InjectView;
import org.herod.framework.lbs.LocationManager;
import org.herod.framework.lbs.SimpleLocationPlan;
import org.herod.framework.lbs.SimpleLocationPlan.OnLocationSuccessListener;
import org.herod.order.common.BaseActivity;
import org.herod.worker.phone.event.EventActionUtils;
import org.herod.worker.phone.event.EventClientService;
import org.herod.worker.phone.fragment.OrderListFragment;
import org.herod.worker.phone.handler.HerodHandler;
import org.herod.worker.phone.service.ReconnectServiceConnection;
import org.herod.worker.phone.service.RegistServiceConnection;
import org.herod.worker.phone.view.OrderTabPageIndicator;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler.Callback;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Menu;
import android.view.MenuItem;

import com.baidu.location.BDLocation;
import com.nostra13.universalimageloader.utils.ImageLoaderUtils;

public class MainActivity extends BaseActivity implements Callback,
		OnLocationSuccessListener, OnPageChangeListener, Handlerable,
		TabPageIndicatorable {
	public static final int MESSAGE_KEY_REFRESH_ORDER_LIST = 1;
	private HerodHandler handler;
	@InjectView(R.id.pager)
	private ViewPager orderListFragmentPager;
	@InjectView(R.id.indicator)
	private OrderTabPageIndicator indicator;
	private OrderGroupAdapter orderListFragmentAdapter;
	private int currentFragmentIndex = 0;
	private BroadcastReceiver eventReceiver = new EventReceiver();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ImageLoaderUtils.initImageLoader(this);
		SimpleLocationPlan locationPlan = new SimpleLocationPlan(this);
		LocationManager.getInstance(this).executeWithPlan(locationPlan);
		new InjectViewHelper().injectViews(this);
		handler = new HerodHandler(this);
		orderListFragmentAdapter = new OrderGroupAdapter(
				getSupportFragmentManager(), createOrderListFragments());
		orderListFragmentPager.setAdapter(orderListFragmentAdapter);
		indicator.setViewPager(orderListFragmentPager);
		indicator.setOnPageChangeListener(this);

		initAndConnectToEventServer();
	}

	private void initAndConnectToEventServer() {
		bindService(new Intent(this, EventClientService.class),
				new RegistServiceConnection(this), Context.BIND_AUTO_CREATE);
		registerReceiver(eventReceiver,
				EventActionUtils.createEventIntentFilter());
	}

	protected List<OrderListFragment> createOrderListFragments() {
		List<OrderListFragment> fragments = new ArrayList<OrderListFragment>();
		fragments.add(createWaitAcceptOrderListFragment(0));
		fragments.add(createWaitCompleteOrderListFragment(1));
		return fragments;
	}

	@Override
	protected void onDestroy() {
		unregisterReceiver(eventReceiver);
		orderListFragmentAdapter.clear();
		super.onDestroy();
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
			reconnectEventServer();
			startActivity(new Intent(this, LoginActivity.class));
			finish();
			return true;
		} else if (item.getItemId() == R.id.historyOrders) {
			startActivity(new Intent(this, HistoryOrderActivity.class));
		}
		return super.onOptionsItemSelected(item);
	}

	private void reconnectEventServer() {
		bindService(new Intent(this, EventClientService.class),
				new ReconnectServiceConnection(this), BIND_AUTO_CREATE);
	}

	protected boolean canBack() {
		return false;
	}

	@Override
	public void onBackPressed() {
		finish();
	}

	public OrderTabPageIndicator getIndicator() {
		return indicator;
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

	class EventReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			Event event = (Event) intent.getSerializableExtra(Constants.EVENT);
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

	private static final List<String> WAIT_ACCEPT_TYPE_MUST_REFRESH_COMMANDS = Arrays
			.asList(REJECT_COMMAND, SUBMIT_COMMAND);

	private static final List<String> WAIT_COMPLETE_TYPE_MUST_REFRESH_COMMANDS = Arrays
			.asList(HEARTBEAT_COMMAND);

}
