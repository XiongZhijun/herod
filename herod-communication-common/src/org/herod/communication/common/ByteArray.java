/*
 *  Copyright@2011 FPI,Inc. All rights reserved.
 */
package org.herod.communication.common;

import java.util.Arrays;

/**
 * @author Xiong Zhijun
 * @since sword-protocol-tools
 * @create 2011-4-20
 */
public class ByteArray {

	private byte[] elementData;

	private int size;

	private int initialCapacity;

	public ByteArray() {
		this(10);
	}

	public ByteArray(int initialCapacity) {
		elementData = new byte[initialCapacity];
		this.initialCapacity = initialCapacity;
	}

	public void add(byte b) {
		ensureCapacity(size + 1);
		elementData[size++] = b;
	}

	public void addAll(byte[] bytes) {
		if (bytes == null || bytes.length == 0) {
			return;
		}
		addAll(bytes, 0, bytes.length);
	}

	public void addAll(byte[] bytes, int offset, int length) {
		if (bytes == null || bytes.length == 0 || offset < 0 || length <= 0) {
			return;
		}
		ensureCapacity(size + length);
		System.arraycopy(bytes, offset, elementData, size, length);
		size += length;
	}

	private void ensureCapacity(int minCapacity) {
		int oldCapacity = elementData.length;
		if (minCapacity > oldCapacity) {
			int newCapacity = (oldCapacity * 3) / 2 + 1;
			if (newCapacity < minCapacity)
				newCapacity = minCapacity;
			elementData = Arrays.copyOf(elementData, newCapacity);
		}
	}

	public int indexOf(byte b) {
		for (int i = 0; i < size; i++) {
			if (elementData[i] == b) {
				return i;
			}
		}
		return -1;
	}

	public int indexOf(int fromIndex, byte b) {
		rangeCheck(fromIndex);
		for (int i = fromIndex; i < size; i++) {
			if (elementData[i] == b) {
				return i;
			}
		}
		return -1;
	}

	public int indexOf(byte[] bytes) {
		for (int i = 0; i < size; i++) {
			boolean equals = true;
			for (int j = 0; j < bytes.length && (i + j) < size; j++) {
				if (bytes[j] != elementData[i + j]) {
					equals = false;
					break;
				}
			}
			if (equals) {
				return i;
			}
		}
		return -1;
	}

	public int indexOf(int fromIndex, byte[] bytes) {
		rangeCheck(fromIndex);
		for (int i = fromIndex; i < size; i++) {
			boolean equals = true;
			for (int j = 0; j < bytes.length && (i + j) < size; j++) {
				if (bytes[j] != elementData[i + j]) {
					equals = false;
					break;
				}
			}
			if (equals) {
				return i;
			}
		}
		return -1;
	}

	public byte remove(int index) {
		rangeCheck(index);

		byte oldValue = elementData[index];

		int numMoved = size - index - 1;
		if (numMoved > 0)
			System.arraycopy(elementData, index + 1, elementData, index,
					numMoved);
		elementData[--size] = 0x00;

		return oldValue;
	}

	public byte[] removeRange(int fromIndex, int toIndex) {
		int numMoved = size - toIndex;
		byte[] oldBytes = new byte[toIndex - fromIndex];
		System.arraycopy(elementData, fromIndex, oldBytes, 0, oldBytes.length);
		System.arraycopy(elementData, toIndex, elementData, fromIndex, numMoved);

		// Let gc do its work
		int newSize = size - (toIndex - fromIndex);
		while (size != newSize)
			elementData[--size] = 0x00;
		return oldBytes;
	}

	public byte[] pullAndRemove(int fromIndex, int toIndex) {
		byte[] newBytes = new byte[toIndex - fromIndex];
		System.arraycopy(elementData, fromIndex, newBytes, 0, newBytes.length);
		removeRange(0, toIndex);
		return newBytes;
	}

	private void rangeCheck(int index) {
		if (index >= size)
			throw new IndexOutOfBoundsException("Index: " + index + ", Size: "
					+ size);
	}

	public int size() {
		return size;
	}

	public void clear() {
		this.size = 0;
		this.elementData = new byte[initialCapacity];
	}

	public byte[] toArray() {
		return Arrays.copyOf(elementData, size);
	}

}
