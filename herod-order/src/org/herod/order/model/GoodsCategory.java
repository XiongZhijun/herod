/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.order.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 商品分类
 * 
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 * 
 */
public class GoodsCategory {
	private long id;
	/** 名称 */
	private String name;
	/** 别名 */
	private String alias;
	/** 所属商店 */
	private long shopId;
	/** 父分类 */
	private long parentId;
	/** 子分类，所有分类只有两层 */
	private List<GoodsCategory> subCategories = new ArrayList<GoodsCategory>();

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

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public long getShopId() {
		return shopId;
	}

	public void setShopId(long shopId) {
		this.shopId = shopId;
	}

	public long getParentId() {
		return parentId;
	}

	public void setParentId(long parentId) {
		this.parentId = parentId;
	}

	public List<GoodsCategory> getSubCategories() {
		return subCategories;
	}

	public void setSubCategories(List<GoodsCategory> subCategories) {
		if (subCategories != null) {
			this.subCategories.clear();
			this.subCategories.addAll(subCategories);
		}
	}

	public boolean isTop() {
		return parentId <= 0;
	}

	public void addSubCategory(GoodsCategory subCategory) {
		this.subCategories.add(subCategory);
	}

}
