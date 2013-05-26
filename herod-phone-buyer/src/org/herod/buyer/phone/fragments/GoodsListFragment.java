/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.buyer.phone.fragments;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.herod.buyer.phone.BaseActivity;
import org.herod.buyer.phone.BuyerContext;
import org.herod.buyer.phone.HerodTask;
import org.herod.buyer.phone.HerodTask.AsyncTaskable;
import org.herod.buyer.phone.R;
import org.herod.buyer.phone.ShoppingCartCache;
import org.herod.framework.adapter.SimpleAdapter;
import org.herod.framework.adapter.SimpleAdapter.ViewBinder;
import org.herod.framework.widget.XListView;
import org.herod.framework.widget.XListView.IXListViewListener;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
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
	private long shopId;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_goods_list, container,
				false);
		shopId = getArguments().getLong("shopId");
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
		final long goodsId = (Long) dataSet.get("id");
		if (to == R.id.quantity) {
			int quantity = ShoppingCartCache.getInstance().getQuantity(shopId,
					goodsId);
			setQuantity(dataSetView, quantity);
			return true;
		}
		if (to == R.id.reduceButton) {
			view.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					decrease(dataSetView, goodsId);
				}
			});
		}
		if (to == R.id.addButton) {
			view.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					increase(dataSetView, dataSet);
				}
			});
		}
		return false;
	}

	private void increase(View dataSetView, Map<String, ?> dataSet) {
		int quantity = ShoppingCartCache.getInstance()
				.increase(shopId, dataSet);
		setQuantity(dataSetView, quantity);
	}

	private void decrease(View dataSetView, long goodsId) {
		int quantity = ShoppingCartCache.getInstance()
				.decrease(shopId, goodsId);
		setQuantity(dataSetView, quantity);
	}

	private void setQuantity(View dataSetView, int quantity) {
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
		FragmentActivity activity = getActivity();
		if (activity instanceof BaseActivity) {
			((BaseActivity) activity).updateTotalQuantity();
		}
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