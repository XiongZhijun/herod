/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved.
 */
package org.herod.framework.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * 
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 */
public class ToastUtils {

	private static Context applicationContext;

	public static void showToast(CharSequence text, int duration) {
		Toast.makeText(applicationContext, text, duration).show();
	}

	public static void showToast(int text, int duration) {
		Toast.makeText(applicationContext, text, duration).show();
	}

	public static void setApplicationContext(Context applicationContext) {
		ToastUtils.applicationContext = applicationContext;
	}
}
