/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.order.common;

import java.util.HashMap;
import java.util.List;

import org.herod.framework.HerodTask;
import org.herod.framework.HerodTask.AsyncTaskable;
import org.herod.framework.MapWrapper;
import org.herod.framework.widget.TabPageIndicator;
import org.herod.order.common.model.OrderItem;

import android.content.Intent;
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
	public static final int NEW_ORDER_ITEMS = 1;
	private ViewPager mPager;
	private TabPageIndicator mIndicator;
	private long shopId;
	private String shopName;
	private HashMap<Long, OrderItem> orderItemsMap;
	private String serialNumber;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_goods_list);
		shopId = getIntent().getLongExtra("shopId", 0);
		shopName = getIntent().getStringExtra("shopName");
		serialNumber = getIntent().getStringExtra("serialNumber");
		setTitle(shopName);

		mPager = (ViewPager) findViewById(R.id.pager);
		mIndicator = (TabPageIndicator) findViewById(R.id.indicator);

		orderItemsMap = new HashMap<Long, OrderItem>();
		new HerodTask<Object, List<MapWrapper<String, Object>>>(this).execute();
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
		GoodsFragmentAdapter mAdapter = new GoodsFragmentAdapter(result);
		mPager.setAdapter(mAdapter);
		mIndicator.setViewPager(mPager);
	}

	@Override
	public void onBackPressed() {
		Intent data = new Intent();
		data.putExtra("serialNumber", serialNumber);
		data.putExtra("newOrderItemsMap", orderItemsMap);
		setResult(NEW_ORDER_ITEMS, data);
		super.onBackPressed();
	}

	protected abstract CommonGoodsListFragment createGoodsListFragment();

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
			CommonGoodsListFragment fragment = createGoodsListFragment();
			fragment.setGoodsType(goodsTypes.get(position));
			Bundle args = new Bundle();
			args.putAll(getIntent().getExtras());
			fragment.setArguments(args);
			return fragment;
		}

		@Override
		public int getCount() {
			return goodsTypes.size();
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return goodsTypes.get(position).getString("name");
		}
	}

	public int getQuantity(long goodsId) {
		OrderItem orderItem = orderItemsMap.get(goodsId);
		return orderItem == null ? 0 : orderItem.getQuantity();
	}

	public int increase(MapWrapper<String, Object> goods) {
		OrderItem orderItem = findAndCreateOrderItem(goods);
		int current = orderItem.getQuantity();
		current++;
		if (current > 99) {
			current = 99;
		}
		orderItem.setQuantity(current);
		return current;
	}

	private OrderItem findAndCreateOrderItem(MapWrapper<String, ?> goods) {
		long goodsId = goods.getLong("id");
		OrderItem orderItem = orderItemsMap.get(goodsId);
		if (orderItem == null) {
			orderItem = createOrderItem(goods);
			orderItemsMap.put(goodsId, orderItem);
		}
		return orderItem;
	}

	private OrderItem createOrderItem(MapWrapper<String, ?> goods) {
		OrderItem orderItem;
		orderItem = new OrderItem();
		long goodsId = goods.getLong("id");
		String goodsCode = goods.getString("code");
		String goodsName = goods.getString("name");
		double sellingPrice = goods.getDouble("sellingPrice");
		orderItem.setSellingPrice(sellingPrice);
		orderItem.setGoodsCode(goodsCode);
		orderItem.setGoodsName(goodsName);
		orderItem.setGoodsId(goodsId);
		return orderItem;
	}

	public int decrease(long goodsId) {
		OrderItem orderItem = orderItemsMap.get(goodsId);
		if (orderItem == null) {
			return 0;
		}
		int current = orderItem.getQuantity();
		current--;
		if (current > 0) {
			orderItem.setQuantity(current);
		} else {
			orderItemsMap.remove(goodsId);
			current = 0;
		}
		return current;
	}
}
