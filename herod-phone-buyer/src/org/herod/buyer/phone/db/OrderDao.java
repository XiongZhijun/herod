/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.buyer.phone.db;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.herod.buyer.phone.model.Order;
import org.herod.buyer.phone.model.OrderItem;
import org.herod.framework.db.DatabaseUtils;

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
			insertOrders(db, orders);
			List<OrderItem> orderItems = new ArrayList<OrderItem>();
			for (Order order : orders) {
				orderItems.addAll(order.getOrderItems());
			}
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
		}
	}

	protected void insertOrders(SQLiteDatabase db, List<Order> orders) {
		String table = "ORDERS";
		List<String> columns = getAllColumnsByDatabase(db, table);
		for (Order order : orders) {
			ContentValues values = DatabaseUtils
					.toContentValues(order, columns);
			db.insert(table, null, values);
		}
	}

	protected void insertOrderItems(SQLiteDatabase db,
			List<OrderItem> orderItems) {
		String table = "ORDER_ITEMS";
		List<String> columns = getAllColumnsByDatabase(db, table);
		for (OrderItem orderItem : orderItems) {
			ContentValues values = DatabaseUtils.toContentValues(orderItem,
					columns);
			db.insert(table, null, values);
		}
	}

	public void deleteAllOrders() {

	}

	private List<String> getAllColumnsByDatabase(SQLiteDatabase db, String table) {
		Cursor cursor = db.query(table, null, null, null, null, null, null);
		return Arrays.asList(cursor.getColumnNames());
	}

}
