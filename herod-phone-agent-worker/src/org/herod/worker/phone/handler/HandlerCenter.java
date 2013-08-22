/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved.
 */
package org.herod.worker.phone.handler;

import android.os.Handler;
import android.os.Message;
import android.util.SparseArray;

/**
 * 
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 */
public class HandlerCenter {

	private Handler handler;
	private SparseArray<Runnable> callbackMap = new SparseArray<Runnable>();

	public HandlerCenter(Handler handler) {
		super();
		this.handler = handler;
	}

	public void registCallback(int what, Runnable callback) {
		callbackMap.put(what, callback);
	}

	public void unregistCallback(int what) {
		callbackMap.remove(what);
	}

	public Message obtainMessage() {
		return handler.obtainMessage();
	}

	public Message obtainMessage(int what) {
		return obtainMessage(what, 0, 0, null);
	}

	public Message obtainMessage(int what, Object obj) {
		return obtainMessage(what, 0, 0, obj);
	}

	public Message obtainMessage(int what, int arg1, int arg2) {
		return obtainMessage(what, arg1, arg2, null);
	}

	public Message obtainMessage(int what, int arg1, int arg2, Object obj) {
		Runnable callback = callbackMap.get(what);
		Message message = Message.obtain(handler, callback);
		message.what = what;
		message.arg1 = arg1;
		message.arg2 = arg2;
		message.obj = obj;
		return message;
	}
}
