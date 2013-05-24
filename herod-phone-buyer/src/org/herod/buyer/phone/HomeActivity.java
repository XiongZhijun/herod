package org.herod.buyer.phone;

import java.util.List;
import java.util.Map;

import org.herod.buyer.phone.HerodTask.AsyncTaskable;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;

public class HomeActivity extends BaseActivity implements
		AsyncTaskable<Object, List<Map<String, Object>>>, OnItemClickListener {
	private GridView shopTypesGridView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		showActionButton(R.id.queryButton, R.id.historyOrdersButton,
				R.id.shoppingCartButton);

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

	public void back(View v) {

	}
}