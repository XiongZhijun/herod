package org.herod.communication.common;

public interface ByteCache {

	void push(byte[] bytes);

	void push(byte[] bytes, Callback callback);

	void push(byte[] bytes, int offset, int length);

	void push(byte[] bytes, int offset, int length, Callback callback);

	byte[] pull(int begin, int end);

	int indexOf(byte[] bytes);

	int indexOf(byte b);

}
