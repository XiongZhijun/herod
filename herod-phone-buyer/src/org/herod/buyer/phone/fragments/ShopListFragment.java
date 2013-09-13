/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.buyer.phone.fragments;

import static org.herod.buyer.phone.Constants.CONTACT_NUMBER;
import static org.herod.buyer.phone.Constants.IMAGE_URL;
import static org.herod.buyer.phone.Constants.SERVICE_TIMES;
import static org.herod.buyer.phone.Constants.SHOP_TYPE_ID;
import static org.herod.order.common.Constants.ID;
import static org.herod.order.common.Constants.NAME;
import static org.herod.order.common.Constants.TIMESTAMP;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.herod.buyer.phone.BuyerContext;
import org.herod.buyer.phone.GoodsListActivity;
import org.herod.buyer.phone.R;
import org.herod.framework.HerodTask.AsyncTaskable;
import org.herod.framework.MapWrapper;
import org.herod.framework.RepeatedlyTask;
import org.herod.framework.ViewFindable;
import org.herod.framework.adapter.SimpleAdapter.ViewBinder;
import org.herod.order.common.ImageLoaderAdapter;
import org.herod.order.common.ListViewFragment;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.nostra13.universalimageloader.core.assist.PauseOnScrollListener;
import com.nostra13.universalimageloader.utils.ImageLoaderUtils;

/**
 * 
 * 
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 * 
 */
public class ShopListFragment extends
		ListViewFragment<GridView, Object, List<MapWrapper<String, Object>>>
		implements ViewFindable, OnItemClickListener {
	private Map<Long, Boolean> shopStatusMap = new HashMap<Long, Boolean>();

	@Override
	protected int getLayoutResources() {
		return R.layout.fragment_shop_list;
	}

	@Override
	protected void initListView(GridView listView) {
		listView.setOnItemClickListener(this);
		listView.setOnScrollListener(new PauseOnScrollListener(ImageLoaderUtils
				.getImageLoader(), false, true));
	}

	@Override
	protected RepeatedlyTask<Object, List<MapWrapper<String, Object>>> createRepeatedlyTask(
			AsyncTaskable<Object, List<MapWrapper<String, Object>>> asyncTaskable) {
		return new RepeatedlyTask<Object, List<MapWrapper<String, Object>>>(
				asyncTaskable);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		loadDataFromRemote();
	}

	@Override
	protected void onPostExecute0(List<MapWrapper<String, Object>> shops) {
		shopStatusMap.clear();
		ImageLoaderAdapter adapter = new ImageLoaderAdapter(getActivity(),
				shops, R.layout.fragment_shop_list_shop_item, new String[] {
						NAME, IMAGE_URL, SERVICE_TIMES }, new int[] {
						R.id.name, R.id.image, R.id.noServicePanel });
		ViewBinder viewBinder = new ShopViewBinder();
		adapter.setViewBinder(viewBinder);
		getListView().setAdapter(adapter);

	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public List<MapWrapper<String, Object>> runOnBackground(Object... params) {
		long shopTypeId = getArguments().getLong(SHOP_TYPE_ID);
		return BuyerContext.getBuyerService().findShopesByType(shopTypeId);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onItemClick(AdapterView<?> adapterView, View arg1,
			int position, long arg3) {
		MapWrapper<String, Object> shop = (MapWrapper<String, Object>) adapterView
				.getItemAtPosition(position);
		Long id = shop.getLong(ID);
		if (shopStatusMap.containsKey(id) && !shopStatusMap.get(id)) {
			return;
		}
		GoodsListActivity.start(getActivity(), shop.getLong(ID),
				shop.getString(NAME), shop.getString(CONTACT_NUMBER),
				shop.getLong(TIMESTAMP), false);
	}

	class ShopViewBinder implements ViewBinder {
		public boolean setViewValue(View dataSetView,
				MapWrapper<String, Object> dataSet, View view, String from,
				int to, int position, Object data, String textRepresentation) {
			if (to != R.id.noServicePanel) {
				return false;
			}
			String serviceTimes = (String) data;
			ServiceTimeManager serviceTimeManager = new ServiceTimeManager(
					serviceTimes);
			boolean inServiceNow = serviceTimeManager.isInServiceNow();
			shopStatusMap.put(dataSet.getLong(ID), inServiceNow);
			if (inServiceNow) {
				view.setVisibility(View.GONE);
			} else {
				view.setVisibility(View.VISIBLE);
				serviceTimeManager.updateServiceTimeViews(dataSetView);
			}
			return true;
		}

	}

}
