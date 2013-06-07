/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.buyer.phone.fragments;

import java.util.Collections;
import java.util.List;

import org.herod.buyer.phone.BuyerContext;
import org.herod.buyer.phone.HerodTask;
import org.herod.buyer.phone.R;
import org.herod.framework.MapWrapper;
import org.herod.framework.adapter.SimpleAdapter;

import android.support.v4.app.FragmentActivity;

/**
 * 
 * 
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 * 
 */
public class SearchGoodsListFragment extends AbstractGoodsListFragment {

	private int begin = 0;
	private int count = 30;
	private String goodsName;

	@Override
	public void onResume() {
		super.onResume();
		begin = 0;
	}

	@Override
	public List<MapWrapper<String, Object>> runOnBackground(Long... params) {
		List<MapWrapper<String, Object>> goodses = BuyerContext
				.getBuyerService().searchGoodses(goodsName, begin, count);
		begin += count;
		return goodses;
	}

	@Override
	protected SimpleAdapter createAdapter(FragmentActivity activity) {
		return new SimpleAdapter(activity,
				Collections.<MapWrapper<String, Object>> emptyList(),
				R.layout.activity_goods_search_goods_item, new String[] {
						"name", "price", "name", "name", "name", "shopName" },
				new int[] { R.id.name, R.id.price, R.id.quantity,
						R.id.addButton, R.id.reduceButton, R.id.shopName });
	}

	@Override
	protected long getGoodsShopId(MapWrapper<String, Object> goods) {
		return goods.getLong("shopId");
	}

	public void query(String query) {
		goodsName = query;
		new HerodTask<Long, List<MapWrapper<String, Object>>>(this).execute();
	}

	@Override
	protected boolean isLoadOnResume() {
		return false;
	}

}
