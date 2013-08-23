package org.herod.communication.server;

import org.apache.mina.common.AttributeKey;
import org.apache.mina.common.IoBuffer;
import org.apache.mina.common.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.herod.communication.common.ByteCache;
import org.herod.communication.common.ByteCacheVisitor;
import org.herod.communication.common.ByteFrame;
import org.herod.communication.common.ByteFrameWrapper;
import org.herod.communication.common.Callback;
import org.herod.communication.common.FrameEncoder;
import org.herod.communication.common.simple.SimpleByteCache;

public class SimpleProtocolCodecFactory implements ProtocolCodecFactory,
		ProtocolDecoder, ProtocolEncoder {
	private static final AttributeKey SESSION_CACHE_KEY = new AttributeKey(
			SimpleProtocolCodecFactory.class, "SESSION_CACHE_KEY");
	private FrameEncoder frameEncoder;
	private ByteCacheVisitor byteCacheVisitor;
	private int initialCapacity;
	private int maxCapacity;

	@Override
	public ProtocolEncoder getEncoder(IoSession session) throws Exception {
		return this;
	}

	@Override
	public ProtocolDecoder getDecoder(IoSession session) throws Exception {
		return this;
	}

	@Override
	public void encode(IoSession session, Object message,
			ProtocolEncoderOutput out) throws Exception {
		ByteFrame frame = null;
		if (message instanceof byte[]) {
			frame = new ByteFrameWrapper((byte[]) message);
		} else if (message instanceof ByteFrame) {
			frame = (ByteFrame) message;
		} else if (message instanceof IoBuffer) {
			IoBuffer buffer = (IoBuffer) message;
			byte[] data = new byte[buffer.remaining()];
			buffer.get(data);
			frame = new ByteFrameWrapper(data);
		}
		if (frame != null) {
			byte[] frameBuffer = frameEncoder.encode(frame);
			IoBuffer buffer = IoBuffer.allocate(frameBuffer.length);
			buffer.put(frameBuffer);
			buffer.flip();
			out.write(buffer);
		}
	}

	@Override
	public void decode(IoSession session, IoBuffer in,
			final ProtocolDecoderOutput out) throws Exception {
		if (!in.hasRemaining()) {
			return;
		}
		ByteCache cache = getCache(session);
		byte[] readedBytes = new byte[in.remaining()];
		in.get(readedBytes);
		cache.push(readedBytes, new Callback() {
			public void doCallback(ByteFrame frame) {
				out.write(frame);
			}
		});
	}

	protected ByteCache getCache(IoSession session) {
		ByteCache cache = (ByteCache) session.getAttribute(SESSION_CACHE_KEY);

		if (cache == null) {
			cache = new SimpleByteCache(initialCapacity, maxCapacity,
					byteCacheVisitor);
			session.setAttribute(SESSION_CACHE_KEY, cache);
		}
		return cache;
	}

	@Override
	public void finishDecode(IoSession session, ProtocolDecoderOutput out)
			throws Exception {
	}

	@Override
	public void dispose(IoSession session) throws Exception {
		session.removeAttribute(SESSION_CACHE_KEY);
	}

	public FrameEncoder getFrameEncoder() {
		return frameEncoder;
	}

	public void setFrameEncoder(FrameEncoder frameEncoder) {
		this.frameEncoder = frameEncoder;
	}

	public ByteCacheVisitor getByteCacheVisitor() {
		return byteCacheVisitor;
	}

	public void setByteCacheVisitor(ByteCacheVisitor byteCacheVisitor) {
		this.byteCacheVisitor = byteCacheVisitor;
	}

	public int getInitialCapacity() {
		return initialCapacity;
	}

	public void setInitialCapacity(int initialCapacity) {
		this.initialCapacity = initialCapacity;
	}

	public int getMaxCapacity() {
		return maxCapacity;
	}

	public void setMaxCapacity(int maxCapacity) {
		this.maxCapacity = maxCapacity;
	}

}
