/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.worker.phone.fragment;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.herod.framework.HerodTask.AsyncTaskable;
import org.herod.framework.utils.ToastUtils;
import org.herod.order.common.model.Result;
import org.herod.worker.phone.AgentWorkerTask;
import org.herod.worker.phone.MainActivity;
import org.herod.worker.phone.R;
import org.herod.worker.phone.WorkerContext;
import org.herod.worker.phone.fragment.FormFragment.OnCancelButtonClickListener;
import org.herod.worker.phone.fragment.FormFragment.OnOkButtonClickListener;
import org.herod.worker.phone.model.OrderUpdateInfo;
import org.herod.worker.phone.view.OrderEditor;
import org.herod.worker.phone.view.OrderEditorManager;

import android.annotation.SuppressLint;
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
public class UpdateOrderDialogFragment extends FormFragment implements
		OnOkButtonClickListener, OnCancelButtonClickListener,
		AsyncTaskable<Map<String, String>, Result> {

	private Handler handler;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setOnOkButtonClickListener(this);
		setOnCancelButtonClickListener(this);
	}

	@Override
	protected int getLayout() {
		return R.layout.dialog_update_order;
	}

	@Override
	protected Map<Integer, String> getFormShowFromToMap() {
		return Collections.singletonMap(R.id.comment, "comment");
	}

	@SuppressLint("UseSparseArrays")
	@Override
	protected Map<Integer, String> getFormInputShowFromToMap() {
		Map<Integer, String> fromToMap = new HashMap<Integer, String>();
		fromToMap.put(R.id.comment, "comment");
		fromToMap.put(R.id.reason, "reason");
		return fromToMap;
	}

	@Override
	public void onCancel() {
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onOk(Map<String, String> formDatas) {
		new AgentWorkerTask<Map<String, String>, Result>(getActivity(), this)
				.execute(formDatas);
	}

	@Override
	public Result runOnBackground(Map<String, String>... params) {
		Map<String, String> formDatas = params[0];
		OrderEditor orderEditor = OrderEditorManager.getInstance()
				.getOrderEditor();
		if (orderEditor == null) {
			return null;
		}
		OrderUpdateInfo updateInfo = orderEditor.toUpdateInfo(
				formDatas.get("comment"), formDatas.get("reason"));
		return WorkerContext.getWorkerService().updateOrder(updateInfo);
	}

	@Override
	public void onPostExecute(Result result) {
		String message;
		if (result != null && result.isSuccess()) {
			handler.sendMessage(handler
					.obtainMessage(MainActivity.MESSAGE_KEY_REFRESH_ORDER_LIST));
			message = "修改订单成功！";
		} else {
			message = "修改订单失败，请重试！";
		}
		ToastUtils.showToast(message, Toast.LENGTH_SHORT);
		if (result != null && result.isSuccess()) {
			OrderEditorManager.getInstance().stopEdit();
		}
	}

	public static void showDialog(FragmentActivity activity, Handler handler,
			String comment) {
		UpdateOrderDialogFragment fragment = new UpdateOrderDialogFragment();
		Bundle args = new Bundle();
		args.putString("comment", comment);
		fragment.setArguments(args);
		fragment.handler = handler;
		fragment.show(activity.getSupportFragmentManager(), null);
	}

}
