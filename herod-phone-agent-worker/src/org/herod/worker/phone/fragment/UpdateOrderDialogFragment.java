/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.worker.phone.fragment;

import static org.herod.order.common.Constants.COMMENT;
import static org.herod.worker.phone.Constants.REASON;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.herod.framework.BundleBuilder;
import org.herod.framework.HerodTask.AsyncTaskable;
import org.herod.framework.utils.ToastUtils;
import org.herod.order.common.model.Result;
import org.herod.worker.phone.AgentWorkerTask;
import org.herod.worker.phone.R;
import org.herod.worker.phone.WorkerContext;
import org.herod.worker.phone.fragment.FormFragment.OnOkButtonClickListener;
import org.herod.worker.phone.model.OrderUpdateInfo;
import org.herod.worker.phone.view.OrderEditor;
import org.herod.worker.phone.view.OrderEditorManager;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.Toast;

/**
 * 
 * 
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 * 
 */
public class UpdateOrderDialogFragment extends FormFragment implements
		OnOkButtonClickListener, AsyncTaskable<Map<String, String>, Result> {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setOnOkButtonClickListener(this);
	}

	@Override
	protected int getLayout() {
		return R.layout.dialog_update_order;
	}

	@Override
	protected Map<Integer, String> getFormShowFromToMap() {
		return Collections.singletonMap(R.id.comment, COMMENT);
	}

	@SuppressLint("UseSparseArrays")
	@Override
	protected Map<Integer, String> getFormInputShowFromToMap() {
		Map<Integer, String> fromToMap = new HashMap<Integer, String>();
		fromToMap.put(R.id.comment, COMMENT);
		fromToMap.put(R.id.reason, REASON);
		return fromToMap;
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
				formDatas.get(COMMENT), formDatas.get(REASON));
		return WorkerContext.getWorkerService().updateOrder(updateInfo);
	}

	@Override
	public void onPostExecute(Result result) {
		String message;
		if (result != null && result.isSuccess()) {
			getTargetFragment().onActivityResult(
					OrderListFragment.REQUEST_UPDATE_ORDER,
					OrderListFragment.RESULT_SUCCESS, null);
			message = "修改订单成功！";
			dismiss();
		} else {
			message = "修改订单失败，请重试！";
		}
		ToastUtils.showToast(message, Toast.LENGTH_SHORT);
		if (result != null && result.isSuccess()) {
			OrderEditorManager.getInstance().stopEdit();
		}
	}

	public static void showDialog(Fragment targetFragment, String comment) {
		UpdateOrderDialogFragment fragment = new UpdateOrderDialogFragment();
		fragment.setTargetFragment(targetFragment,
				OrderListFragment.REQUEST_UPDATE_ORDER);
		Bundle args = BundleBuilder.create(COMMENT, comment);
		fragment.setArguments(args);
		fragment.show(targetFragment.getFragmentManager(), null);
	}

}
