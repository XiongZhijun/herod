/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved.
 */
package org.herod.worker.phone.handler;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

/**
 * 
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 */
public class HerodHandler extends Handler {

	private Handler parent;

	public HerodHandler() {
		this(null, Looper.getMainLooper(), null);
	}

	public HerodHandler(Handler parent) {
		this(parent, Looper.getMainLooper(), null);
	}

	public HerodHandler(Handler parent, Looper looper) {
		this(parent, looper, null);
	}

	public HerodHandler(Callback callback) {
		this(null, Looper.getMainLooper(), callback);
	}

	public HerodHandler(Handler parent, Callback callback) {
		this(parent, Looper.getMainLooper(), callback);
	}

	public HerodHandler(Handler parent, Looper looper, Callback callback) {
		super(looper, callback);
		this.parent = parent;
	}

	public final void handleMessage(Message msg) {
		if (handleMessage0(msg)) {
			return;
		} else if (parent != null) {
			Message message = Message.obtain(msg);
			message.setTarget(parent);
			parent.sendMessage(message);
		}
	}

	public boolean handleMessage0(Message msg) {
		return false;
	}

}
