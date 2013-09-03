/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.order.common;

import java.util.List;

import org.herod.framework.MapWrapper;
import org.herod.framework.adapter.SimpleAdapter;
import org.herod.framework.rest.URLBuilder;

import android.content.Context;
import android.widget.ImageView;

import com.nostra13.universalimageloader.utils.ImageLoaderUtils;
import static org.herod.order.common.Constants.*;

/**
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 * 
 */
public class ImageLoaderAdapter extends SimpleAdapter {
	private URLBuilder urlBuilder;

	public ImageLoaderAdapter(Context context,
			List<MapWrapper<String, Object>> data, int resource, String[] from,
			int[] to) {
		super(context, data, resource, from, to);
		urlBuilder = new ImageUrlBuilder();
	}

	public void setViewImage(ImageView v, String value) {
		ImageLoaderUtils.loadImage(v, urlBuilder.build(value));
	}

	public void addData(List<MapWrapper<String, Object>> data) {
		for (MapWrapper<String, Object> existRecord : this.mData) {
			for (MapWrapper<String, Object> record : data) {
				if (existRecord.containsKey(ID) && existRecord.get(ID) != null
						&& existRecord.get(ID).equals(record.get(ID))) {
					data.remove(record);
					break;
				}
			}
		}
		this.mData.addAll(data);
		notifyDataSetChanged();
	}
}
