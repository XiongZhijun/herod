package org.herod.communication.common;

public interface FrameEncoder {

	byte[] encode(ByteFrame command);
}
