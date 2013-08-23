/*
 * 香港摩比科技有限公司 版权所有
 *
 * www.mobi-inf.com
 */
package org.herod.event;

import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Xiong Zhijun
 * 
 */
public class EventUtils {
	/**  */
	public static final String UTF_8 = "UTF-8";
	private static final String EVENT_REGEX = "^(\\w+)(\\|(.+))?$";
	private static final Pattern EVENT_PATTERN = Pattern.compile(EVENT_REGEX);

	public static byte[] toByteArray(Event event) {
		String str = toString(event);
		try {
			return str.getBytes(UTF_8);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("get byte array failed : " + str, e);
		}
	}

	public static String toString(Event event) {
		StringBuilder sb = new StringBuilder();
		sb.append(event.getCode());
		Map<String, String> properties = event.getProperties();
		if (properties.size() > 0) {
			sb.append("|");
		}
		for (Entry<String, String> entry : properties.entrySet()) {
			sb.append(entry.getKey()).append("=").append(entry.getValue())
					.append(";");
		}
		int length = properties.size() > 0 ? sb.length() - 1 : sb.length();
		return sb.substring(0, length);
	}

	public static Event parse(byte[] bytes) {
		if (bytes == null || bytes.length == 0) {
			return null;
		}
		String eventString;
		try {
			eventString = new String(bytes, UTF_8);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("parse event failed.", e);
		}
		return parse(eventString);
	}

	public static Event parse(String eventString) {
		if (eventString == null) {
			return null;
		}
		Matcher matcher = EVENT_PATTERN.matcher(eventString);
		if (!matcher.matches()) {
			return null;
		}
		Event event = new Event();
		event.setCode(matcher.group(1));
		String propertiesString = matcher.group(3);
		initProperties(event, propertiesString);
		return event;
	}

	private static void initProperties(Event event, String propertiesString) {
		if (propertiesString == null || propertiesString.length() == 0) {
			return;
		}
		String[] properties = propertiesString.split(";");
		for (String property : properties) {
			String[] keyValues = property.split("=");
			if (keyValues.length == 2) {
				event.put(keyValues[0], keyValues[1]);
			}
		}
	}

}
