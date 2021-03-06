/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.order.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 订单
 * 
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 * 
 */
public class Order {
	/** 流水号 */
	private String serialNumber;
	/** 买家联系电话 */
	private String buyerPhone;
	/** 买家姓名 */
	private String buyerName;
	/** 代理商 */
	private long agentId;
	/** 商店 */
	private long shopId;
	/** 商店名称 */
	private String shopName;
	/** 商店联系方式 */
	private String shopPhone;
	/** 商店地址 */
	private Address shopAddress = new Address();
	/** 配送人员 */
	private long workerId;
	/** 配送人员姓名 */
	private String workerName;
	/** 配送人员联系电话 */
	private String workerPhone;
	/** 订单状态 */
	private OrderStatus status = OrderStatus.Unsubmit;
	/** 提交时间 */
	private Date submitTime;
	/** 完成时间 */
	private Date completeTime;
	/** 备货时间 */
	private int prepareTime = 0;
	/** 送货地址 */
	private Address deliveryAddress = new Address();
	/** 订单备注 */
	private String comment;
	/** 配送费 */
	private double costOfRunErrands = 0;
	/** 商店配送费 */
	private double shopCostOfRunErrands = 0;
	/** 商店免费配送最低起送额 */
	private double shopMinChargeForFreeDelivery = 0;
	/** 订单项 */
	private List<OrderItem> orderItems = new ArrayList<OrderItem>();

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public String getBuyerPhone() {
		return buyerPhone;
	}

	public void setBuyerPhone(String buyerPhone) {
		this.buyerPhone = buyerPhone;
	}

	public String getBuyerName() {
		return buyerName;
	}

	public void setBuyerName(String buyerName) {
		this.buyerName = buyerName;
	}

	public long getAgentId() {
		return agentId;
	}

	public void setAgentId(long agentId) {
		this.agentId = agentId;
	}

	public long getShopId() {
		return shopId;
	}

	public void setShopId(long shopId) {
		this.shopId = shopId;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public String getShopPhone() {
		return shopPhone;
	}

	public void setShopPhone(String shopPhone) {
		this.shopPhone = shopPhone;
	}

	public Address getShopAddress() {
		return shopAddress;
	}

	public void setShopAddress(Address shopAddress) {
		this.shopAddress = shopAddress;
	}

	public long getWorkerId() {
		return workerId;
	}

	public void setWorkerId(long workerId) {
		this.workerId = workerId;
	}

	public String getWorkerName() {
		return workerName;
	}

	public void setWorkerName(String workerName) {
		this.workerName = workerName;
	}

	public String getWorkerPhone() {
		return workerPhone;
	}

	public void setWorkerPhone(String workerPhone) {
		this.workerPhone = workerPhone;
	}

	public OrderStatus getStatus() {
		return status;
	}

	public void setStatus(OrderStatus status) {
		this.status = status;
	}

	public Date getSubmitTime() {
		return submitTime;
	}

	public void setSubmitTime(Date submitTime) {
		this.submitTime = submitTime;
	}

	public Date getCompleteTime() {
		return completeTime;
	}

	public void setCompleteTime(Date completeTime) {
		this.completeTime = completeTime;
	}

	public int getPrepareTime() {
		return prepareTime;
	}

	public void setPrepareTime(int prepareTime) {
		this.prepareTime = prepareTime;
	}

	public List<OrderItem> getOrderItems() {
		return orderItems;
	}

	public void setOrderItems(List<OrderItem> orderItems) {
		if (orderItems != null) {
			this.orderItems.clear();
			this.orderItems.addAll(orderItems);
		}
	}

	public void addOrderItem(OrderItem orderItem) {
		this.orderItems.add(orderItem);
	}

	public Address getDeliveryAddress() {
		return deliveryAddress;
	}

	public void setDeliveryAddress(Address deliveryAddress) {
		this.deliveryAddress = deliveryAddress;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public double getCostOfRunErrands() {
		return costOfRunErrands;
	}

	public void setCostOfRunErrands(double costOfRunErrands) {
		this.costOfRunErrands = costOfRunErrands;
	}

	public double getShopCostOfRunErrands() {
		return shopCostOfRunErrands;
	}

	public void setShopCostOfRunErrands(double shopCostOfRunErrands) {
		this.shopCostOfRunErrands = shopCostOfRunErrands;
	}

	public double getShopMinChargeForFreeDelivery() {
		return shopMinChargeForFreeDelivery;
	}

	public void setShopMinChargeForFreeDelivery(
			double shopMinChargeForFreeDelivery) {
		this.shopMinChargeForFreeDelivery = shopMinChargeForFreeDelivery;
	}

	public void initOrderItemProperties() {
		for (OrderItem orderItem : orderItems) {
			orderItem.setAgentId(agentId);
		}
	}
}
