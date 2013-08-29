/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved.
 */
package org.herod.order.common;

import java.util.ArrayList;
import java.util.List;

import org.herod.framework.RepeatedlyTask;
import org.herod.framework.ViewFindable;

import android.view.View;
import android.view.View.OnClickListener;

/**
 * 
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 */
public class RefreshButtonHelper {
	private View refreshButton;
	private List<View> otherViews = new ArrayList<View>();

	public RefreshButtonHelper(ViewFindable viewFindable,
			final RepeatedlyTask<?, ?> task, int refreshButton,
			int... otherViews) {
		this(viewFindable, refreshButton, new OnClickListener() {
			public void onClick(View v) {
				task.execute();
			}
		}, otherViews);
	}

	private RefreshButtonHelper(ViewFindable viewFindable, int refreshButton,
			OnClickListener refreshListener, int... otherViews) {
		this.refreshButton = viewFindable.findViewById(refreshButton);
		this.refreshButton.setOnClickListener(refreshListener);
		for (int otherView : otherViews) {
			this.otherViews.add(viewFindable.findViewById(otherView));
		}
	}

	public void enableRefresh() {
		refreshButton.setVisibility(View.VISIBLE);
		for (View view : otherViews) {
			view.setVisibility(View.GONE);
		}
	}

	public void disableRefresh() {
		refreshButton.setVisibility(View.GONE);
		for (View view : otherViews) {
			view.setVisibility(View.VISIBLE);
		}
	}

	public boolean checkNullResult(Object result) {
		if (result == null) {
			enableRefresh();
			return true;
		} else {
			disableRefresh();
			return false;
		}

	}

}
