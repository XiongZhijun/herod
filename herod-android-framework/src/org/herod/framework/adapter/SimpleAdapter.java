/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.framework.adapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.herod.framework.MapWrapper;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 * 
 */
public class SimpleAdapter extends BaseAdapter {

	private int[] mTo;
	private String[] mFrom;
	private ViewBinder mViewBinder;

	protected List<MapWrapper<String, Object>> mData = new ArrayList<MapWrapper<String, Object>>();

	private int mResource;
	private int mDropDownResource;
	private LayoutInflater mInflater;

	public SimpleAdapter(Context context,
			List<MapWrapper<String, Object>> data, int resource, String[] from,
			int[] to) {
		mData.addAll(data);
		mResource = mDropDownResource = resource;
		mFrom = from;
		mTo = to;
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public int getCount() {
		return mData.size();
	}

	public Object getItem(int position) {
		return mData.get(position);
	}

	public void updateData(List<MapWrapper<String, Object>> data) {
		this.mData.clear();
		this.mData.addAll(data);
		notifyDataSetChanged();
	}

	public void clear() {
		updateData(Collections.<MapWrapper<String, Object>> emptyList());
	}

	public void addData(List<MapWrapper<String, Object>> data) {
		this.mData.addAll(data);
		notifyDataSetChanged();
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		return createViewFromResource(position, convertView, parent, mResource);
	}

	private View createViewFromResource(int position, View convertView,
			ViewGroup parent, int resource) {
		View v;
		if (convertView == null) {
			v = mInflater.inflate(resource, parent, false);
		} else {
			v = convertView;
		}

		bindView(position, v);

		return v;
	}

	public void setDropDownViewResource(int resource) {
		this.mDropDownResource = resource;
	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		return createViewFromResource(position, convertView, parent,
				mDropDownResource);
	}

	private void bindView(int position, View view) {
		final MapWrapper<String, Object> dataSet = mData.get(position);
		if (dataSet == null) {
			return;
		}

		final ViewBinder binder = mViewBinder;
		final String[] from = mFrom;
		final int[] to = mTo;
		final int count = to.length;

		for (int i = 0; i < count; i++) {
			final View v = view.findViewById(to[i]);
			if (v != null) {
				final Object data = dataSet.get(from[i]);
				String text = data == null ? "" : data.toString();
				if (text == null) {
					text = "";
				}

				boolean bound = false;
				if (binder != null) {
					bound = binder.setViewValue(view, dataSet, v, from[i],
							to[i], position, data, text);
				}

				if (!bound) {
					if (v instanceof Checkable) {
						if (data instanceof Boolean) {
							((Checkable) v).setChecked((Boolean) data);
						} else if (v instanceof TextView) {
							// Note: keep the instanceof TextView check at the
							// bottom of these
							// ifs since a lot of views are TextViews (e.g.
							// CheckBoxes).
							setViewText((TextView) v, text);
						} else {
							throw new IllegalStateException(v.getClass()
									.getName()
									+ " should be bound to a Boolean, not a "
									+ (data == null ? "<unknown type>"
											: data.getClass()));
						}
					} else if (v instanceof TextView) {
						// Note: keep the instanceof TextView check at the
						// bottom of these
						// ifs since a lot of views are TextViews (e.g.
						// CheckBoxes).
						setViewText((TextView) v, text);
					} else if (v instanceof ImageView) {
						if (data instanceof Integer) {
							setViewImage((ImageView) v, (Integer) data);
						} else {
							setViewImage((ImageView) v, text);
						}
					} else {
					}
				}
			}
		}
	}

	public ViewBinder getViewBinder() {
		return mViewBinder;
	}

	public void setViewBinder(ViewBinder viewBinder) {
		mViewBinder = viewBinder;
	}

	public void setViewImage(ImageView v, int value) {
		v.setImageResource(value);
	}

	public void setViewImage(ImageView v, String value) {
		try {
			v.setImageResource(Integer.parseInt(value));
		} catch (NumberFormatException nfe) {
			v.setImageURI(Uri.parse(value));
		}
	}

	public void setViewText(TextView v, String text) {
		v.setText(text);
	}

	public static interface ViewBinder {
		boolean setViewValue(View dataSetView,
				MapWrapper<String, Object> dataSet, View view, String from,
				int to, int position, Object data, String textRepresentation);
	}

}
