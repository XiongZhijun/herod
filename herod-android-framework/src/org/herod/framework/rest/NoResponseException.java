/*
 * 香港摩比科技有限公司 版权所有
 *
 * www.mobi-inf.com
 */
package org.herod.framework.rest;


public class NoResponseException extends RestException {

	public NoResponseException() {
		super();
	}

	public NoResponseException(String message, Throwable cause) {
		super(message, cause);
	}

	public NoResponseException(String message) {
		super(message);
	}

	public NoResponseException(Throwable cause) {
		super(cause);
	}

	private static final long serialVersionUID = 6075741956449925443L;

}
