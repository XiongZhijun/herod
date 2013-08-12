/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved.
 */
package org.herod.framework.utils;

import org.herod.framework.ViewFindable;

import android.widget.TextView;

/**
 * 
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 */
public abstract class TextViewUtils {

	public static void setText(ViewFindable viewFindable, int id, Object data) {
		if (data != null) {
			((TextView) viewFindable.findViewById(id)).setText(data.toString());
		}
	}
}
