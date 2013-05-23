/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.buyer.phone;

import java.util.Collections;
import java.util.Map;

import android.content.Context;
import android.widget.SimpleAdapter;

/**
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 * 
 */
public abstract class SimpleAdapterUtils {

	public static SimpleAdapter createSimpleAdapter(Context context,
			int resource, String[] from, int[] to) {
		return new SimpleAdapter(context,
				Collections.<Map<String, ?>> emptyList(), resource, from, to);
	}
}
