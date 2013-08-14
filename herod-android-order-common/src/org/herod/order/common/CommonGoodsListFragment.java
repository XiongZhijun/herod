/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.order.common;

import java.util.Collections;
import java.util.List;

import org.herod.framework.MapWrapper;
import org.herod.framework.adapter.SimpleAdapter;

import android.support.v4.app.FragmentActivity;

/**
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 * 
 */
public abstract class CommonGoodsListFragment extends AbstractGoodsListFragment {
	private MapWrapper<String, Object> goodsType;
	private int count = 0;

	@Override
	public void onResume() {
		super.onResume();
		count = 0;
	}

	@Override
	public List<MapWrapper<String, Object>> runOnBackground(Long... params) {
		long id = goodsType.getLong("id");
		return findGoodsesByType(id, count, 20);
	}

	protected abstract List<MapWrapper<String, Object>> findGoodsesByType(
			long goodsTypeId, int begin, int count);

	@Override
	public void onPostExecute(List<MapWrapper<String, Object>> data) {
		super.onPostExecute(data);
		if (data != null)
			count += data.size();
	}

	@Override
	protected SimpleAdapter createAdapter(FragmentActivity activity) {
		return new ImageLoaderAdapter(getActivity(),
				Collections.<MapWrapper<String, Object>> emptyList(),
				R.layout.fragment_goods_list_item,
				new String[] { "thumbnail", "name", "sellingPrice", "name",
						"name", "name", "unit" }, new int[] { R.id.image,
						R.id.name, R.id.price, R.id.quantity, R.id.addButton,
						R.id.reduceButton, R.id.unit });
	}

	public void setGoodsType(MapWrapper<String, Object> goodsType) {
		this.goodsType = goodsType;
	}

	@Override
	protected boolean isLoadOnResume() {
		return true;
	}

}