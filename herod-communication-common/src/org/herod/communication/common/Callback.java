package org.herod.communication.common;

public interface Callback {
	void doCallback(ByteFrame byteFrame);

	Callback NULL_CALLBACK = new Callback() {
		@Override
		public void doCallback(ByteFrame byteFrame) {
		}
	};
}
