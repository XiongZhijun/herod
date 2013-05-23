/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.buyer.phone.fragments;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.herod.buyer.phone.BuyerContext;
import org.herod.buyer.phone.HerodTask;
import org.herod.buyer.phone.HerodTask.AsyncTaskable;
import org.herod.buyer.phone.R;
import org.herod.framework.adapter.SimpleAdapter;
import org.herod.framework.widget.XListView;
import org.herod.framework.widget.XListView.IXListViewListener;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 * 
 */
public class GoodsListFragment extends Fragment implements
		AsyncTaskable<Long, List<Map<String, Object>>>, OnItemClickListener,
		IXListViewListener {
	private XListView goodsListView;
	private Map<String, Object> goodsType;
	private long latestGoodsId = 0;
	private SimpleAdapter adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_goods_list, container,
				false);
		goodsListView = (XListView) view.findViewById(R.id.goodsListView);
		goodsListView.setPullRefreshEnable(false);
		goodsListView.setPullLoadEnable(true);
		goodsListView.setOnItemClickListener(this);
		goodsListView.setXListViewListener(this);
		adapter = new SimpleAdapter(getActivity(),
				Collections.<Map<String, Object>> emptyList(),
				R.layout.fragment_goods_list_item, new String[] { "name",
						"price" }, new int[] { R.id.name, R.id.price });
		goodsListView.setAdapter(adapter);
		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
		latestGoodsId = 0;
		new HerodTask<Long, List<Map<String, Object>>>(this).execute();
	}

	@Override
	public List<Map<String, Object>> runOnBackground(Long... params) {
		return BuyerContext.getBuyerService().findGoodsesByType(
				(Long) goodsType.get("id"), latestGoodsId, 20);
	}

	@Override
	public void onPostExecute(List<Map<String, Object>> data) {
		adapter.addData(data);
		latestGoodsId = (Long) data.get(data.size() - 1).get("id");
		goodsListView.stopLoadMore();
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

	}

	public void setGoodsType(Map<String, Object> goodsType) {
		this.goodsType = goodsType;
	}

	@Override
	public void onRefresh() {
	}

	@Override
	public void onLoadMore() {
		new HerodTask<Long, List<Map<String, Object>>>(this).execute();
	}

}