/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.order.das;

import java.util.ArrayList;
import java.util.List;

import org.herod.common.das.HerodBeanPropertyRowMapper;
import org.herod.order.model.Goods;
import org.herod.order.model.GoodsCategory;
import org.herod.order.service.SimplePhoneBuyerService.GoodsCategoryQueryService;
import org.herod.order.service.SimplePhoneBuyerService.GoodsQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

/**
 * 
 * 
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 * 
 */
public class SimpleGoodsDas implements GoodsCategoryQueryService,
		GoodsQueryService {
	private static final String QUERY_CATEGORY_BY_SHOP_SQL = "SELECT ID,NAME,ALIAS,SHOP_ID,PARENT_ID FROM HEROD_GOODS_CATEGORY where SHOP_ID = ?";
	private static final String QUERY_GOODS_SQL = "SELECT ID,CODE,NAME,ALIAS,SUPPLY_PRICE,SELLING_PRICE,"
			+ "UNIT,COMMENT,LARGE_IMAGE,THUMBNAIL,CATEGORY_ID,SHOP_ID "
			+ "FROM HEROD_GOODS WHERE SHOP_ID = ? AND CATEGORY_ID = ?";
	@Autowired
	@Qualifier("simpleJdbcTemplate")
	private SimpleJdbcTemplate simpleJdbcTemplate;

	@Override
	public List<Goods> findGoodsByCategory(long shopId, long categoryId) {
		RowMapper<Goods> rm = new HerodBeanPropertyRowMapper<Goods>(Goods.class);
		return simpleJdbcTemplate
				.query(QUERY_GOODS_SQL, rm, shopId, categoryId);
	}

	@Override
	public List<GoodsCategory> findGoodsCategoryByShop(long shopId) {
		RowMapper<GoodsCategory> rm = new HerodBeanPropertyRowMapper<GoodsCategory>(
				GoodsCategory.class);
		List<GoodsCategory> categories = simpleJdbcTemplate.query(
				QUERY_CATEGORY_BY_SHOP_SQL, rm, shopId);
		return toTree(categories);
	}

	protected List<GoodsCategory> toTree(List<GoodsCategory> queryList) {
		List<GoodsCategory> categories = new ArrayList<GoodsCategory>();
		for (GoodsCategory category : queryList) {
			if (category.isTop()) {
				categories.add(category);
			}
		}
		queryList.removeAll(categories);
		for (GoodsCategory category : categories) {
			for (GoodsCategory subCategory : queryList) {
				if (subCategory.getParentId() == category.getId()) {
					category.addSubCategory(subCategory);
				}
			}
		}
		return categories;
	}

	public void setSimpleJdbcTemplate(SimpleJdbcTemplate simpleJdbcTemplate) {
		this.simpleJdbcTemplate = simpleJdbcTemplate;
	}

}
