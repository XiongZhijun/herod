/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.buyer.phone.fragments;

import java.util.List;
import java.util.Map;

import org.herod.buyer.phone.BuyerContext;
import org.herod.buyer.phone.GoodsListActivity;
import org.herod.buyer.phone.HerodTask;
import org.herod.buyer.phone.HerodTask.AsyncTaskable;
import org.herod.buyer.phone.R;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.SimpleAdapter;

/**
 * 
 * 
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 * 
 */
public class ShopListFragment extends Fragment implements
		AsyncTaskable<Long, List<Map<String, Object>>>, OnItemClickListener {
	private GridView shopsGridView;

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
	public void onResume() {
		super.onResume();
		new HerodTask<Long, List<Map<String, Object>>>(this).execute();
	}

	@Override
	public List<Map<String, Object>> runOnBackground(Long... params) {
		return BuyerContext.getBuyerService().findShopesByType(1);
	}

	@Override
	public void onPostExecute(List<Map<String, Object>> data) {
		SimpleAdapter adapter = new SimpleAdapter(getActivity(), data,
				R.layout.fragment_shop_list_shop_item, new String[] { "name" },
				new int[] { R.id.name });
		shopsGridView.setAdapter(adapter);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onItemClick(AdapterView<?> adapterView, View arg1,
			int position, long arg3) {
		Intent intent = new Intent(getActivity(), GoodsListActivity.class);
		Map<String, Object> item = (Map<String, Object>) adapterView
				.getItemAtPosition(position);
		intent.putExtra("shopId", (Long) item.get("id"));
		startActivity(intent);
	}

}
