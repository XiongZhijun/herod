/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.worker.phone.fragment;

import static org.herod.worker.phone.Constants.REASON;

import java.util.Collections;
import java.util.Map;

import org.herod.framework.HerodTask.AsyncTaskable;
import org.herod.framework.utils.ToastUtils;
import org.herod.order.common.model.Result;
import org.herod.worker.phone.AgentWorkerTask;
import org.herod.worker.phone.MainActivity;
import org.herod.worker.phone.R;
import org.herod.worker.phone.WorkerContext;
import org.herod.worker.phone.fragment.FormFragment.OnOkButtonClickListener;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

/**
 * 
 * 
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 * 
 */
public class CancelOrderDialogFragment extends FormFragment implements
		OnOkButtonClickListener, AsyncTaskable<String, Result> {

	private String serialNumber;
	private Handler handler;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setOnOkButtonClickListener(this);
	}

	@Override
	protected int getLayout() {
		return R.layout.dialog_cancel_order;
	}

	@Override
	protected Map<Integer, String> getFormShowFromToMap() {
		return Collections.emptyMap();
	}

	@Override
	protected Map<Integer, String> getFormInputShowFromToMap() {
		return Collections.singletonMap(R.id.reason, REASON);
	}

	@Override
	public void onOk(Map<String, String> formDatas) {
		new AgentWorkerTask<String, Result>(getActivity(), this)
				.execute(formDatas.get(REASON));

	}

	@Override
	public Result runOnBackground(String... reasons) {
		return WorkerContext.getWorkerService().cancelOrder(serialNumber,
				reasons[0]);
	}

	@Override
	public void onPostExecute(Result result) {
		String message;
		if (result != null && result.isSuccess()) {
			handler.sendMessage(handler
					.obtainMessage(MainActivity.MESSAGE_KEY_REFRESH_ORDER_LIST));
			message = "取消订单成功！";
			dismiss();
		} else {
			message = "取消订单失败，请重试！";
		}
		ToastUtils.showToast(message, Toast.LENGTH_SHORT);
	}

	public static void showDialog(FragmentActivity activity, Handler handler,
			String serialNumber) {
		CancelOrderDialogFragment fragment = new CancelOrderDialogFragment();
		// TODO 存在这样类似handler的参数传递问题。
		fragment.handler = handler;
		fragment.serialNumber = serialNumber;
		fragment.show(activity.getSupportFragmentManager(), null);
	}

}
