package org.herod.communication.common.simple;

import org.herod.communication.common.ByteArray;
import org.herod.communication.common.ByteCache;
import org.herod.communication.common.ByteCacheVisitor;
import org.herod.communication.common.ByteFrame;
import org.herod.communication.common.ByteFrameWrapper;
import org.herod.communication.common.ByteStack;
import org.herod.communication.common.FrameEncoder;

public class HeadTailHandler implements ByteCacheVisitor, FrameEncoder {

	private byte[] head;
	private byte[] tail;

	public HeadTailHandler(byte[] head, byte[] tail) {
		super();
		this.head = head;
		this.tail = tail;
	}

	@Override
	public ByteFrame visit(ByteCache cache) {
		int headIndex = cache.indexOf(head);
		if (headIndex < 0) {
			return null;
		}
		int tailIndex = cache.indexOf(tail);
		if (tailIndex <= headIndex) {
			return null;
		}
		byte[] containHeadTailFrame = cache.pull(headIndex, tailIndex
				+ tail.length);
		return decodeContainHeadTailFrame(containHeadTailFrame);
	}

	protected ByteFrame decodeContainHeadTailFrame(byte[] containHeadTailFrame) {
		ByteStack stack = new ByteStack(containHeadTailFrame);
		stack.pop(head.length);
		byte[] frameBytes = stack.pop(stack.size() - tail.length);
		return decodeFrame(frameBytes);
	}

	protected ByteFrame decodeFrame(byte[] frameBytes) {
		return new ByteFrameWrapper(frameBytes);
	}

	@Override
	public byte[] encode(ByteFrame command) {
		byte[] data = command.getBytes();
		ByteArray byteArray = new ByteArray(data.length + head.length
				+ tail.length);
		byteArray.addAll(head);
		byteArray.addAll(data);
		byteArray.addAll(tail);
		return byteArray.toArray();
	}

}
