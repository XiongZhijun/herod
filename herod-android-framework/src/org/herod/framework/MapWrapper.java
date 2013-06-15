/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.framework;

import java.util.Map;

import org.herod.framework.utils.MapUtils;

/**
 * 
 * 
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 * 
 */
public class MapWrapper<K, V> {
	private Map<K, V> map;

	public MapWrapper(Map<K, V> map) {
		super();
		this.map = map;
	}

	public long getLong(K key) {
		return MapUtils.getLong(map, key);
	}

	public double getDouble(K key) {
		return MapUtils.getDouble(map, key);
	}

	public int getInt(K key) {
		return MapUtils.getInt(map, key);
	}

	public String getString(K key) {
		return MapUtils.getString(map, key);
	}

	public V get(K key) {
		return map.get(key);
	}

	public boolean containsKey(K key) {
		return map.containsKey(key);
	}

}
