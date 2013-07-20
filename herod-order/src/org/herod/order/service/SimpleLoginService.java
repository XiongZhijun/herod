/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved.
 */
package org.herod.order.service;

import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.commons.lang.StringUtils;
import org.herod.order.model.Token;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 */
public class SimpleLoginService implements LoginService {
	@Autowired
	private AgentWorkerQueryService agentWorkerQueryService;
	private ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
	private Lock readLock = readWriteLock.readLock();
	private Lock writeLock = readWriteLock.writeLock();
	private Set<WorkerLoginInfo> loginInfoSet = new HashSet<WorkerLoginInfo>();

	@Override
	public Token doLogin(String name, String password, String imei) {
		Map<String, Object> worker = agentWorkerQueryService
				.findWorkerByNameAndPassword(name, password);
		if (worker == null) {
			return null;
		}
		WorkerLoginInfo workerLoginInfo = buildWorkerLoginInfo(name, imei);
		addToSet(workerLoginInfo);
		return new Token(workerLoginInfo.tokenString);
	}

	private void addToSet(WorkerLoginInfo workerLoginInfo) {
		writeLock.lock();
		try {
			loginInfoSet.add(workerLoginInfo);
		} finally {
			writeLock.unlock();
		}
	}

	@Override
	public boolean isUserValid(Token token, String imei) {
		readLock.lock();
		try {
			for (WorkerLoginInfo loginInfo : loginInfoSet) {
				if (StringUtils.equals(loginInfo.imei, imei)
						&& StringUtils.equals(loginInfo.tokenString,
								token.getTokenString())) {
					return true;
				}
			}
		} finally {
			readLock.unlock();
		}
		return false;
	}

	public void setAgentWorkerQueryService(
			AgentWorkerQueryService agentWorkerQueryService) {
		this.agentWorkerQueryService = agentWorkerQueryService;
	}

	private WorkerLoginInfo buildWorkerLoginInfo(String name, String imei) {
		WorkerLoginInfo workerLoginInfo = new WorkerLoginInfo();
		workerLoginInfo.name = name;
		workerLoginInfo.imei = imei;
		workerLoginInfo.tokenString = UUID.randomUUID().toString();
		workerLoginInfo.loginTime = new Date();
		return workerLoginInfo;
	}

	public static interface AgentWorkerQueryService {
		Map<String, Object> findWorkerByNameAndPassword(String name,
				String password);
	}

	class WorkerLoginInfo {
		String name;
		String tokenString;
		Date loginTime;
		String imei;

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + ((name == null) ? 0 : name.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			WorkerLoginInfo other = (WorkerLoginInfo) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (name == null) {
				if (other.name != null)
					return false;
			} else if (!name.equals(other.name))
				return false;
			return true;
		}

		private SimpleLoginService getOuterType() {
			return SimpleLoginService.this;
		}
	}

}
