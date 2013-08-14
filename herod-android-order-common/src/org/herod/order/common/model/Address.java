/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.order.common.model;

import java.io.Serializable;

import org.herod.framework.lbs.Location;

/**
 * 买家常用地址
 * 
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 * 
 */
public class Address implements Serializable {
	private static final long serialVersionUID = -348084199235297187L;
	/** 地址 */
	private String address;
	/** 地理位置 */
	private Location location;

	public Address() {
		super();
	}

	public Address(String address, Location location) {
		super();
		this.address = address;
		this.location = location;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

}
