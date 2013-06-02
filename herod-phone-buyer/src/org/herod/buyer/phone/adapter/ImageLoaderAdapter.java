/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.buyer.phone.adapter;

import java.util.List;

import org.herod.framework.MapWrapper;
import org.herod.framework.adapter.SimpleAdapter;

import android.content.Context;
import android.widget.ImageView;

import com.nostra13.universalimageloader.utils.ImageLoaderUtils;

/**
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 * 
 */
public class ImageLoaderAdapter extends SimpleAdapter {

	public ImageLoaderAdapter(Context context,
			List<MapWrapper<String, Object>> data, int resource, String[] from,
			int[] to) {
		super(context, data, resource, from, to);
	}

	public void setViewImage(ImageView v, String value) {
		ImageLoaderUtils.loadImage(v, value);
	}

}
