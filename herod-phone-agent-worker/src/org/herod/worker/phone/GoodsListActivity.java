/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved.
 */
package org.herod.worker.phone;

import java.util.HashMap;
import java.util.List;

import org.herod.framework.MapWrapper;
import org.herod.order.common.AbstractGoodsListActivity;
import org.herod.order.common.AbstractGoodsListFragment.IShoppingCartCache;
import org.herod.order.common.AbstractGoodsTypeGoodsListFragment;
import org.herod.order.common.model.OrderItem;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * 
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 */
public class GoodsListActivity extends AbstractGoodsListActivity implements
		IShoppingCartCache {
	public static final int NEW_ORDER_ITEMS = 1;
	private HashMap<Long, OrderItem> orderItemsMap;
	private String serialNumber;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		serialNumber = getIntent().getStringExtra("serialNumber");
		orderItemsMap = new HashMap<Long, OrderItem>();
	}

	@Override
	protected List<MapWrapper<String, Object>> findGoodsTypesByShop(long shopId) {
		return WorkerContext.getWorkerService().findGoodsTypesByShop(shopId);
	}

	@Override
	protected AbstractGoodsTypeGoodsListFragment createGoodsListFragment() {
		return new GoodsListFragment();
	}

	public static class GoodsListFragment extends
			AbstractGoodsTypeGoodsListFragment {

		@Override
		protected List<MapWrapper<String, Object>> findGoodsesByType(
				long goodsTypeId, int begin, int count) {
			return WorkerContext.getWorkerService().findGoodsesByType(
					goodsTypeId, begin, count);
		}

	}

	@Override
	public void onBackPressed() {
		Intent data = new Intent();
		data.putExtra("serialNumber", serialNumber);
		data.putExtra("newOrderItemsMap", orderItemsMap);
		setResult(NEW_ORDER_ITEMS, data);
		super.onBackPressed();
	}

	@Override
	public int getQuantity(long shopId, long goodsId) {
		OrderItem orderItem = orderItemsMap.get(goodsId);
		return orderItem == null ? 0 : orderItem.getQuantity();
	}

	@Override
	public int increase(long shopId, MapWrapper<String, ?> goods) {
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

	@Override
	public int decrease(long shopId, long goodsId) {
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

	public static void show(Fragment fragment, long shopId, String shopName,
			String serialNumber) {
		Intent intent = new Intent(fragment.getActivity(),
				GoodsListActivity.class);
		intent.putExtra("shopId", shopId);
		intent.putExtra("shopName", shopName);
		intent.putExtra("serialNumber", serialNumber);
		fragment.startActivityForResult(intent, NEW_ORDER_ITEMS);
	}

}
