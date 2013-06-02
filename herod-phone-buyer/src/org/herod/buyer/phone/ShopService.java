/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.buyer.phone;

import org.herod.framework.MapWrapper;

/**
 * 
 * 
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 * 
 */
public interface ShopService {

	MapWrapper<String, Object> findShopById(long shopId);
}
