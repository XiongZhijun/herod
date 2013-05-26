/*
 * 香港摩比科技有限公司 版权所有
 *
 * www.mobi-inf.com
 */
package org.herod.framework;


public class AndroidException extends RuntimeException {

	public AndroidException() {
		super();
	}

	public AndroidException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	public AndroidException(String detailMessage) {
		super(detailMessage);
	}

	public AndroidException(Throwable throwable) {
		super(throwable);
	}

	private static final long serialVersionUID = 2942168444829360851L;

}
