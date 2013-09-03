package org.herod.buyer.phone;

import static org.herod.buyer.phone.Constants.IMAGE_URL;
import static org.herod.buyer.phone.Constants.POSITION;
import static org.herod.buyer.phone.Constants.TYPE_ID;
import static org.herod.order.common.Constants.ID;
import static org.herod.order.common.Constants.NAME;

import java.util.List;

import org.herod.buyer.phone.fragments.ConfirmDialogFragment;
import org.herod.buyer.phone.fragments.ConfirmDialogFragment.OnOkButtonClickListener;
import org.herod.framework.HerodTask.AsyncTaskable;
import org.herod.framework.MapWrapper;
import org.herod.framework.RepeatedlyTask;
import org.herod.framework.lbs.LocationManager;
import org.herod.framework.lbs.SimpleLocationPlan;
import org.herod.framework.lbs.SimpleLocationPlan.OnLocationSuccessListener;
import org.herod.framework.tools.NetworkStatusTools;
import org.herod.framework.tools.NetworkStatusTools.ConnectType;
import org.herod.framework.tools.NetworkStatusTools.NetworkConnectInfo;
import org.herod.framework.utils.ToastUtils;
import org.herod.order.common.ImageLoaderAdapter;
import org.herod.order.common.RefreshButtonHelper;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.nostra13.universalimageloader.utils.ImageLoaderUtils;

public class HomeActivity extends BuyerBaseActivity implements
		AsyncTaskable<Object, List<MapWrapper<String, Object>>>,
		OnItemClickListener, OnLocationSuccessListener {

	private GridView shopTypesGridView;
	private boolean isFirst = true;
	private RefreshButtonHelper refreshButtonHelper;
	private RepeatedlyTask<Object, List<MapWrapper<String, Object>>> loadShopTypesTask;
	private ProgressDialog locationProgressDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		BuyerContext.init(this);
		ImageLoaderUtils.initImageLoader(this);
		setContentView(R.layout.activity_home);
		SimpleLocationPlan locationPlan = new SimpleLocationPlan(this);
		LocationManager.getInstance(this).executeWithPlan(locationPlan);
		shopTypesGridView = (GridView) findViewById(R.id.shopTypesGrid);
		shopTypesGridView.setOnItemClickListener(this);

		NetworkConnectInfo networkConnectInfo = NetworkStatusTools
				.getNetworkConnectInfo(this);
		if (!networkConnectInfo.available) {
			ConfirmDialogFragment.showDialog(this, "当前网络不可用，请设置网络！",
					new OnSettingNetworkOkButtonClickListener());
			return;
		}
		if (networkConnectInfo.connectType == ConnectType._2G) {
			ToastUtils.showToast("当前为2G网络", Toast.LENGTH_SHORT);
		}
		loadShopTypesTask = new RepeatedlyTask<Object, List<MapWrapper<String, Object>>>(
				this);
		refreshButtonHelper = new RefreshButtonHelper(this, loadShopTypesTask,
				R.id.refreshButton, R.id.shopTypesGrid);

		locationProgressDialog = ProgressDialog.show(this, "提示", "定位中……");
	}

	@Override
	public void onLocationSuccess(BDLocation location) {
		Log.d("LocationSuccess",
				location.getLatitude() + "," + location.getLongitude());
		if (locationProgressDialog != null
				&& locationProgressDialog.isShowing()) {
			locationProgressDialog.dismiss();
			locationProgressDialog = null;
		}
		if (isFirst) {
			loadShopTypesTask.execute(this);
			isFirst = false;
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@SuppressWarnings("unchecked")
	public void onItemClick(AdapterView<?> adapterView, View view,
			int position, long id) {
		Intent intent = new Intent(HomeActivity.this, StoresActivity.class);
		intent.putExtra(POSITION, position);
		MapWrapper<String, Object> item = (MapWrapper<String, Object>) adapterView
				.getItemAtPosition(position);
		intent.putExtra(TYPE_ID, item.getLong(ID));
		startActivity(intent);
	}

	@Override
	public List<MapWrapper<String, Object>> runOnBackground(Object... params) {
		return BuyerContext.getBuyerService().findShopTypes();
	}

	@Override
	public void onPostExecute(List<MapWrapper<String, Object>> data) {
		if (refreshButtonHelper.checkNullResult(data)) {
			return;
		}
		ListAdapter adapter = new ImageLoaderAdapter(this, data,
				R.layout.activity_home_shop_type_item, new String[] { NAME,
						IMAGE_URL }, new int[] { R.id.name, R.id.image });
		shopTypesGridView.setAdapter(adapter);
	}

	public void back(View v) {

	}

	protected boolean canBack() {
		return false;
	}

	class OnSettingNetworkOkButtonClickListener implements
			OnOkButtonClickListener {
		public void onOk() {
			startActivity(new Intent(Settings.ACTION_SETTINGS));
		}

	}

}