/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.buyer.phone.fragments;

import java.util.List;

import org.herod.buyer.phone.BuyerContext;
import org.herod.buyer.phone.GoodsListActivity;
import org.herod.buyer.phone.R;
import org.herod.framework.HerodTask;
import org.herod.framework.HerodTask.AsyncTaskable;
import org.herod.framework.MapWrapper;
import org.herod.framework.ViewFindable;
import org.herod.order.common.ImageLoaderAdapter;
import org.herod.order.common.RefreshButtonHelper;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
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
public class ShopListFragment extends Fragment implements ViewFindable,
		AsyncTaskable<Long, List<MapWrapper<String, Object>>>,
		OnItemClickListener {
	private GridView shopsGridView;
	private RefreshButtonHelper refreshButtonHelper;

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
		refreshButtonHelper = new RefreshButtonHelper(this, R.id.refreshButton,
				new OnClickListener() {
					public void onClick(View arg0) {
						refresh();
					}
				}, R.id.shopsGridView);
	}

	@Override
	public void onResume() {
		super.onResume();
		refresh();
	}

	private void refresh() {
		long shopTypeId = getArguments().getLong("shopTypeId");
		new HerodTask<Long, List<MapWrapper<String, Object>>>(this)
				.execute(shopTypeId);
	}

	@Override
	public List<MapWrapper<String, Object>> runOnBackground(Long... params) {
		return BuyerContext.getBuyerService().findShopesByType(params[0]);
	}

	@Override
	public void onPostExecute(List<MapWrapper<String, Object>> data) {
		if (refreshButtonHelper.checkNullResult(data)) {
			return;
		}
		ImageLoaderAdapter adapter = new ImageLoaderAdapter(getActivity(),
				data, R.layout.fragment_shop_list_shop_item, new String[] {
						"name", "imageUrl" },
				new int[] { R.id.name, R.id.image });
		shopsGridView.setAdapter(adapter);
		shopsGridView.setOnScrollListener(new PauseOnScrollListener(
				ImageLoaderUtils.getImageLoader(), false, true));
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onItemClick(AdapterView<?> adapterView, View arg1,
			int position, long arg3) {
		Intent intent = new Intent(getActivity(), GoodsListActivity.class);
		MapWrapper<String, Object> item = (MapWrapper<String, Object>) adapterView
				.getItemAtPosition(position);
		intent.putExtra("shopId", item.getLong("id"));
		intent.putExtra("shopName", item.getString("name"));
		startActivity(intent);
	}

	@Override
	public View findViewById(int id) {
		return getView().findViewById(id);
	}

}
