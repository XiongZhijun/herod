/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.framework.tools;

import java.util.Date;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * 
 * 
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 * 
 */
public class GsonUtils {

	public static Gson buildDeaultGson() {
		return new GsonBuilder().registerTypeAdapter(Date.class,
				new DateTypeAdapter()).create();
	}
}
