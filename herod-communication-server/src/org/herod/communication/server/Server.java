package org.herod.communication.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.mina.common.IoAcceptor;
import org.apache.mina.common.IoFilter;
import org.apache.mina.common.IoHandler;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

/**
 * 事件的服务对象，通过该对象来启动事件服务。
 * 
 * @author Xiong Zhijun
 * 
 */
public class Server {

	private static Log _log = LogFactory.getLog(Server.class);

	private int port = 29999;
	private int readerIdleTime = 0;
	private int writerIdleTime = 0;
	private int receiveBufferSize = 1024;
	private int sendBufferSize = 1024;
	private Executor executor;
	private IoHandler ioHandler;
	private IoFilter frameFilter;

	public void start() {
		IoAcceptor acceptor = createIoAcceptor();
		acceptor.setDefaultLocalAddress(new InetSocketAddress(port));
		acceptor.getSessionConfig().setReaderIdleTime(readerIdleTime);
		acceptor.getSessionConfig().setWriterIdleTime(writerIdleTime);
		acceptor.setHandler(ioHandler);
		acceptor.getFilterChain().addLast("frame", frameFilter);
		acceptor.getFilterChain().addLast("executor",
				new ExecutorFilter(getExecutor()));
		try {
			acceptor.bind();
		} catch (IOException e) {
			_log.error("bind event service IoAcceptor failed", e);
			System.exit(0);
		}
	}

	protected IoAcceptor createIoAcceptor() {
		NioSocketAcceptor acceptor = new NioSocketAcceptor(Runtime.getRuntime()
				.availableProcessors() + 1);
		acceptor.setBacklog(200);
		acceptor.getSessionConfig().setReuseAddress(true);
		acceptor.getSessionConfig().setTcpNoDelay(true);
		acceptor.getSessionConfig().setReceiveBufferSize(receiveBufferSize);
		acceptor.getSessionConfig().setSendBufferSize(sendBufferSize);
		return acceptor;

	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getPort() {
		return port;
	}

	public void setReaderIdleTime(int readerIdleTime) {
		this.readerIdleTime = readerIdleTime;
	}

	public void setWriterIdleTime(int writerIdleTime) {
		this.writerIdleTime = writerIdleTime;
	}

	public Executor getExecutor() {
		if (executor == null) {
			executor = Executors.newSingleThreadExecutor();
		}
		return executor;
	}

	public void setExecutor(Executor executor) {
		this.executor = executor;
	}

	public void setReceiveBufferSize(int receiveBufferSize) {
		this.receiveBufferSize = receiveBufferSize;
	}

	public void setSendBufferSize(int sendBufferSize) {
		this.sendBufferSize = sendBufferSize;
	}

	public void setIoHandler(IoHandler ioHandler) {
		this.ioHandler = ioHandler;
	}

	public void setFrameFilter(IoFilter frameFilter) {
		this.frameFilter = frameFilter;
	}

}
