package org.herod.android.communication;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

import org.herod.communication.common.ByteCache;
import org.herod.communication.common.ByteFrame;
import org.herod.communication.common.Callback;
import org.herod.communication.common.FrameEncoder;

import android.os.Handler;
import android.util.Log;

public class TcpClient implements Runnable {

	private String serverHost;
	private int serverPort;
	private SocketChannel client;
	private Selector selector;
	private boolean connectted = false;
	private Handler handler;
	private int delayMillis;
	private ByteBuffer buffer = ByteBuffer.allocate(1024);
	private ByteCache cache;
	private FrameEncoder frameEncoder;
	private Callback frameCallback;

	public TcpClient(String serverHost, int serverPort, Handler handler,
			int delayMillis, ByteCache cache, FrameEncoder frameEncoder,
			Callback frameCallback) {
		super();
		this.serverHost = serverHost;
		this.serverPort = serverPort;
		this.handler = handler;
		this.delayMillis = delayMillis;
		this.cache = cache;
		this.frameEncoder = frameEncoder;
		this.frameCallback = frameCallback;
	}

	@Override
	public void run() {
		connectToServer();
		try {
			readDatas();
		} catch (Exception e) {
			Log.w("TcpClient", "read from server failed.");
			handler.sendEmptyMessage(TcpClientService.READ_DATA_FROM_SERVER_FAILED);
		}
	}

	private void connectToServer() {
		InetSocketAddress address = new InetSocketAddress(serverHost,
				serverPort);
		try {
			client = SocketChannel.open();
			client.connect(address);
			client.configureBlocking(false);
			selector = Selector.open();
			client.register(selector, SelectionKey.OP_READ);
			connectted = true;
			handler.sendEmptyMessage(TcpClientService.CONNECT_SUCCESS);
		} catch (Exception e) {
			Log.w("TcpClient", "connect server failed.");
			handler.sendEmptyMessageDelayed(TcpClientService.CONNECT_FAILED,
					delayMillis);
		}
	}

	private void readDatas() throws IOException {
		while (connectted) {
			if (selector.select() > 0) {
				Set<?> set = selector.selectedKeys();
				Iterator<?> it = set.iterator();
				while (it.hasNext()) {
					SelectionKey skey = (SelectionKey) it.next();
					it.remove();
					if (skey.isReadable()) {
						read(skey);
					}
				}
			}
		}
	}

	private void read(SelectionKey skey) throws IOException {
		int read;
		SocketChannel sc = (SocketChannel) skey.channel();
		while ((read = sc.read(buffer)) != -1) {
			if (read == 0)
				break;
			buffer.flip();
			byte[] array = new byte[read];
			buffer.get(array);
			buffer.clear();
			cache.push(array, 0, read, frameCallback);
		}
	}

	public void stop() {
		connectted = false;
		try {
			client.close();
		} catch (IOException e) {
			Log.w("TcpClient", "disconnect failed.");
			handler.sendEmptyMessageDelayed(TcpClientService.DISCONNECT_FAILED,
					delayMillis);
		}
	}

	public boolean sendFrame(ByteFrame frame) {
		if (frame == null || client == null || !client.isConnected()) {
			return false;
		}
		try {
			frameEncoder.encode(frame);
			byte[] bytes = frame.getBytes();
			ByteBuffer bytebuf = ByteBuffer.allocate(bytes.length);
			bytebuf = ByteBuffer.wrap(bytes);
			client.write(bytebuf);
			bytebuf.flip();
			return true;
		} catch (Exception e) {
			Log.w("TcpClient", "send data to server failed.");
			return false;
		}
	}

	public boolean sendMessage(String msg) {
		try {
			ByteBuffer bytebuf = ByteBuffer.allocate(1024);
			bytebuf = ByteBuffer.wrap(msg.getBytes("UTF-8"));
			client.write(bytebuf);
			bytebuf.flip();
			return true;
		} catch (Exception e) {
			Log.w("TcpClient", "send data to server failed.");
			return false;
		}
	}
}