/**
 * Copyright@2012 FPI,Inc. All rights reserved.
 * 
 * Project	: mobi-platform-order-monitor
 * Filename	: MobiAuthenticationFilter.java
 * create	: Binghua Zhou, 2013-5-31
 *
 */
package org.herod.order.filter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;

import com.fpi.bear.permission.filter.BearAuthenticationFilter;

public class HerodAuthenticationFilter extends BearAuthenticationFilter {

	protected boolean onLoginSuccess(AuthenticationToken token,
			Subject subject, ServletRequest request, ServletResponse response)
			throws Exception {
		return true;
	}

	protected boolean onLoginFailure(AuthenticationToken token,
			AuthenticationException e, ServletRequest request,
			ServletResponse response) {
		setFailureAttribute(request, e);
		return false;
	}

}
