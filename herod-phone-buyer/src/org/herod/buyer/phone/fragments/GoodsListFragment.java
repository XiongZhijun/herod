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
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

/**
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 * 
 */
public class GoodsListFragment extends Fragment implements
		AsyncTaskable<Long, List<Map<String, Object>>>, IXListViewListener,
		ViewBinder {
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
		// goodsListView.setOnItemClickListener(this);
		goodsListView.setXListViewListener(this);
		adapter = new SimpleAdapter(getActivity(),
				Collections.<Map<String, Object>> emptyList(),
				R.layout.fragment_goods_list_item, new String[] { "name",
						"price", "name", "name", "name" }, new int[] {
						R.id.name, R.id.price, R.id.quantity, R.id.addButton,
						R.id.reduceButton });
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
		final long id = (Long) dataSet.get("id");
		if (to == R.id.quantity) {
			setQuantity(dataSetView, id);
			return true;
		}
		if (to == R.id.reduceButton) {
			view.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					decrease(dataSetView, id);
				}
			});
		}
		if (to == R.id.addButton) {
			view.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					increase(dataSetView, id);
				}
			});
		}
		return false;
	}

	private void increase(View dataSetView, long id) {
		int quantity = getQuantity(id);
		quantity++;

		if (quantity <= 0) {
			goodsQuantityMap.remove(id);
		} else if (quantity > 99) {
			Toast.makeText(getActivity(), "一次购买某个商品的数量不能超过99！",
					Toast.LENGTH_SHORT).show();
			goodsQuantityMap.put(id, 99);
		} else {
			goodsQuantityMap.put(id, quantity);
		}
		setQuantity(dataSetView, id);
	}

	private void decrease(View dataSetView, long id) {
		int quantity = getQuantity(id);
		quantity--;
		if (quantity <= 0) {
			goodsQuantityMap.remove(id);
		} else {
			goodsQuantityMap.put(id, quantity);
		}
		setQuantity(dataSetView, id);
	}

	private void setQuantity(View dataSetView, Long id) {
		int quantity = getQuantity(id);
		TextView quantityView = (TextView) dataSetView
				.findViewById(R.id.quantity);
		View reduceButton = dataSetView.findViewById(R.id.reduceButton);
		if (quantity > 0) {
			quantityView.setText(quantity + "");
			quantityView.setVisibility(View.VISIBLE);
			reduceButton.setVisibility(View.VISIBLE);
		} else {
			quantityView.setVisibility(View.INVISIBLE);
			reduceButton.setVisibility(View.INVISIBLE);
		}
	}

	private int getQuantity(Long id) {
		Integer quantity = goodsQuantityMap.get(id);
		quantity = quantity == null || quantity < 0 ? 0 : quantity;
		return quantity;
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