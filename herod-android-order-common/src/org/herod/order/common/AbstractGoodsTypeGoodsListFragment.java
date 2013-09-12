/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.order.common;

import static org.herod.order.common.Constants.GOODS_TYPE_ID;
import static org.herod.order.common.Constants.NAME;
import static org.herod.order.common.Constants.SELLING_PRICE;
import static org.herod.order.common.Constants.THUMBNAIL;
import static org.herod.order.common.Constants.UNIT;

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
public abstract class AbstractGoodsTypeGoodsListFragment extends
		AbstractGoodsListFragment {

	@Override
	public List<MapWrapper<String, Object>> findPageGoods(int begin, int count) {
		long goodsTypeId = getArguments().getLong(GOODS_TYPE_ID);
		return findGoodsesByType(goodsTypeId, begin, count);
	}

	protected abstract List<MapWrapper<String, Object>> findGoodsesByType(
			long goodsTypeId, int begin, int count);

	@Override
	protected SimpleAdapter createAdapter(FragmentActivity activity) {
		return new ImageLoaderAdapter(getActivity(),
				Collections.<MapWrapper<String, Object>> emptyList(),
				R.layout.fragment_goods_list_item, new String[] { THUMBNAIL,
						NAME, SELLING_PRICE, NAME, NAME, NAME, UNIT },
				new int[] { R.id.image, R.id.name, R.id.price, R.id.quantity,
						R.id.addButton, R.id.reduceButton, R.id.unit });
	}

	@Override
	protected boolean isLoadOnViewCreated() {
		return true;
	}

}