/*
 * 香港摩比科技有限公司 版权所有
 *
 * www.mobi-inf.com
 */
package org.herod.communication.common;

/**
 * @author Xiong Zhijun
 * 
 */
public class ByteFrameWrapper implements ByteFrame {
	private byte[] bytes;

	public ByteFrameWrapper(byte[] bytes) {
		super();
		this.bytes = bytes;
	}

	@Override
	public byte[] getBytes() {
		return bytes == null ? new byte[0] : bytes;
	}

}
