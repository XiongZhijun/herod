/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved.
 */
package org.herod.worker.phone.model;

import java.io.Serializable;

import org.herod.framework.lbs.Location;
import org.herod.order.common.model.Address;

/**
 * 
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 */
public class MapAddress implements Serializable {
	private static final long serialVersionUID = 7702853929087304816L;
	private Address address;
	private AddressType type;

	public MapAddress(Address address, AddressType type) {
		super();
		this.address = address;
		this.type = type;
	}

	public MapAddress(String address, Location location) {
		this(new Address(address, location), AddressType.Current);
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public AddressType getType() {
		return type;
	}

	public void setType(AddressType type) {
		this.type = type;
	}

	public static enum AddressType {
		Current, Shop, Buyer
	}
}
