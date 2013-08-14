/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved.
 */
package org.herod.worker.phone.lbs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.herod.framework.lbs.Location;
import org.herod.framework.lbs.LocationManager;
import org.herod.order.common.model.Address;
import org.springframework.util.CollectionUtils;

import android.content.Context;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.search.MKPlanNode;
import com.baidu.mapapi.search.MKSearch;
import com.baidu.mapapi.search.MKSearchListener;
import com.baidu.mapapi.search.MKWpNode;

/**
 * 
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 */
public class MKSearchHelper {

	private BMapManager mapManager;

	public MKSearchHelper(BMapManager mapManager) {
		super();
		this.mapManager = mapManager;
	}

	public void doSearchFromCurrentLocation(Context context,
			MKSearchListener listener, Address endAddress,
			List<Address> wpAddresses) {
		Location currentLocation = LocationManager.getInstance(context)
				.getLatestLocation();
		MKSearch mMKSearch = new MKSearch();
		mMKSearch.init(mapManager, listener);
		MKPlanNode start = LocationUtils.createMKPlanNode(currentLocation);
		MKPlanNode end = LocationUtils.createMKPlanNode(endAddress);
		mMKSearch.setDrivingPolicy(MKSearch.ECAR_DIS_FIRST);
		mMKSearch.setTransitPolicy(MKSearch.EBUS_TIME_FIRST);
		List<MKWpNode> wpNodes = createWpNodes(wpAddresses);
		mMKSearch.drivingSearch(null, start, null, end, wpNodes);
	}

	private List<MKWpNode> createWpNodes(List<Address> wpAddresses) {
		if (CollectionUtils.isEmpty(wpAddresses)) {
			return Collections.emptyList();
		}
		List<MKWpNode> nodes = new ArrayList<MKWpNode>();
		for (Address address : wpAddresses) {
			MKWpNode node = LocationUtils.createMKWpNode(address);
			nodes.add(node);
		}
		return nodes;
	}
}
