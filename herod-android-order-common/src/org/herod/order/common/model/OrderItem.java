/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.order.common.model;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * 订单项
 * 
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 * 
 */
@JsonIgnoreProperties({ "totalAmount" })
public class OrderItem implements Serializable {
	private static final long serialVersionUID = -3497838304695783267L;
	/** 流水号 */
	private String serialNumber;
	/** 订单流水号 */
	private String orderSerialNumber;
	/** 商品Id */
	private long goodsId;
	/** 商品编码 */
	private String goodsCode;
	private String goodsName;
	/** 代理商 */
	private long agentId;
	/** 商店 */
	private long shopId;
	private String shopName;
	/** 成交单价 */
	private double sellingPrice;
	private double supplyPrice;
	/** 数量 */
	private int quantity;
	/** 原始数量 */
	private int originalQuantity;
	/** 订单项标记 */
	private OrderItemFlag flag = OrderItemFlag.Common;

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public String getOrderSerialNumber() {
		return orderSerialNumber;
	}

	public void setOrderSerialNumber(String orderSerialNumber) {
		this.orderSerialNumber = orderSerialNumber;
	}

	public long getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(long goodsId) {
		this.goodsId = goodsId;
	}

	public String getGoodsCode() {
		return goodsCode;
	}

	public void setGoodsCode(String goodsCode) {
		this.goodsCode = goodsCode;
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

	public double getSellingPrice() {
		return sellingPrice;
	}

	public void setSellingPrice(double sellingPrice) {
		this.sellingPrice = sellingPrice;
	}

	public double getSupplyPrice() {
		return supplyPrice;
	}

	public void setSupplyPrice(double supplyPrice) {
		this.supplyPrice = supplyPrice;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public int getOriginalQuantity() {
		return originalQuantity;
	}

	public void setOriginalQuantity(int originalQuantity) {
		this.originalQuantity = originalQuantity;
	}

	public OrderItemFlag getFlag() {
		return flag;
	}

	public void setFlag(OrderItemFlag flag) {
		this.flag = flag;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public double getTotalAmount() {
		return sellingPrice * quantity;
	}

	public void increaseQuantity() {
		quantity++;
	}

	public void decreaseQuantity() {
		if (quantity <= 0) {
			quantity = 0;
		} else {
			quantity--;
		}
	}

}
