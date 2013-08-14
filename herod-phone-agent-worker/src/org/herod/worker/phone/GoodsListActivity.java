/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved.
 */
package org.herod.worker.phone;

import java.util.List;

import org.herod.framework.MapWrapper;
import org.herod.order.common.AbstractGoodsListActivity;
import org.herod.order.common.CommonGoodsListFragment;

import android.app.Activity;
import android.content.Intent;

/**
 * 
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 */
public class GoodsListActivity extends AbstractGoodsListActivity {

	@Override
	protected List<MapWrapper<String, Object>> findGoodsTypesByShop(long shopId) {
		return WorkerContext.getWorkerService().findGoodsTypesByShop(shopId);
	}

	@Override
	protected CommonGoodsListFragment createGoodsListFragment() {
		return new GoodsListFragment();
	}

	public static void startGoodsActivity(Activity activity, long shopId,
			String shopName, String serialNumber) {
		Intent intent = new Intent(activity, GoodsListActivity.class);
		intent.putExtra("shopId", shopId);
		intent.putExtra("shopName", shopName);
		intent.putExtra("serialNumber", serialNumber);
		activity.startActivityForResult(intent, NEW_ORDER_ITEMS);
	}

	public static class GoodsListFragment extends CommonGoodsListFragment {

		@Override
		protected List<MapWrapper<String, Object>> findGoodsesByType(
				long goodsTypeId, int begin, int count) {
			return WorkerContext.getWorkerService().findGoodsesByType(
					goodsTypeId, begin, count);
		}

	}

}
