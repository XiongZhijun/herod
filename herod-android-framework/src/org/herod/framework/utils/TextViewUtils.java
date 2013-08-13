/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved.
 */
package org.herod.framework.utils;

import org.herod.framework.ViewFindable;

import android.content.Context;
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

	public static void setText(Context context, ViewFindable viewFindable,
			int id, Object data) {
		if (data != null) {
			((TextView) viewFindable.findViewById(id)).setText(toString(
					context, data));
		}
	}

	private static String toString(Context context, Object data) {
		if (data instanceof Enum) {
			return ResourcesUtils.getEnumShowName(context, (Enum<?>) data);
		}
		return data.toString();
	}
}
