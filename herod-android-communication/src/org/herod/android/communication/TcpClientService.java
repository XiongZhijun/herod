/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved.
 */
package org.herod.android.communication;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.herod.communication.common.ByteCache;
import org.herod.communication.common.ByteFrame;
import org.herod.communication.common.FrameEncoder;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.IBinder;
import android.os.Message;

/**
 * 
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 */
public abstract class TcpClientService extends Service implements Callback,
		org.herod.communication.common.Callback {

	public static final int CONNECT_FAILED = 1;
	public static final int READ_DATA_FROM_SERVER_FAILED = 2;
	public static final int DISCONNECT_FAILED = 3;
	public static final int CONNECT_SUCCESS = 4;
	public static final int RECEIVE_MESSAGE = 5;

	private final IBinder mBinder = new LocalBinder();
	private Handler handler;
	private TcpClient client;
	private ExecutorService executor = Executors.newSingleThreadExecutor();

	public void onCreate() {
		super.onCreate();
		handler = new Handler(this);
		start();
	}

	public void onDestroy() {
		super.onDestroy();
		stop();
	}

	protected void restart() {
		stop();
		start();
	}

	protected void start() {
		client = new TcpClient(getServerHost(), getServerPort(), handler,
				getDelayMillis(), getByteCache(), getFrameEncoder(), this);
		executor.execute(client);
	}

	protected abstract ByteCache getByteCache();

	protected abstract FrameEncoder getFrameEncoder();

	@Override
	public final void doCallback(ByteFrame byteFrame) {
		onReceiveMessage(byteFrame);
	}

	protected abstract void onReceiveMessage(ByteFrame byteFrame);

	protected void stop() {
		if (client != null) {
			client.stop();
			client = null;
		}
	}

	public IBinder onBind(Intent intent) {
		return mBinder;
	}

	@Override
	public boolean handleMessage(Message msg) {
		switch (msg.what) {
		case CONNECT_FAILED:
			onConnectFailed();
			return true;
		case READ_DATA_FROM_SERVER_FAILED:
			onReadDataFailed();
			return true;
		case CONNECT_SUCCESS:
			onConnectSuccess();
			return true;
		default:
			return false;
		}
	}

	protected void onConnectSuccess() {
	}

	protected void onConnectFailed() {
		restart();
	}

	protected void onReadDataFailed() {
		restart();
	}

	public boolean sendMessage(String msg) {
		if (client != null) {
			return client.sendMessage(msg);
		}
		return false;
	}

	public class LocalBinder extends Binder {
		TcpClientService getService() {
			return TcpClientService.this;
		}
	}

	private int getServerPort() {
		return Integer.parseInt(getString(R.string.TcpServerPort));
	}

	private String getServerHost() {
		return getString(R.string.TcpServerHost);
	}

	private int getDelayMillis() {
		return Integer.parseInt(getString(R.string.TcpServerReconnectDelay));
	}

}
