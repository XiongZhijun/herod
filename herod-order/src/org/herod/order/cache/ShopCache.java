/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.order.cache;

import java.util.List;

import org.herod.order.model.Location;
import org.herod.order.model.Shop;

/**
 * 
 * 
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 * 
 */
public interface ShopCache {

	List<Shop> findShopsByLocation(Location location);
}
