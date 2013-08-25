/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved.
 */
package org.herod.android.communication;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.herod.communication.common.ByteCacheVisitor;
import org.herod.communication.common.ByteFrame;
import org.herod.communication.common.FrameEncoder;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.IBinder;
import android.os.Looper;
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
	private ExecutorService messageSenderExecutor = Executors
			.newSingleThreadExecutor();
	private Set<ByteFrame> waitSendFrames = new HashSet<ByteFrame>();

	public void onCreate() {
		super.onCreate();
		handler = new Handler(Looper.getMainLooper(), this);
		connect();
	}

	public void onDestroy() {
		super.onDestroy();
		waitSendFrames.clear();
		disconnect();
	}

	public void reconnect() {
		disconnect();
		connect();
	}

	public void connect() {
		client = new TcpClient(getServerHost(), getServerPort(), handler,
				getDelayMillis(), getByteCacheVisitor(), getFrameEncoder(),
				this);
		new Thread(client).start();
	}

	protected abstract ByteCacheVisitor getByteCacheVisitor();

	protected abstract FrameEncoder getFrameEncoder();

	@Override
	public final void doCallback(ByteFrame byteFrame) {
		onReceiveMessage(byteFrame);
	}

	protected abstract void onReceiveMessage(ByteFrame byteFrame);

	public void disconnect() {
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
		for (ByteFrame frame : waitSendFrames) {
			sendMessageCertainly(frame);
		}
	}

	protected void onConnectFailed() {
		reconnect();
	}

	protected void onReadDataFailed() {
		reconnect();
	}

	public boolean sendMessage(final ByteFrame byteFrame) {
		if (client != null) {
			messageSenderExecutor.execute(new Runnable() {
				public void run() {
					client.sendFrame(byteFrame);
				}
			});
			return true;
		}
		return false;
	}

	public void sendMessageCertainly(final ByteFrame byteFrame) {
		if (client != null) {
			messageSenderExecutor.execute(new Runnable() {
				public void run() {
					boolean success = client.sendFrame(byteFrame);
					if (!success) {
						waitSendFrames.add(byteFrame);
					}
				}
			});
		}
	}

	public class LocalBinder extends Binder {
		public TcpClientService getService() {
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
