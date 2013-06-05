package org.herod.buyer.phone;

import java.util.List;

import org.herod.buyer.phone.HerodTask.AsyncTaskable;
import org.herod.framework.MapWrapper;
import org.herod.framework.adapter.SimpleAdapter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ListAdapter;

import com.nostra13.universalimageloader.utils.ImageLoaderUtils;

public class HomeActivity extends BaseActivity implements
		AsyncTaskable<Object, List<MapWrapper<String, Object>>>,
		OnItemClickListener {
	private GridView shopTypesGridView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ImageLoaderUtils.initImageLoader(this);
		setContentView(R.layout.activity_home);

		shopTypesGridView = (GridView) findViewById(R.id.shopTypesGrid);
		shopTypesGridView.setOnItemClickListener(this);

		new HerodTask<Object, List<MapWrapper<String, Object>>>(this).execute();
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@SuppressWarnings("unchecked")
	public void onItemClick(AdapterView<?> adapterView, View view,
			int position, long id) {
		Intent intent = new Intent(HomeActivity.this, StoresActivity.class);
		intent.putExtra("position", position);
		MapWrapper<String, Object> item = (MapWrapper<String, Object>) adapterView
				.getItemAtPosition(position);
		intent.putExtra("typeId", item.getLong("id"));
		startActivity(intent);
	}

	@Override
	public List<MapWrapper<String, Object>> runOnBackground(Object... params) {
		return BuyerContext.getBuyerService().findShopTypes();
	}

	@Override
	public void onPostExecute(List<MapWrapper<String, Object>> data) {
		ListAdapter adapter = new SimpleAdapter(this, data,
				R.layout.activity_home_shop_type_item, new String[] { "name" },
				new int[] { R.id.name });
		shopTypesGridView.setAdapter(adapter);
	}

	public void back(View v) {

	}


}