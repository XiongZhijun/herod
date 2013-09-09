/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.buyer.phone;

import static org.herod.order.common.Constants.SHOP_ID;
import static org.herod.order.common.Constants.SHOP_NAME;
import static org.herod.order.common.Constants.SHOP_PHONE;
import static org.herod.order.common.Constants.TEL;
import static org.herod.order.common.Constants.TIMESTAMP;

import java.util.List;

import org.herod.framework.MapWrapper;
import org.herod.order.common.AbstractGoodsListActivity;
import org.herod.order.common.AbstractGoodsListFragment.QuantityChangedListener;
import org.herod.order.common.AbstractGoodsTypeGoodsListFragment;
import org.herod.order.common.Constants;

import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.TextView;

/**
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 * 
 */
public class GoodsListActivity extends AbstractGoodsListActivity implements
		QuantityChangedListener {
	/**  */
	private static final String FROM_SHOPPING_CART = "fromShoppingCart";
	private Menu menu;
	protected SearchView searchview;

	@Override
	protected List<MapWrapper<String, Object>> findGoodsTypesByShop(long shopId) {
		long timestamp = getIntent().getLongExtra(Constants.TIMESTAMP, 0);
		return BuyerContext.getBuyerService().findGoodsTypesByShop(shopId,
				timestamp);
	}

	@Override
	protected AbstractGoodsTypeGoodsListFragment createGoodsListFragment() {
		return new GoodsListFragment();
	}

	@Override
	protected void onResume() {
		super.onResume();
		updateTotalQuantity();
		if (searchview != null && !searchview.isIconified()) {
			searchview.setIconified(true);
		}
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		int menuConfigResource = getMenuConfigResource();
		if (menuConfigResource <= 0) {
			return false;
		}
		getMenuInflater().inflate(menuConfigResource, menu);
		this.menu = menu;
		initSearch(menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		boolean done = super.onOptionsItemSelected(item);
		if (done) {
			return done;
		}
		switch (item.getItemId()) {
		case R.id.historyOrders:
			showHistoryOrders(item);
			return true;
		case R.id.shoppingCart:
			showShoppingCart(item);
			return true;
		case R.id.dialing:
			String phone = getIntent().getStringExtra(SHOP_PHONE);
			Uri uri = Uri.parse(TEL + phone);
			Intent it = new Intent(Intent.ACTION_DIAL, uri);
			startActivity(it);
			return true;
		default:
			return false;
		}
	}

	public void showHistoryOrders(MenuItem item) {
		startActivity(new Intent(this, HisttoryOrdersActivity.class));
	}

	public void showShoppingCart(MenuItem item) {
		startActivity(new Intent(this, ShoppingCartActivity.class));
	}

	private void initSearch(Menu menu) {
		MenuItem search = menu.findItem(R.id.search);
		if (search == null) {
			return;
		}
		searchview = (SearchView) search.getActionView();
		searchview.setMaxWidth(10000);
		searchview.setIconifiedByDefault(isSearchViewIconified());
		searchview.setOnQueryTextListener(new OnQueryTextListener() {
			public boolean onQueryTextSubmit(String query) {
				searchview.setIconified(true);
				return false;
			}

			public boolean onQueryTextChange(String newText) {
				return false;
			}
		});
		searchview.setOnSearchClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (!searchview.isIconified()) {
					showBackAction(true);
				}
			}
		});
		SearchManager mSearchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		SearchableInfo info = mSearchManager
				.getSearchableInfo(getComponentName());
		searchview.setSearchableInfo(info);
	}

	protected boolean isSearchViewIconified() {
		return true;
	}

	protected int getMenuConfigResource() {
		boolean isFromShoppingCart = getIntent().getBooleanExtra(
				FROM_SHOPPING_CART, false);
		if (isFromShoppingCart) {
			return R.menu.shop_goods_activity_menu1;
		} else {
			return R.menu.shop_goods_activity_menu2;
		}
	}

	public void updateTotalQuantity() {
		int quantity = ShoppingCartCache.getInstance().getTotalQuantity();
		if (menu == null) {
			return;
		}
		MenuItem item = menu.findItem(R.id.shoppingCart);
		if (item == null) {
			return;
		}
		View actionView = item.getActionView();
		if (actionView == null) {
			return;
		}
		View totalQuantityView = actionView.findViewById(R.id.totalQuantity);
		if (totalQuantityView != null) {
			((TextView) totalQuantityView).setText(quantity + "");
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_MENU && searchview != null
				&& !searchview.isIconified()) {
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onBackPressed() {
		if (searchview != null && !searchview.isIconified()) {
			searchview.setIconified(true);
			searchview.setIconified(true);
			showBackAction(canBack());
			return;
		}
		super.onBackPressed();
	}

	protected boolean canBack() {
		return true;
	}

	public static void start(Context context, long shopId, String shopName,
			String shopPhone, long shopTimestamp, boolean fromShoppingCart) {
		Intent intent = new Intent(context, GoodsListActivity.class);
		intent.putExtra(SHOP_ID, shopId);
		intent.putExtra(SHOP_NAME, shopName);
		intent.putExtra(SHOP_PHONE, shopPhone);
		intent.putExtra(TIMESTAMP, shopTimestamp);
		intent.putExtra(FROM_SHOPPING_CART, fromShoppingCart);

		context.startActivity(intent);
	}

	public static class GoodsListFragment extends
			AbstractGoodsTypeGoodsListFragment {
		@Override
		protected List<MapWrapper<String, Object>> findGoodsesByType(
				long goodsTypeId, int begin, int count) {
			long timestamp = getArguments().getLong(Constants.TIMESTAMP);
			return BuyerContext.getBuyerService().findGoodsesByType(
					goodsTypeId, begin, count, timestamp);
		}

		protected IShoppingCartCache getShoppingCartCache() {
			return ShoppingCartCache.getInstance();
		}
	}

}
