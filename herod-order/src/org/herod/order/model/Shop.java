/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.order.model;

/**
 * 店铺，每一个店铺拥有很多商品，并可以对商品进行分类
 * 
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 * 
 */
public class Shop {
	private long id;
	/** 店名 */
	private String name;
	/** 所属代理商 */
	private long agentId;
	/** 地址 */
	private String address;
	/** 联系电话 */
	private String contactNumber;
	/** 地理位置 */
	private Location location;
	/** 服务开始时间 */
	private String serviceBeginTime;
	/** 服务结束时间 */
	private String serviceEndTime;
	/** 服务区间 */
	private int serviceArea;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public String getServiceBeginTime() {
		return serviceBeginTime;
	}

	public void setServiceBeginTime(String serviceBeginTime) {
		this.serviceBeginTime = serviceBeginTime;
	}

	public String getServiceEndTime() {
		return serviceEndTime;
	}

	public void setServiceEndTime(String serviceEndTime) {
		this.serviceEndTime = serviceEndTime;
	}

	public long getAgentId() {
		return agentId;
	}

	public void setAgentId(long agentId) {
		this.agentId = agentId;
	}

	public int getServiceArea() {
		return serviceArea;
	}

	public void setServiceArea(int serviceArea) {
		this.serviceArea = serviceArea;
	}
}
