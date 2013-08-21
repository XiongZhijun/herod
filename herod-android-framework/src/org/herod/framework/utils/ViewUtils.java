/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved.
 */
package org.herod.framework.utils;

import org.herod.framework.ViewFindable;

import android.view.View;
import android.view.View.OnClickListener;

/**
 * 
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 */
public class ViewUtils {

	public static <T extends ViewFindable & OnClickListener> void setOnClickListener(
			T register, int... viewIds) {
		setOnClickListener(register, register, viewIds);
	}

	public static void setOnClickListener(ViewFindable viewFindable,
			OnClickListener listener, int... viewIds) {
		for (int viewId : viewIds) {
			viewFindable.findViewById(viewId).setOnClickListener(listener);
		}
	}
	
	public static void setVisibility(ViewFindable viewFindable,int visibility, int... ids) {
		for (int id : ids) {
			View view = viewFindable.findViewById(id);
			setVisibility(visibility, view);
		}
	}

	public static void setVisibility(int visibility, View... views) {
		for (View view : views) {
			if (view != null) {
				view.setVisibility(visibility);
			}
		}
	}
}
