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
import org.herod.framework.BaseFragment;
import org.herod.framework.HerodTask.AsyncTaskable;
import org.herod.framework.MapWrapper;
import org.herod.framework.RepeatedlyTask;
import org.herod.framework.ViewFindable;
import org.herod.framework.adapter.SimpleAdapter.ViewBinder;
import org.herod.order.common.ImageLoaderAdapter;
import org.herod.order.common.RefreshButtonHelper;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
public class ShopListFragment extends BaseFragment implements ViewFindable,
		AsyncTaskable<Object, List<MapWrapper<String, Object>>>,
		OnItemClickListener {
	private GridView shopsGridView;
	private RefreshButtonHelper refreshButtonHelper;
	private RepeatedlyTask<Object, List<MapWrapper<String, Object>>> loadShopsTask;
	private Map<Long, Boolean> shopStatusMap = new HashMap<Long, Boolean>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_shop_list, container,
				false);
		shopsGridView = (GridView) view.findViewById(R.id.shopsGridView);
		shopsGridView.setOnItemClickListener(this);
		return view;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		loadShopsTask = new RepeatedlyTask<Object, List<MapWrapper<String, Object>>>(
				this);
		refreshButtonHelper = new RefreshButtonHelper(this, loadShopsTask,
				R.id.refreshButton, R.id.shopsGridView);
	}

	@Override
	public void onResume() {
		super.onResume();
		shopStatusMap.clear();
		loadShopsTask.execute(getActivity());
	}

	@Override
	public List<MapWrapper<String, Object>> runOnBackground(Object... params) {
		long shopTypeId = getArguments().getLong(SHOP_TYPE_ID);
		return BuyerContext.getBuyerService().findShopesByType(shopTypeId);
	}

	@Override
	public void onPostExecute(List<MapWrapper<String, Object>> shops) {
		if (refreshButtonHelper.checkNullResult(shops)) {
			return;
		}
		ImageLoaderAdapter adapter = new ImageLoaderAdapter(getActivity(),
				shops, R.layout.fragment_shop_list_shop_item, new String[] {
						NAME, IMAGE_URL, SERVICE_TIMES }, new int[] {
						R.id.name, R.id.image, R.id.noServicePanel });
		ViewBinder viewBinder = new ShopViewBinder();
		adapter.setViewBinder(viewBinder);
		shopsGridView.setAdapter(adapter);
		shopsGridView.setOnScrollListener(new PauseOnScrollListener(
				ImageLoaderUtils.getImageLoader(), false, true));
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
