/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.buyer.phone.fragments;

import java.util.Collections;
import java.util.List;

import org.herod.buyer.phone.BuyerContext;
import org.herod.buyer.phone.R;
import org.herod.buyer.phone.ShoppingCartCache;
import org.herod.framework.MapWrapper;
import org.herod.framework.adapter.SimpleAdapter;
import org.herod.order.common.AbstractGoodsListFragment;
import org.herod.order.common.ImageLoaderAdapter;

import android.support.v4.app.FragmentActivity;
import static org.herod.buyer.phone.Constants.*;

/**
 * 
 * 
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 * 
 */
public class SearchGoodsListFragment extends AbstractGoodsListFragment {

	private String goodsName;

	@Override
	public List<MapWrapper<String, Object>> findPageGoods(int begin, int count) {
		List<MapWrapper<String, Object>> goodses = BuyerContext
				.getBuyerService().searchGoodses(goodsName, begin, count);
		begin += count;
		return goodses;
	}

	@Override
	protected SimpleAdapter createAdapter(FragmentActivity activity) {
		return new ImageLoaderAdapter(activity,
				Collections.<MapWrapper<String, Object>> emptyList(),
				R.layout.activity_goods_search_goods_item, new String[] {
						THUMBNAIL, NAME, SELLING_PRICE, NAME, NAME, NAME,
						SHOP_NAME, UNIT }, new int[] { R.id.image, R.id.name,
						R.id.price, R.id.quantity, R.id.addButton,
						R.id.reduceButton, R.id.shopName, R.id.unit });
	}

	public void query(String query) {
		goodsName = query;
		clear();
		loadDataFromRemote();
	}

	@Override
	protected boolean isLoadOnViewCreated() {
		return false;
	}

	protected IShoppingCartCache getShoppingCartCache() {
		return ShoppingCartCache.getInstance();
	}
}
