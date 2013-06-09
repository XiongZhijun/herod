/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.order.web.servlet;

import java.util.UUID;

import org.apache.commons.lang.StringUtils;

/**
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 * 
 */
public class FileUtils {

	public static String rename(String originalFileName, String newFileName) {
		String suffix = getSuffix(originalFileName);
		return StringUtils.isNotBlank(suffix) ? (newFileName + "." + suffix)
				: newFileName;
	}

	public static String getSuffix(String fileName) {
		int beginIndex = fileName.lastIndexOf(".");
		if (beginIndex > 0) {
			return fileName.substring(beginIndex + 1);
		}
		return "";
	}

	public static String buildUUID() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}
}
