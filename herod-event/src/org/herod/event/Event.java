/*
 * 香港摩比科技有限公司 版权所有
 *
 * www.mobi-inf.com
 */
package org.herod.event;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.herod.communication.common.ByteFrame;

/**
 * @author Xiong Zhijun
 * 
 */
public class Event implements ByteFrame {

	private String code;
	private Map<String, String> properties = new HashMap<String, String>();

	public String getCode() {
		return code == null ? "" : code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public long getLong(String propertyName) {
		String value = getString(propertyName);
		if (isNotBlank(value)) {
			return Long.parseLong(value);
		}
		return 0;
	}

	public Date getDate(String propertyName) {
		long time = getLong(propertyName);
		return new Date(time);
	}

	public String getString(String propertyName) {
		return properties.get(propertyName);
	}

	public void put(String propertyName, Object value) {
		if (isBlank(propertyName) || value == null) {
			return;
		}
		properties.put(propertyName, valueToString(value));
	}

	private String valueToString(Object value) {
		if (value instanceof Date) {
			return Long.toString(((Date) value).getTime());
		}
		return value.toString();
	}

	public Map<String, String> getProperties() {
		return properties;
	}

	public String toString() {
		return EventUtils.toString(this);
	}

	private static boolean isBlank(String str) {
		return str == null || str.trim().length() == 0;
	}

	private static boolean isNotBlank(String str) {
		return str != null && str.trim().length() > 0;
	}

	@Override
	public byte[] getBytes() {
		return EventUtils.toByteArray(this);
	}
}
