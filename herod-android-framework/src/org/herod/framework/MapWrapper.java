/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.framework;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.herod.framework.utils.MapUtils;

/**
 * 
 * 
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 * 
 */
public class MapWrapper<K, V> implements Serializable {
	private static final long serialVersionUID = -5747357541078088745L;
	private HashMap<K, V> map = new HashMap<K, V>();

	public MapWrapper(Map<K, V> map) {
		super();
		this.map.putAll(map);
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((map == null) ? 0 : map.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MapWrapper<?, ?> other = (MapWrapper<?, ?>) obj;
		if (map == null) {
			if (other.map != null)
				return false;
		} else if (!map.equals(other.map))
			return false;
		return true;
	}

}
