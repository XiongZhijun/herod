/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.buyer.phone.model;

/**
 * 订单项
 * 
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 * 
 */
public class OrderItem {
	/** 流水号 */
	private String serialNumber;
	/** 订单流水号 */
	private String orderSerialNumber;
	/** 商品Id */
	private long goodsId;
	/** 商品编码 */
	private String goodsCode;
	/** 商品名称 */
	private String goodsName;
	/** 商店 */
	private long shopId;
	/** 成交单价 */
	private double unitPrice;
	/** 数量 */
	private int quantity = 0;
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

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public long getShopId() {
		return shopId;
	}

	public void setShopId(long shopId) {
		this.shopId = shopId;
	}

	public double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(double unitPrice) {
		this.unitPrice = unitPrice;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public OrderItemFlag getFlag() {
		return flag;
	}

	public void setFlag(OrderItemFlag flag) {
		this.flag = flag;
	}

	public double getTotalAmount() {
		return unitPrice * quantity;
	}

}
