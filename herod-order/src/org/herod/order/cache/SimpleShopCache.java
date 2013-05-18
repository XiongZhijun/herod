/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.order.cache;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.herod.order.model.Location;
import org.herod.order.model.Shop;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 
 * 
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 * 
 */
public class SimpleShopCache implements ShopCache {
	@Autowired
	private ShopQueryService shopQueryService;
	private List<Shop> allShops = new ArrayList<Shop>();

	@Override
	public List<Shop> findShopsByLocation(Location location) {
		List<Shop> shops = new ArrayList<Shop>();
		for (Shop shop : allShops) {
			if (location.isInServiceArea(shop)) {
				shops.add(shop);
			}
		}
		return shops;
	}

	protected void init() {
		if (CollectionUtils.isNotEmpty(allShops)) {
			return;
		}
		List<Shop> shops = shopQueryService.findAllShops();
		if (shops != null) {
			allShops.clear();
			allShops.addAll(shops);
		}
	}

	public void setShopQueryService(ShopQueryService shopQueryService) {
		this.shopQueryService = shopQueryService;
	}

	public static interface ShopQueryService {
		List<Shop> findAllShops();
	}

}
