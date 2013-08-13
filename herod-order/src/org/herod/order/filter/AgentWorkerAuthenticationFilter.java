/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved.
 */
package org.herod.order.filter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.web.util.WebUtils;
import org.herod.order.pms.AgentWorkerToken;
import org.herod.order.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 */
public class AgentWorkerAuthenticationFilter extends HerodAuthenticationFilter {

	private String imeiParam = "imei";
	private String tokenParam = "token";
	private String workerPhoneParam = "workerPhone";
	private String workerPasswordParam = "workerPassword";
	@Autowired
	private LoginService loginService;

	protected AuthenticationToken createToken(ServletRequest request,
			ServletResponse response) {
		String username = getWorkerPhone(request);
		String password = getWorkerPassword(request);
		String imei = getImei(request);
		boolean rememberMe = isRememberMe(request);
		String host = getHost(request);
		return new AgentWorkerToken(username, password, rememberMe, host, imei);
	}

	protected boolean isAccessAllowed(ServletRequest request,
			ServletResponse response, Object mappedValue) {
		long workerId = loginService.getWorkerId(getToken(request),
				getImei(request));
		return workerId > 0;
	}

	protected boolean isLoginRequest(ServletRequest request,
			ServletResponse response) {
		String url = ((HttpServletRequest) request).getRequestURL().toString();
		return url.indexOf("/rest/herod/agentworker/login") >= 0;
	}

	protected String getWorkerPhone(ServletRequest request) {
		return WebUtils.getCleanParam(request, getWorkerPhoneParam());
	}

	protected String getWorkerPassword(ServletRequest request) {
		return WebUtils.getCleanParam(request, getWorkerPasswordParam());
	}

	protected String getImei(ServletRequest request) {
		return WebUtils.getCleanParam(request, getImeiParam());
	}

	protected String getToken(ServletRequest request) {
		return WebUtils.getCleanParam(request, getTokenParam());
	}

	public LoginService getLoginService() {
		return loginService;
	}

	public void setLoginService(LoginService loginService) {
		this.loginService = loginService;
	}

	public void setImeiParam(String imeiParam) {
		this.imeiParam = imeiParam;
	}

	public void setTokenParam(String tokenParam) {
		this.tokenParam = tokenParam;
	}

	private String getWorkerPhoneParam() {
		return workerPhoneParam;
	}

	private String getWorkerPasswordParam() {
		return workerPasswordParam;
	}

	protected String getImeiParam() {
		return imeiParam;
	}

	protected String getTokenParam() {
		return tokenParam;
	}
}
