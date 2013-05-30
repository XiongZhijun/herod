/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.buyer.phone.db;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.herod.framework.db.DatabaseAccessSupport;
import org.herod.framework.db.DatabaseUtils;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Address;

/**
 * 
 * 
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 * 
 */
public class AddressDao extends DatabaseAccessSupport {
	private static final String ADDRESS = "ADDRESS";

	public AddressDao(SQLiteOpenHelper openHelper) {
		super(openHelper);
	}

	public void addAddress(Address address) {
		executeWrite(new NewAddressWriter(address));
	}

	public List<Address> getAllAddresses() {
		return executeRead(new AllAddressReader());
	}

	protected String getNow() {
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		return dateFormat.format(new Date());
	}

	class NewAddressWriter implements Writer {
		private Address address;

		public NewAddressWriter(Address address) {
			super();
			this.address = address;
		}

		public void write(SQLiteDatabase db) {
			List<String> columns = getAllColumnsByDatabase(db, ADDRESS);
			ContentValues values = DatabaseUtils.toContentValues(address,
					columns);
			values.put("SAVE_TIME", getNow());
			db.insert(ADDRESS, null, values);
		}
	}

	class AllAddressReader implements Reader<List<Address>> {
		@Override
		public List<Address> read(SQLiteDatabase db) {
			Cursor cursor = db.query(ADDRESS, null, null, null, null, null,
					"SAVE_TIME");
			return DatabaseUtils.toList(cursor, -1, -1, Address.class);
		}

	}
}
