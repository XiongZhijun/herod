/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
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
