/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.buyer.phone.fragments;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.herod.buyer.phone.BuyerContext;
import org.herod.buyer.phone.HerodTask;
import org.herod.buyer.phone.HerodTask.AsyncTaskable;
import org.herod.buyer.phone.R;
import org.herod.framework.adapter.SimpleAdapter;
import org.herod.framework.adapter.SimpleAdapter.ViewBinder;
import org.herod.framework.widget.XListView;
import org.herod.framework.widget.XListView.IXListViewListener;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

/**
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 * 
 */
public class GoodsListFragment extends Fragment implements
		AsyncTaskable<Long, List<Map<String, Object>>>, OnItemClickListener,
		IXListViewListener, ViewBinder {
	private XListView goodsListView;
	private Map<String, Object> goodsType;
	private long latestGoodsId = 0;
	private SimpleAdapter adapter;
	private Map<Long, Integer> goodsQuantityMap = new HashMap<Long, Integer>();

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
						"price", "name", "name" }, new int[] { R.id.name,
						R.id.price, R.id.quantity, R.id.deleteItem });
		adapter.setViewBinder(this);
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
	public boolean setViewValue(final View dataSetView,
			final Map<String, ?> dataSet, final View view, String from, int to,
			int position, Object data, String textRepresentation) {
		if (to == R.id.quantity) {
			decreaseQuantity((Long) dataSet.get("id"), view,
					dataSetView.findViewById(R.id.deleteItem));
			return true;
		}
		if (to == R.id.deleteItem) {
			view.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					decreaseQuantity((Long) dataSet.get("id"),
							dataSetView.findViewById(R.id.quantity), view);
				}
			});
		}
		return false;
	}

	private void decreaseQuantity(long id, View quantityView,
			View decreaseButton) {
		int quantity = 0;
		if (goodsQuantityMap.containsKey(id)) {
			quantity = goodsQuantityMap.get(id);
			quantity--;
		}
		if (quantity > 0) {
			((TextView) quantityView).setText(quantity + "");
			quantityView.setVisibility(View.VISIBLE);
			decreaseButton.setVisibility(View.VISIBLE);
		} else {
			quantity = 0;
			quantityView.setVisibility(View.INVISIBLE);
			decreaseButton.setVisibility(View.INVISIBLE);
		}
		goodsQuantityMap.put(id, quantity);
	}

	private void increaseQuantity(long id, View quantityView,
			View decreaseButton) {
		int quantity = 1;
		if (goodsQuantityMap.containsKey(id)) {
			quantity = goodsQuantityMap.get(id);
			quantity++;
		}
		if (quantity > 0) {
			((TextView) quantityView).setText(quantity + "");
			quantityView.setVisibility(View.VISIBLE);
			decreaseButton.setVisibility(View.VISIBLE);
		} else {
			quantity = 0;
			quantityView.setVisibility(View.INVISIBLE);
			decreaseButton.setVisibility(View.INVISIBLE);
		}
		goodsQuantityMap.put(id, quantity);
	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View view,
			int position, long itemId) {
		Map<String, Object> item = (Map<String, Object>) adapterView
				.getItemAtPosition(position);
		long id = (Long) item.get("id");
		increaseQuantity(id, view.findViewById(R.id.quantity),
				view.findViewById(R.id.deleteItem));

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