/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved.
 */
package org.herod.framework;

import org.herod.framework.utils.ResourcesUtils;
import org.herod.framework.utils.ToastUtils;

import android.app.Application;

/**
 * 
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 */
public class HerodApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		ResourcesUtils.setApplication(this);
		ToastUtils.setApplicationContext(this);
	}

}
