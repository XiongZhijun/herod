/*
 * Copyright 2010 FPI,Inc. All rights reserved.
 * http://www.fpi-inc.com
 */
package org.herod.communication.common;

import java.util.Arrays;
import java.util.EmptyStackException;

/**
 * 
 * @author Xiong Zhijun
 * @since sword_accessserver
 * @create 2010-9-17
 * 
 */
public class ByteStack {

	private byte[] element;

	private int size;

	public ByteStack(byte[] element) {
		if (element == null) {
			throw new IllegalArgumentException("null element.");
		}
		this.element = Arrays.copyOf(element, element.length);
		this.size = element.length;
	}

	public byte peek() {
		if (size == 0)
			throw new EmptyStackException();
		return element[0];
	}

	public byte[] peek(int count) {
		if (size < count)
			throw new IllegalArgumentException("count is too large.");
		return Arrays.copyOf(element, count);
	}

	public byte pop() {
		byte b = peek();
		removeElementAt(0);
		return b;
	}

	public byte[] pop(int count) {
		byte[] result = peek(count);
		removeElementOfRange(0, count);
		return result;
	}

	private void removeElementOfRange(int from, int to) {
		if (to > size) {
			throw new ArrayIndexOutOfBoundsException(to + " >= " + size);
		} else if (from < 0) {
			throw new ArrayIndexOutOfBoundsException(from);
		} else if (from >= to) {
			throw new IllegalArgumentException(from + " > " + to);
		}

		System.arraycopy(element, to, element, from, size - to);

		size -= to;
	}

	private void removeElementAt(int index) {
		if (index >= size) {
			throw new ArrayIndexOutOfBoundsException(index + " >= " + size);
		} else if (index < 0) {
			throw new ArrayIndexOutOfBoundsException(index);
		}
		int j = size - index - 1;
		if (j > 0) {
			System.arraycopy(element, index + 1, element, index, j);
		}
		size--;
	}

	public int size() {
		return size;
	}
}
