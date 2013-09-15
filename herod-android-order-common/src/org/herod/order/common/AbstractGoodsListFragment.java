/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.order.common;

import static org.herod.order.common.Constants.ID;
import static org.herod.order.common.Constants.SHOP_ID;

import java.util.List;

import org.herod.framework.HerodTask;
import org.herod.framework.HerodTask.AsyncTaskable;
import org.herod.framework.MapWrapper;
import org.herod.framework.RepeatedlyTask;
import org.herod.framework.adapter.SimpleAdapter;
import org.herod.framework.adapter.SimpleAdapter.ViewBinder;
import org.herod.framework.utils.ToastUtils;
import org.herod.framework.widget.XListView;
import org.herod.framework.widget.XListView.IXListViewListener;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * 
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 * 
 */
public abstract class AbstractGoodsListFragment extends
		ListViewFragment<XListView, Object, List<MapWrapper<String, Object>>>
		implements IXListViewListener, ViewBinder {
	protected SimpleAdapter adapter;
	private int count = 20;

	@Override
	protected void initListView(XListView listView) {
		listView.setPullRefreshEnable(false);
		listView.setPullLoadEnable(true);
		listView.setXListViewListener(this);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
	}

	@Override
	protected RepeatedlyTask<Object, List<MapWrapper<String, Object>>> createRepeatedlyTask(
			AsyncTaskable<Object, List<MapWrapper<String, Object>>> asyncTaskable) {
		return new RepeatedlyTask<Object, List<MapWrapper<String, Object>>>(
				asyncTaskable);
	}

	@Override
	public void onStart() {
		super.onStart();
		if (isLoadOnViewCreated()) {
			loadDataFromRemote();
		}
	}

	@Override
	public void onStop() {
		super.onStop();
		adapter = null;
	}

	@Override
	public void onResume() {
		super.onResume();
		if (adapter != null) {
			adapter.notifyDataSetChanged();
		}
	}

	protected abstract SimpleAdapter createAdapter(FragmentActivity activity);

	@Override
	public List<MapWrapper<String, Object>> runOnBackground(Object... params) {
		int begin = adapter != null ? adapter.getCount() : 0;
		return findPageGoods(begin, count);
	}

	protected abstract List<MapWrapper<String, Object>> findPageGoods(
			int begin, int count);

	protected abstract boolean isLoadOnViewCreated();

	@Override
	public void onPostExecute0(List<MapWrapper<String, Object>> data) {
		if (data.size() == 0) {
			ToastUtils.showToast("没有更多商品了！", Toast.LENGTH_SHORT);
			getListView().stopLoadMore();
		} else {
			if (adapter == null) {
				adapter = createAdapter(getActivity());
				adapter.setViewBinder(this);
				getListView().setAdapter(adapter);
			}
			adapter.addData(data);
			getListView().stopLoadMore();
		}
		getListView().setPullLoadEnable(data.size() >= count);
	}

	@Override
	public boolean setViewValue(final View dataSetView,
			final MapWrapper<String, Object> goods, final View view,
			String from, int to, int position, Object data,
			String textRepresentation) {
		final long goodsId = goods.getLong(ID);
		if (to == R.id.quantity) {
			int quantity = getShoppingCartCache().getQuantity(getShopId(goods),
					goodsId);
			setQuantity(dataSetView, quantity);
			return true;
		}
		if (to == R.id.reduceButton) {
			view.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					decrease(dataSetView, goods, goodsId);
				}
			});
		}
		if (to == R.id.addButton) {
			view.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					increase(dataSetView, goods);
				}
			});
		}
		return false;
	}

	protected long getShopId(MapWrapper<String, Object> goods) {
		return goods.getLong(SHOP_ID);
	}

	private void increase(final View dataSetView,
			final MapWrapper<String, Object> goods) {
		new HerodTask<Object, Integer>(new AsyncTaskable<Object, Integer>() {
			public Integer runOnBackground(Object... params) {
				return getShoppingCartCache().increase(getShopId(goods), goods);
			}

			public void onPostExecute(Integer quantity) {
				setQuantity(dataSetView, quantity);
			}
		}).execute();
	}

	private void decrease(View dataSetView, MapWrapper<String, Object> goods,
			long goodsId) {
		int quantity = getShoppingCartCache().decrease(getShopId(goods),
				goodsId);
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
		if (getActivity() instanceof QuantityChangedListener) {
			((QuantityChangedListener) getActivity()).updateTotalQuantity();
		}
	}

	public void clear() {
		if (adapter != null)
			adapter.clear();
	}

	@Override
	public void onRefresh() {
	}

	@Override
	public void onLoadMore() {
		executeLoadDataTask();
	}

	protected IShoppingCartCache getShoppingCartCache() {
		return (IShoppingCartCache) getActivity();
	}

	public static interface QuantityChangedListener {
		void updateTotalQuantity();
	}

	public static interface IShoppingCartCache {

		int getQuantity(long shopId, long goodsId);

		int increase(long shopId, MapWrapper<String, ?> goods);

		int decrease(long shopId, long goodsId);

	}
}