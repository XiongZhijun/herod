/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.worker.phone.fragment;

import static org.herod.order.common.Constants.SERIAL_NUMBER;
import static org.herod.worker.phone.Constants.REASON;
import static org.herod.worker.phone.RequestCodes.REQUEST_CANCEL_ORDER;
import static org.herod.worker.phone.RequestCodes.RESULT_SUCCESS;

import java.util.Collections;
import java.util.Map;

import org.herod.framework.BundleBuilder;
import org.herod.framework.HerodTask.AsyncTaskable;
import org.herod.framework.utils.ToastUtils;
import org.herod.order.common.model.Result;
import org.herod.worker.phone.AgentWorkerTask;
import org.herod.worker.phone.R;
import org.herod.worker.phone.WorkerContext;
import org.herod.worker.phone.fragment.FormFragment.OnOkButtonClickListener;

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
public class CancelOrderDialogFragment extends FormFragment implements
		OnOkButtonClickListener, AsyncTaskable<String, Result> {

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
		String serialNumber = getArguments().getString(SERIAL_NUMBER);
		return WorkerContext.getWorkerService().cancelOrder(serialNumber,
				reasons[0]);
	}

	@Override
	public void onPostExecute(Result result) {
		String message;
		if (result != null && result.isSuccess()) {
			message = "取消订单成功！";
			getTargetFragment().onActivityResult(REQUEST_CANCEL_ORDER,
					RESULT_SUCCESS, null);
			dismiss();
		} else {
			message = "取消订单失败，请重试！";
		}
		ToastUtils.showToast(message, Toast.LENGTH_SHORT);
	}

	public static void showDialog(Fragment targetFragment, String serialNumber) {
		CancelOrderDialogFragment fragment = new CancelOrderDialogFragment();
		fragment.setTargetFragment(targetFragment, REQUEST_CANCEL_ORDER);
		Bundle args = BundleBuilder.create(SERIAL_NUMBER, serialNumber);
		fragment.setArguments(args);
		fragment.show(targetFragment.getFragmentManager(), null);
	}

}
