/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.order.model;

import java.util.Date;

/**
 * 
 * 
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 * 
 */
public class OrderLog {
	private String orderSerialNumber;
	private Operation operation;
	private String reason;
	private OperatorType operatorType;
	private String operator;
	private Date operateTime = new Date();

	public String getOrderSerialNumber() {
		return orderSerialNumber;
	}

	public void setOrderSerialNumber(String orderSerialNumber) {
		this.orderSerialNumber = orderSerialNumber;
	}

	public Operation getOperation() {
		return operation;
	}

	public void setOperation(Operation operation) {
		this.operation = operation;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public OperatorType getOperatorType() {
		return operatorType;
	}

	public void setOperatorType(OperatorType operatorType) {
		this.operatorType = operatorType;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public Date getOperateTime() {
		return operateTime;
	}

	public void setOperateTime(Date operateTime) {
		this.operateTime = operateTime;
	}

	public static enum OperatorType {
		/** 买家 */
		Buyer,
		/** 代理商工作人员 */
		AgentWorker
	}

	public static enum Operation {
		/** 提交订单 */
		Submit,
		/** 受理订单 */
		Accept,
		/** 拒绝订单 */
		Reject,
		/** 取消订单 */
		Cancel,
		/** 完成订单 */
		Complete,
		/** 更新订单 */
		Update
	}
}
