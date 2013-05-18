/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.order.service;

import java.util.ArrayList;
import java.util.List;

import org.herod.order.model.Order;
import org.herod.order.model.OrderLog;
import org.herod.order.model.OrderLog.Operation;
import org.herod.order.model.OrderLog.OperatorType;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 
 * 
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 * 
 */
public class SimpleOrderLogService implements OrderLogService {
	@Autowired
	private AgentWorkerIdentityService agentWorkerIdentityService;
	@Autowired
	private LogDas logDas;

	@Override
	public void agentWorkerlog(String orderSerialNumber, Operation operation,
			String reason) {
		OrderLog log = new OrderLog();
		log.setOrderSerialNumber(orderSerialNumber);
		log.setOperation(operation);
		log.setOperator(agentWorkerIdentityService.getCurrentWorkerId() + "");
		log.setOperatorType(OperatorType.AgentWorker);
		log.setReason(reason);
		logDas.addLog(log);
	}

	@Override
	public void buyerSubmitLog(List<Order> orders) {
		List<OrderLog> logs = new ArrayList<OrderLog>();
		for (Order order : orders) {
			OrderLog log = new OrderLog();
			log.setOrderSerialNumber(order.getSerialNumber());
			log.setOperation(Operation.Submit);
			log.setOperator(order.getBuyerPhone());
			log.setOperatorType(OperatorType.Buyer);
			logs.add(log);
		}
		logDas.addLogs(logs);
	}

	public void setAgentWorkerIdentityService(
			AgentWorkerIdentityService agentWorkerIdentityService) {
		this.agentWorkerIdentityService = agentWorkerIdentityService;
	}

	public void setLogDas(LogDas logDas) {
		this.logDas = logDas;
	}

	public static interface LogDas {
		void addLog(OrderLog log);

		void addLogs(List<OrderLog> logs);
	}

}
