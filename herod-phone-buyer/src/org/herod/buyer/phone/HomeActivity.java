package org.herod.buyer.phone;

import java.util.List;
import java.util.Map;

import org.herod.buyer.phone.HerodTask.AsyncTaskable;
import org.herod.framework.widget.ActionBar;
import org.herod.framework.widget.ActionBar.Action;
import org.herod.framework.widget.ActionBar.IntentAction;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;

public class HomeActivity extends Activity implements
		AsyncTaskable<Object, List<Map<String, Object>>>, OnItemClickListener {
	private GridView shopTypesGridView;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

		ActionBar actionBar = (ActionBar) findViewById(R.id.actionbar);

		Action shareAction = new IntentAction(this, createShareIntent(),
				R.drawable.ic_title_share_default);
		actionBar.addAction(shareAction);
		Action shoppingCartAction = new IntentAction(this, new Intent(this,
				ShoppingCartActivity.class), R.drawable.shopping_cart);
		actionBar.addAction(shoppingCartAction);

		shopTypesGridView = (GridView) findViewById(R.id.shopTypesGrid);
		shopTypesGridView.setOnItemClickListener(this);

		new HerodTask<Object, List<Map<String, Object>>>(this).execute();
	}

	@SuppressWarnings("unchecked")
	public void onItemClick(AdapterView<?> adapterView, View view,
			int position, long id) {
		Intent intent = new Intent(HomeActivity.this, StoresActivity.class);
		intent.putExtra("position", position);
		Map<String, Object> item = (Map<String, Object>) adapterView
				.getItemAtPosition(position);
		intent.putExtra("typeId", (Long) item.get("id"));
		startActivity(intent);
	}

	public void showTakeOutStores(View view) {
		startActivity(new Intent(this, StoresActivity.class));
	}

	public void showConvenienceStores(View view) {
		startActivity(new Intent(this, StoresActivity.class));

	}

	public static Intent createIntent(Context context) {
		Intent i = new Intent(context, HomeActivity.class);
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		return i;
	}

	private Intent createShareIntent() {
		final Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_TEXT, "Shared from the ActionBar widget.");
		return Intent.createChooser(intent, "Share");
	}

	@Override
	public List<Map<String, Object>> runOnBackground(Object... params) {
		return BuyerContext.getBuyerService().findShopTypes();
	}

	@Override
	public void onPostExecute(List<Map<String, Object>> data) {
		ListAdapter adapter = new SimpleAdapter(this, data,
				R.layout.activity_home_shop_type_item, new String[] { "name" },
				new int[] { R.id.name });
		shopTypesGridView.setAdapter(adapter);
	}
}