/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.buyer.phone.fragments;

import java.util.Date;
import java.util.List;

import org.herod.buyer.phone.AbstractOrdersActivity;
import org.herod.buyer.phone.BuyerContext;
import org.herod.buyer.phone.R;
import org.herod.buyer.phone.ShoppingCartCache;
import org.herod.buyer.phone.db.LocalAddress;
import org.herod.buyer.phone.db.OrderDao;
import org.herod.buyer.phone.service.SimpleAddressSelectService;
import org.herod.framework.db.DatabaseOpenHelper;
import org.herod.framework.utils.StringUtils;
import org.herod.framework.utils.ToastUtils;
import org.herod.order.common.model.Address;
import org.herod.order.common.model.Order;
import org.herod.order.common.model.OrderStatus;
import org.herod.order.common.model.Result;
import org.herod.order.common.model.ResultCode;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 * 
 */
public class SubmitOrderInfoFragment extends DialogFragment implements
		OnClickListener {
	private static final int[] EDIT_TEXT_IDS = new int[] { R.id.buyerAddress,
			R.id.buyerPhone, R.id.buyerName };
	private static final String[] EDIT_TEXT_ERROR_INFOS = new String[] {
			"请输入送货地址！", "请输入正确的手机号！", "请输入联系人姓名！" };
	private TextView commentView;
	private AddressSelectService addressSelectService = new SimpleAddressSelectService();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setStyle(DialogFragment.STYLE_NO_TITLE,
				android.R.style.Theme_Holo_Light_Dialog);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_submit_order_info, container,
				false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		int totalQuantity = ShoppingCartCache.getInstance().getTotalQuantity();
		double totalAmount = ShoppingCartCache.getInstance().getTotalAmount();
		setText(R.id.totalQuantity, totalQuantity);
		setText(R.id.totalAmount, totalAmount);
		commentView = (TextView) view.findViewById(R.id.comment);
		view.findViewById(R.id.submitOrdersButton).setOnClickListener(this);
		view.findViewById(R.id.cancelButton).setOnClickListener(this);

		LocalAddress selectedAddress = addressSelectService
				.selectAddress(getActivity());
		if (selectedAddress != null) {
			setText(R.id.buyerName, selectedAddress.getName());
			setText(R.id.buyerPhone, selectedAddress.getPhone());
			setText(R.id.buyerAddress, selectedAddress.getAddress());
		}
	}

	protected void setText(int viewId, Object text) {
		((TextView) getView().findViewById(viewId)).setText(text.toString());
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.cancelButton) {
			dismiss();
			return;
		} else if (v.getId() == R.id.submitOrdersButton) {
			submitOrders();
		}
	}

	protected void submitOrders() {
		boolean success = true;
		String[] inputs = new String[EDIT_TEXT_IDS.length];

		for (int i = 0; i < EDIT_TEXT_IDS.length; i++) {
			TextView view = (TextView) getView().findViewById(EDIT_TEXT_IDS[i]);
			String text = view.getText().toString();
			if (StringUtils.isBlank(text)) {
				view.setError(EDIT_TEXT_ERROR_INFOS[i]);
				success = false;
			}
			inputs[i] = text;
		}

		if (!success) {
			return;
		}

		String comment = commentView.getText().toString();
		new SubmitOrdersTask(inputs[0], inputs[1], inputs[2], comment)
				.execute();
	}

	class SubmitOrdersTask extends AsyncTask<Object, Object, Result> {

		private List<Order> orders;
		private String buyerAddress;
		private String buyerPhone;
		private String buyerName;
		private String comment;

		public SubmitOrdersTask(String buyerAddress, String buyerPhone,
				String buyerName, String comment) {
			super();
			this.buyerAddress = buyerAddress;
			this.buyerPhone = buyerPhone;
			this.buyerName = buyerName;
			this.comment = comment;
		}

		protected Result doInBackground(Object... params) {
			orders = ShoppingCartCache.getInstance().getAllOrders();
			String transactionSN = getTransactionSerialNumber();
			for (int i = 0; i < orders.size(); i++) {
				Order order = orders.get(i);
				Address deliveryAddress = new Address();
				deliveryAddress.setAddress(buyerAddress);
				order.setDeliveryAddress(deliveryAddress);
				order.setBuyerPhone(buyerPhone);
				order.setBuyerName(buyerName);
				order.setComment(comment);
				order.setSubmitTime(new Date());
				order.setSerialNumber(transactionSN + "-" + (i + 1));
			}
			return BuyerContext.getBuyerService().submitOrders(orders);
		}

		protected String getTransactionSerialNumber() {
			return BuyerContext.getBuyerService().getTransactionSerialNumber();
		}

		@Override
		protected void onPostExecute(Result result) {
			FragmentActivity activity = getActivity();
			if (result != null
					&& (result.isSuccess() || result.getCode() == ResultCode.SomeOrderIsExists)) {
				onSubmitSuccess(activity);
				return;
			}
			ToastUtils.showToast("下单失败，请重试！", Toast.LENGTH_SHORT);
		}

		private void onSubmitSuccess(FragmentActivity activity) {
			for (Order order : orders) {
				order.setStatus(OrderStatus.Submitted);
			}
			addOrdersToDatabase(activity, orders);
			ToastUtils.showToast("下单成功", Toast.LENGTH_SHORT);
			ShoppingCartCache.getInstance().clearOrders();
			dismiss();
			if (activity instanceof AbstractOrdersActivity) {
				((AbstractOrdersActivity) activity).refreshOrders();
			}
			addressSelectService.addCurrentAddress(activity, buyerName,
					buyerPhone, buyerAddress);
		}
	}

	private void addOrdersToDatabase(Context context, List<Order> orders) {
		SQLiteOpenHelper openHelper = new DatabaseOpenHelper(context);
		try {
			OrderDao orderDao = new OrderDao(openHelper);
			orderDao.addOrders(orders);
		} finally {
			if (openHelper != null)
				openHelper.close();
		}
	}

	public static interface AddressSelectService {
		LocalAddress selectAddress(Context context);

		void addCurrentAddress(Context context, String buyerName,
				String buyerPhone, String buyerAddress);
	}
}
