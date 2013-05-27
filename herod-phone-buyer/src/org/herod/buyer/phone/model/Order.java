/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.buyer.phone.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.location.Address;

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
	/** 处理的人 */
	private long workerId;
	/** 订单状态 */
	private OrderStatus status = OrderStatus.Unsubmit;
	/** 提交时间 */
	private Date submitTime;
	/** 完成时间 */
	private Date completeTime;
	/** 备货时间 */
	private int prepareTime = 0;
	/** 送货地址 */
	private Address deliveryAddress;
	/** 订单备注 */
	private String comment;
	/** 订单项 */
	private List<OrderItem> orderItems = new ArrayList<OrderItem>();
	/** 跑腿费 */
	private double costOfRunErrands = 0;

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
		updateShopIdWithItems();
	}

	public long getWorkerId() {
		return workerId;
	}

	public void setWorkerId(long workerId) {
		this.workerId = workerId;
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
			updateShopIdWithItems();
		}
	}

	protected void updateShopIdWithItems() {
		for (OrderItem item : orderItems) {
			item.setShopId(shopId);
		}
	}

	public void addOrderItem(OrderItem orderItem) {
		orderItem.setShopId(shopId);
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

	public int getTotalQuantity() {
		int total = 0;
		for (OrderItem orderItem : orderItems) {
			total += orderItem.getQuantity();
		}
		return total;
	}

	public OrderItem findOrderItemByGoodsId(long goodsId) {
		for (OrderItem orderItem : orderItems) {
			if (orderItem.getGoodsId() == goodsId) {
				return orderItem;
			}
		}
		return null;
	}

	public void removeOrderItemByGoodsId(long goodsId) {
		for (OrderItem orderItem : orderItems) {
			if (orderItem.getGoodsId() == goodsId) {
				orderItems.remove(orderItem);
				break;
			}
		}
	}

	public double getTotalAmount() {
		double total = 0;
		for (OrderItem orderItem : orderItems) {
			total += orderItem.getTotalAmount();
		}
		return total;
	}

	public double getTotalAmountWithCostOfRunErrands() {
		return getTotalAmount() + getCostOfRunErrands();
	}

}
