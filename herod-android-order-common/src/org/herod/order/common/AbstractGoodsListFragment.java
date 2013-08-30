/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.order.common;

import java.util.List;

import org.herod.framework.BaseFragment;
import org.herod.framework.HerodTask.AsyncTaskable;
import org.herod.framework.MapWrapper;
import org.herod.framework.RepeatedlyTask;
import org.herod.framework.ViewFindable;
import org.herod.framework.adapter.SimpleAdapter;
import org.herod.framework.adapter.SimpleAdapter.ViewBinder;
import org.herod.framework.utils.ToastUtils;
import org.herod.framework.widget.XListView;
import org.herod.framework.widget.XListView.IXListViewListener;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * 
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 * 
 */
public abstract class AbstractGoodsListFragment extends BaseFragment implements
		ViewFindable, AsyncTaskable<Object, List<MapWrapper<String, Object>>>,
		IXListViewListener, ViewBinder {
	protected XListView goodsListView;
	protected SimpleAdapter adapter;
	private boolean isFirstLoad = true;
	private int begin = 0;
	private int count = 30;
	private RefreshButtonHelper refreshButtonHelper;
	private RepeatedlyTask<Object, List<MapWrapper<String, Object>>> loadGoodsTask;

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

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		loadGoodsTask = new RepeatedlyTask<Object, List<MapWrapper<String, Object>>>(
				this, false);
		refreshButtonHelper = new RefreshButtonHelper(this, loadGoodsTask,
				R.id.refreshButton, R.id.goodsListView);
	}

	@Override
	public void onResume() {
		super.onResume();
		begin = 0;
		isFirstLoad = true;
		if (isLoadOnResume()) {
			loadGoods();
		}
	}

	protected abstract SimpleAdapter createAdapter(FragmentActivity activity);

	@Override
	public List<MapWrapper<String, Object>> runOnBackground(Object... params) {
		return findPageGoods(begin, count);
	}

	protected abstract List<MapWrapper<String, Object>> findPageGoods(
			int begin, int count);

	protected abstract boolean isLoadOnResume();

	@Override
	public void onPostExecute(List<MapWrapper<String, Object>> data) {
		if (refreshButtonHelper.checkNullResult(data)) {
			begin = 0;
			return;
		}
		if (data.size() == 0) {
			if (!isFirstLoad) {
				ToastUtils.showToast("没有更多商品了！", Toast.LENGTH_SHORT);
			}
			goodsListView.stopLoadMore();
			goodsListView.setPullLoadEnable(false);
		} else {
			adapter.addData(data);
			begin += data.size();
			goodsListView.stopLoadMore();
		}
		isFirstLoad = false;
	}

	@Override
	public boolean setViewValue(final View dataSetView,
			final MapWrapper<String, Object> goods, final View view,
			String from, int to, int position, Object data,
			String textRepresentation) {
		final long goodsId = goods.getLong("id");
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
		return goods.getLong("shopId");
	}

	private void increase(View dataSetView, MapWrapper<String, Object> goods) {
		int quantity = getShoppingCartCache().increase(getShopId(goods), goods);
		setQuantity(dataSetView, quantity);
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
		begin = 0;
	}

	@Override
	public void onRefresh() {
	}

	@Override
	public void onLoadMore() {
		loadGoods();
	}

	public void loadGoods() {
		loadGoodsTask.execute(getActivity());
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