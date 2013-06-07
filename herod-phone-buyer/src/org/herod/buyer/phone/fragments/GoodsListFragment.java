/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.buyer.phone.fragments;

import java.util.Collections;
import java.util.List;

import org.herod.buyer.phone.BuyerContext;
import org.herod.buyer.phone.R;
import org.herod.framework.MapWrapper;
import org.herod.framework.adapter.SimpleAdapter;

import android.support.v4.app.FragmentActivity;

/**
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 * 
 */
public class GoodsListFragment extends AbstractGoodsListFragment {
	private MapWrapper<String, Object> goodsType;
	private long latestGoodsId = 0;

	@Override
	public void onResume() {
		super.onResume();
		latestGoodsId = 0;
	}

	@Override
	public List<MapWrapper<String, Object>> runOnBackground(Long... params) {
		long id = goodsType.getLong("id");
		return BuyerContext.getBuyerService().findGoodsesByType(id,
				latestGoodsId, 20);
	}

	@Override
	public void onPostExecute(List<MapWrapper<String, Object>> data) {
		super.onPostExecute(data);
		latestGoodsId = data.get(data.size() - 1).getLong("id");
	}

	@Override
	protected SimpleAdapter createAdapter(FragmentActivity activity) {
		return new SimpleAdapter(getActivity(),
				Collections.<MapWrapper<String, Object>> emptyList(),
				R.layout.fragment_goods_list_item, new String[] { "name",
						"price", "name", "name", "name" }, new int[] {
						R.id.name, R.id.price, R.id.quantity, R.id.addButton,
						R.id.reduceButton });
	}

	@Override
	protected long getGoodsShopId(MapWrapper<String, Object> goods) {
		return getArguments().getLong("shopId");
	}

	public void setGoodsType(MapWrapper<String, Object> goodsType) {
		this.goodsType = goodsType;
	}

}