/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.buyer.phone.db;

import java.util.Arrays;
import java.util.List;

import org.herod.buyer.phone.model.Order;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 
 * 
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 * 
 */
public class OrderDao {
	private SQLiteOpenHelper openHelper;

	public void addOrders(List<Order> orders) {
		SQLiteDatabase db = openHelper.getWritableDatabase();
		db.beginTransaction();
		try {

			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
		}
	}

	protected void insertOrders(SQLiteDatabase db, List<Order> orders) {
		String table = "ORDERS";
		List<String> columns = getAllColumnsByDatabase(db, table);
		for (Order order : orders) {
			ContentValues values = new ContentValues();
			db.insert(table, null, values);
		}
	}

	private List<String> getAllColumnsByDatabase(SQLiteDatabase db, String table) {
		Cursor cursor = db.query(table, null, null, null, null, null, null);
		return Arrays.asList(cursor.getColumnNames());
	}

}
