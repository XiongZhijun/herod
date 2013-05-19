/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.buyer.phone;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;

/**
 * 
 * 
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 * 
 */
public class ShopListFragment extends Fragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_shop_list, container,
				false);
		GridView shopsGridView = (GridView) view
				.findViewById(R.id.shopsGridView);
		List<Map<String, Object>> data = createDatas();
		ListAdapter adapter = new SimpleAdapter(getActivity(), data,
				R.layout.fragment_shop_list_shop_item, new String[] { "name" },
				new int[] { R.id.name });
		shopsGridView.setAdapter(adapter);
		return view;
	}

	/**
	 * @return
	 */
	private List<Map<String, Object>> createDatas() {
		List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
		data.add(createItem("肯德基"));
		data.add(createItem("麦当劳"));
		data.add(createItem("外婆家"));
		data.add(createItem("外婆家"));
		data.add(createItem("外婆家"));
		data.add(createItem("外婆家"));
		data.add(createItem("外婆家"));
		return data;
	}

	/**
	 * @param name
	 *            TODO
	 * @return
	 */
	private HashMap<String, Object> createItem(String name) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("name", name);
		return map;
	}

}
