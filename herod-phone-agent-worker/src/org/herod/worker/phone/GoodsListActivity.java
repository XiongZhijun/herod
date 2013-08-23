/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved.
 */
package org.herod.worker.phone;

import java.util.List;

import org.herod.framework.MapWrapper;
import org.herod.order.common.AbstractGoodsListActivity;
import org.herod.order.common.AbstractGoodsTypeGoodsListFragment;
import org.herod.worker.phone.view.OrderEditorManager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * 
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 */
public class GoodsListActivity extends AbstractGoodsListActivity {
	public static final int NEW_ORDER_ITEMS = 1;
	private String serialNumber;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		serialNumber = getIntent().getStringExtra("serialNumber");
	}

	@Override
	protected List<MapWrapper<String, Object>> findGoodsTypesByShop(long shopId) {
		return WorkerContext.getWorkerService().findGoodsTypesByShop(shopId);
	}

	@Override
	protected AbstractGoodsTypeGoodsListFragment createGoodsListFragment() {
		GoodsListFragment goodsListFragment = new GoodsListFragment();
		goodsListFragment.serialNumber = serialNumber;
		return goodsListFragment;
	}

	public static class GoodsListFragment extends
			AbstractGoodsTypeGoodsListFragment {
		private String serialNumber;

		@Override
		protected List<MapWrapper<String, Object>> findGoodsesByType(
				long goodsTypeId, int begin, int count) {
			return WorkerContext.getWorkerService().findGoodsesByType(
					goodsTypeId, begin, count);
		}

		@Override
		protected IShoppingCartCache getShoppingCartCache() {
			if (OrderEditorManager.getInstance().isInEdit(serialNumber)) {
				return OrderEditorManager.getInstance().getOrderEditor();
			}
			return new IShoppingCartCache() {
				public int increase(long shopId, MapWrapper<String, ?> goods) {
					return 0;
				}

				public int getQuantity(long shopId, long goodsId) {
					return 0;
				}

				public int decrease(long shopId, long goodsId) {
					return 0;
				}
			};
		}

	}

	@Override
	public void onBackPressed() {
		Intent data = new Intent();
		setResult(NEW_ORDER_ITEMS, data);
		super.onBackPressed();
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
