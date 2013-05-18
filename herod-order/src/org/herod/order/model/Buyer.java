/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.order.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 买家，购买商品的用户
 * 
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 * 
 */
public class Buyer {
	private long id;
	/** 姓名 */
	private String name;
	/** 电话 */
	private String phone;
	/** 密码 */
	private String password;
	/** 常用地址 */
	private List<Address> usedAddresses = new ArrayList<Address>();

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

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<Address> getUsedAddresses() {
		return usedAddresses;
	}

	public void setUsedAddresses(List<Address> usedAddresses) {
		this.usedAddresses = usedAddresses;
	}

}
