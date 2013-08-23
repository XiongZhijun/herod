package org.herod.communication.common.simple;

import org.herod.communication.common.ByteArray;
import org.herod.communication.common.ByteCache;
import org.herod.communication.common.ByteCacheVisitor;
import org.herod.communication.common.ByteFrame;
import org.herod.communication.common.Callback;

public class SimpleByteCache implements ByteCache {

	private static final byte[] EMPTY_BYTES = new byte[0];
	private static final int DEFAULT_INIT_CACHE_SIZE = 512;
	private static final int DEFAULT_MAX_CACHE_SIZE = DEFAULT_INIT_CACHE_SIZE * 3;
	private ByteArray byteArray;
	private int maxCapacity;
	private ByteCacheVisitor visitor = ByteCacheVisitor.NULL_VISITOR;

	public SimpleByteCache() {
		this(DEFAULT_INIT_CACHE_SIZE, DEFAULT_MAX_CACHE_SIZE,
				ByteCacheVisitor.NULL_VISITOR);
	}

	public SimpleByteCache(int initialCapacity, int maxCapacity,
			ByteCacheVisitor visitor) {
		super();
		this.byteArray = new ByteArray(initialCapacity);
		this.maxCapacity = maxCapacity;
		if (visitor != null)
			this.visitor = visitor;
	}

	@Override
	public void push(byte[] bytes) {
		push(bytes, Callback.NULL_CALLBACK);
	}

	@Override
	public void push(byte[] bytes, Callback callback) {
		push(bytes, 0, bytes.length, callback);
	}

	@Override
	public void push(byte[] bytes, int offset, int length) {
		push(bytes, offset, length, Callback.NULL_CALLBACK);
	}

	@Override
	public void push(byte[] bytes, int offset, int length, Callback callback) {
		if (bytes == null || bytes.length <= 0 || offset < 0 || length <= 0
				|| bytes.length < offset + length) {
			return;
		}
		synchronized (byteArray) {
			if (length + byteArray.size() > maxCapacity) {
				byteArray.clear();
			}
			byteArray.addAll(bytes, offset, length);
			ByteFrame byteFrame = null;
			do {
				byteFrame = visitor.visit(this);
				if (byteFrame != null) {
					callback.doCallback(byteFrame);
				}
			} while (byteFrame != null);
		}

	}

	@Override
	public byte[] pull(int begin, int end) {
		synchronized (byteArray) {
			if (byteArray.size() >= end) {
				return byteArray.pullAndRemove(begin, end);
			}
		}
		return EMPTY_BYTES;
	}

	@Override
	public int indexOf(byte[] bytes) {
		synchronized (byteArray) {
			return byteArray.indexOf(bytes);
		}
	}

	@Override
	public int indexOf(byte b) {
		synchronized (byteArray) {
			return byteArray.indexOf(b);
		}
	}

	public void setVisitor(ByteCacheVisitor visitor) {
		this.visitor = visitor;
	}

}
