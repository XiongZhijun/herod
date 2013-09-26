/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved.
 */
package org.herod.worker.phone.service;

import org.herod.android.communication.TcpClientService.LocalBinder;
import org.herod.worker.phone.event.EventClientService;

import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.IBinder;

/**
 * 
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 */
public class RegistServiceConnection implements ServiceConnection {
	private Context context;

	public RegistServiceConnection(Context context) {
		super();
		this.context = context;
	}

	public void onServiceDisconnected(ComponentName name) {
	}

	public void onServiceConnected(ComponentName name, IBinder service) {
		EventClientService eventService = (EventClientService) ((LocalBinder) service)
				.getService();
		eventService.registToServer();
		context.unbindService(this);
	}
}
