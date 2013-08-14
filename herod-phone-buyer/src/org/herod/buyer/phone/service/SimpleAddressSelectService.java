/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved.
 */
package org.herod.buyer.phone.service;

import java.util.List;

import org.herod.buyer.phone.db.AddressDao;
import org.herod.buyer.phone.db.LocalAddress;
import org.herod.buyer.phone.fragments.SubmitOrderInfoFragment.AddressSelectService;
import org.herod.framework.db.DatabaseOpenHelper;
import org.herod.framework.lbs.Location;
import org.herod.framework.lbs.LocationManager;
import org.herod.framework.utils.GpsTools;
import org.springframework.util.CollectionUtils;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 */
public class SimpleAddressSelectService implements AddressSelectService {

	@Override
	public LocalAddress selectAddress(Context context) {
		List<LocalAddress> allAddresses;
		SQLiteOpenHelper openHelper = new DatabaseOpenHelper(context);
		try {
			AddressDao addressDao = new AddressDao(openHelper);
			allAddresses = addressDao.getAllAddresses();
		} finally {
			if (openHelper != null)
				openHelper.close();
		}
		if (CollectionUtils.isEmpty(allAddresses)) {
			return null;
		}
		LocalAddress defaultAddress = allAddresses.get(0);
		for (int i = 1; i < allAddresses.size(); i++) {
			LocalAddress newAddress = allAddresses.get(i);
			Location currentLocation = LocationManager.getInstance(context)
					.getLatestLocation();
			if (GpsTools.calculateDistance(defaultAddress.getLocation(),
					currentLocation) > GpsTools.calculateDistance(
					newAddress.getLocation(), currentLocation)) {
				defaultAddress = newAddress;
			}
		}
		return defaultAddress;
	}

	@Override
	public void addCurrentAddress(Context context, String buyerName,
			String buyerPhone, String buyerAddress) {
		SQLiteOpenHelper openHelper = new DatabaseOpenHelper(context);
		Location location = LocationManager.getInstance(context)
				.getLatestLocation();
		LocalAddress address = new LocalAddress();
		address.setName(buyerName);
		address.setPhone(buyerPhone);
		address.setAddress(buyerAddress);
		address.setLatitude(location.getLatitude());
		address.setLongitude(location.getLongitude());
		try {
			AddressDao addressDao = new AddressDao(openHelper);
			addressDao.addAddress(address);
		} finally {
			if (openHelper != null)
				openHelper.close();
		}
	}

}
