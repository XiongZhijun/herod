/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.framework.db;

/**
 * 
 * 
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 * 
 */
public class ColumnNameUtils {

	public static String toPropertyName(String fieldname) {
		StringBuilder sb = new StringBuilder();
		Status status = Status.NONE;
		for (char c : fieldname.toCharArray()) {
			if (c == '_') {
				status = Status.STRIKE;
				continue;
			} else if (Character.isLetter(c)) {
				if (status == Status.STRIKE && sb.length() > 0) {
					sb.append(Character.toUpperCase(c));
				} else {
					sb.append(Character.toLowerCase(c));
				}
				status = Status.LETTER;
			}

		}
		return sb.toString();
	}

	private static enum Status {
		NONE, STRIKE, LETTER
	}
}
