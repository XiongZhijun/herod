/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.framework.utils;

import org.herod.framework.AndroidException;

/**
 * @author Xiong Zhijun
 * 
 */
public class ClassException extends AndroidException {

	public ClassException() {
		super();
	}

	public ClassException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	public ClassException(String detailMessage) {
		super(detailMessage);
	}

	public ClassException(Throwable throwable) {
		super(throwable);
	}

	/**  */
	private static final long serialVersionUID = -3874190048206554826L;

}
