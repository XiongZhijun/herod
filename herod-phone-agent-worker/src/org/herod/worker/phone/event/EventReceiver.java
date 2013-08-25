/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.worker.phone.event;

import org.herod.event.Event;
import org.herod.framework.utils.ToastUtils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * 
 * 
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 * 
 */
public class EventReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Event event = (Event) intent
				.getSerializableExtra(EventClientService.EVENT);
		ToastUtils.showToast(event.toString(), Toast.LENGTH_SHORT);
	}

}
