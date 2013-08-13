/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved.
 */
package org.herod.order.pms;

import org.apache.shiro.authc.HostAuthenticationToken;
import org.apache.shiro.authc.RememberMeAuthenticationToken;

/**
 * 
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 */
public class AgentWorkerToken implements HostAuthenticationToken,
		RememberMeAuthenticationToken {
	private static final long serialVersionUID = 899575521958279932L;

	private String username;

	private String password;

	private boolean rememberMe = false;

	private String host;

	private String imei;

	public AgentWorkerToken(String username, String password,
			boolean rememberMe, String host, String imei) {
		this.username = username;
		this.password = password;
		this.rememberMe = rememberMe;
		this.host = host;
		this.imei = imei;
	}

	@Override
	public Object getPrincipal() {
		return username;
	}

	@Override
	public Object getCredentials() {
		return password;
	}

	@Override
	public boolean isRememberMe() {
		return rememberMe;
	}

	@Override
	public String getHost() {
		return host;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public String getImei() {
		return imei;
	}

	public void clear() {
		this.username = null;
		this.host = null;
		this.rememberMe = false;
		this.password = null;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getClass().getName());
		sb.append(" - ");
		sb.append(username);
		sb.append(", rememberMe=").append(rememberMe);
		if (host != null) {
			sb.append(" (").append(host).append(")");
		}
		return sb.toString();
	}

}
