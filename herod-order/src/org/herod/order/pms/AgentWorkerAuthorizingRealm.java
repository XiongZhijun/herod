/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved.
 */
package org.herod.order.pms;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.herod.order.model.Token;
import org.herod.order.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 */
public class AgentWorkerAuthorizingRealm extends AuthorizingRealm {
	@Autowired
	private LoginService loginService;

	@Override
	public boolean supports(AuthenticationToken token) {
		return token != null
				&& (AgentWorkerToken.class.isAssignableFrom(token.getClass()));
	}

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(
			PrincipalCollection principals) {
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		return info;
	}

	public CredentialsMatcher getCredentialsMatcher() {
		return new MyCredentialsMatcher();
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(
			AuthenticationToken token) throws AuthenticationException {
		AgentWorkerToken uptToken = (AgentWorkerToken) token;
		String username = uptToken.getUsername();
		String password = uptToken.getPassword();
		return new SimpleAuthenticationInfo(username, password, getName());
	}

	public void setLoginService(LoginService loginService) {
		this.loginService = loginService;
	}

	class MyCredentialsMatcher implements CredentialsMatcher {
		public boolean doCredentialsMatch(AuthenticationToken token,
				AuthenticationInfo info) {
			AgentWorkerToken uptToken = (AgentWorkerToken) token;
			String name = uptToken.getUsername();
			String password = uptToken.getPassword();
			String imei = uptToken.getImei();
			Token result = loginService.doLogin(name, password, imei);
			return result != null;
		}

	}
}
