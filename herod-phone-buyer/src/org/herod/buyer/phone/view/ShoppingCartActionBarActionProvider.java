/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.buyer.phone.view;

import org.herod.buyer.phone.R;

import android.content.Context;
import android.view.ActionProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

/**
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 * 
 */
public class ShoppingCartActionBarActionProvider extends ActionProvider {

	private Context context;

	public ShoppingCartActionBarActionProvider(Context context) {
		super(context);
		this.context = context;
	}

	@Override
	public View onCreateActionView() {
		View view = LayoutInflater.from(context).inflate(
				R.layout.view_shopping_cart_action_bar, null);
		((TextView) view.findViewById(R.id.totalQuantity)).setText("0");
		return view;
	}

	public boolean onPerformDefaultAction() {
		return true;
	}
}
