/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.buyer.phone.fragments;

import java.util.List;

import org.herod.buyer.phone.BaseActivity;
import org.herod.buyer.phone.HerodTask;
import org.herod.buyer.phone.HerodTask.AsyncTaskable;
import org.herod.buyer.phone.R;
import org.herod.buyer.phone.ShoppingCartCache;
import org.herod.framework.MapWrapper;
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
 * 
 * 
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 * 
 */
public abstract class AbstractGoodsListFragment extends Fragment implements
		AsyncTaskable<Long, List<MapWrapper<String, Object>>>,
		IXListViewListener, ViewBinder {
	protected XListView goodsListView;
	protected SimpleAdapter adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_goods_list, container,
				false);
		goodsListView = (XListView) view.findViewById(R.id.goodsListView);
		goodsListView.setPullRefreshEnable(false);
		goodsListView.setPullLoadEnable(true);
		goodsListView.setXListViewListener(this);
		adapter = createAdapter(getActivity());
		adapter.setViewBinder(this);
		goodsListView.setAdapter(adapter);
		return view;
	}

	protected abstract SimpleAdapter createAdapter(FragmentActivity activity);

	@Override
	public void onResume() {
		super.onResume();
		new HerodTask<Long, List<MapWrapper<String, Object>>>(this).execute();
	}

	@Override
	public void onPostExecute(List<MapWrapper<String, Object>> data) {
		adapter.addData(data);
		goodsListView.stopLoadMore();
	}

	@Override
	public boolean setViewValue(final View dataSetView,
			final MapWrapper<String, Object> dataSet, final View view,
			String from, int to, int position, Object data,
			String textRepresentation) {
		final long goodsId = dataSet.getLong("id");
		if (to == R.id.quantity) {
			int quantity = ShoppingCartCache.getInstance().getQuantity(
					getGoodsShopId(dataSet), goodsId);
			setQuantity(dataSetView, quantity);
			return true;
		}
		if (to == R.id.reduceButton) {
			view.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					decrease(dataSetView, dataSet, goodsId);
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

	protected abstract long getGoodsShopId(MapWrapper<String, Object> goods);

	private void increase(View dataSetView, MapWrapper<String, Object> dataSet) {
		int quantity = ShoppingCartCache.getInstance().increase(
				getGoodsShopId(dataSet), dataSet);
		setQuantity(dataSetView, quantity);
	}

	private void decrease(View dataSetView, MapWrapper<String, Object> dataSet,
			long goodsId) {
		int quantity = ShoppingCartCache.getInstance().decrease(
				getGoodsShopId(dataSet), goodsId);
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

	@Override
	public void onRefresh() {
	}

	@Override
	public void onLoadMore() {
		new HerodTask<Long, List<MapWrapper<String, Object>>>(this).execute();
	}

}