/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.worker.phone.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * 
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 * 
 */
public class OrderUpdateInfo {
	private String orderSerialNumber;
	private String newComment;
	private List<String> deletedOrderItems = new ArrayList<String>();
	private List<OrderItem> newOrderItems = new ArrayList<OrderItem>();
	private Map<String, Integer> quantityChangeMap = new HashMap<String, Integer>();
	private String reason;

	public OrderUpdateInfo() {
		super();
	}

	public OrderUpdateInfo(String orderSerialNumber) {
		super();
		this.orderSerialNumber = orderSerialNumber;
	}

	public String getOrderSerialNumber() {
		return orderSerialNumber;
	}

	public void setOrderSerialNumber(String orderSerialNumber) {
		this.orderSerialNumber = orderSerialNumber;
	}

	public String getNewComment() {
		return newComment;
	}

	public void setNewComment(String newComment) {
		this.newComment = newComment;
	}

	public List<String> getDeletedOrderItems() {
		return deletedOrderItems;
	}

	public void setDeletedOrderItems(List<String> deletedOrderItems) {
		this.deletedOrderItems = deletedOrderItems;
	}

	public List<OrderItem> getNewOrderItems() {
		return newOrderItems;
	}

	public void setNewOrderItems(List<OrderItem> newOrderItems) {
		this.newOrderItems = newOrderItems;
	}

	public Map<String, Integer> getQuantityChangeMap() {
		return quantityChangeMap;
	}

	public void setQuantityChangeMap(Map<String, Integer> quantityChangeMap) {
		this.quantityChangeMap = quantityChangeMap;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

}
