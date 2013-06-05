/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.buyer.phone.view;

import org.herod.buyer.phone.R;
import org.herod.buyer.phone.ShoppingCartActivity;
import org.herod.buyer.phone.ShoppingCartCache;

import android.content.Context;
import android.content.Intent;
import android.view.ActionProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

/**
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 * 
 */
public class ShoppingCartActionBarActionProvider extends ActionProvider
		implements OnClickListener {

	private Context context;

	public ShoppingCartActionBarActionProvider(Context context) {
		super(context);
		this.context = context;
	}

	@Override
	public View onCreateActionView() {
		View view = LayoutInflater.from(context).inflate(
				R.layout.view_shopping_cart_action_bar, null);
		int totalQuantity = ShoppingCartCache.getInstance().getTotalQuantity();
		((TextView) view.findViewById(R.id.totalQuantity))
				.setText(totalQuantity + "");
		view.setOnClickListener(this);
		return view;
	}

	public boolean onPerformDefaultAction() {
		return true;
	}

	@Override
	public void onClick(View arg0) {
		context.startActivity(new Intent(context, ShoppingCartActivity.class));
	}
}
