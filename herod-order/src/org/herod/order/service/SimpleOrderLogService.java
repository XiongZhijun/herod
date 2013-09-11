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
import org.herod.order.order.OrderCenter;
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
	private LogDas logDas;
	@Autowired
	private OrderCenter orderCenter;

	@Override
	public void agentWorkerlog(long agentId, long workerId,
			String orderSerialNumber, Operation operation, String reason) {
		OrderLog log = new OrderLog();
		log.setOrderSerialNumber(orderSerialNumber);
		log.setOperation(operation);
		log.setOperator(workerId + "");
		log.setOperatorType(OperatorType.AgentWorker);
		log.setReason(reason);
		logDas.addLog(log);
		notifyOrderCenter(agentId, workerId, orderSerialNumber, operation);
	}

	private void notifyOrderCenter(long agentId, long workerId,
			String orderSerialNumber, Operation operation) {
		switch (operation) {
		case Accept:
			orderCenter.acceptOrder(agentId, workerId, orderSerialNumber);
			break;
		case Cancel:
			orderCenter.cancelOrder(agentId, workerId, orderSerialNumber);
			break;
		case Submit:
			orderCenter.submitOrder(agentId, workerId, orderSerialNumber);
			break;
		case Reject:
			orderCenter.rejectOrder(agentId, workerId, orderSerialNumber);
			break;
		case Complete:
			orderCenter.completeOrder(agentId, workerId, orderSerialNumber);
			break;
		case Update:
			// TODO 更新事件
			break;
		default:
			break;
		}
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
			notifyOrderCenter(order.getAgentId(), order.getWorkerId(),
					order.getSerialNumber(), Operation.Submit);
		}
		logDas.addLogs(logs);
	}

	public void setLogDas(LogDas logDas) {
		this.logDas = logDas;
	}

	public void setOrderCenter(OrderCenter orderCenter) {
		this.orderCenter = orderCenter;
	}

	public static interface LogDas {
		void addLog(OrderLog log);

		void addLogs(List<OrderLog> logs);
	}

}
