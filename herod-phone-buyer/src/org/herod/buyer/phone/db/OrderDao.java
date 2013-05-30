/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.buyer.phone.db;

import java.util.ArrayList;
import java.util.List;

import org.herod.buyer.phone.model.Order;
import org.herod.buyer.phone.model.OrderItem;
import org.herod.framework.db.DatabaseAccessSupport;
import org.herod.framework.db.DatabaseUtils;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 
 * 
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 * 
 */
public class OrderDao extends DatabaseAccessSupport {

	public OrderDao(SQLiteOpenHelper openHelper) {
		super(openHelper);
	}

	public void addOrders(List<Order> orders) {
		executeWrite(new NewOrdersWriter(orders));
	}

	public void deleteAllOrders() {

	}

	class NewOrdersWriter implements Writer {
		private List<Order> orders;

		public NewOrdersWriter(List<Order> orders) {
			super();
			this.orders = orders;
		}

		public void write(SQLiteDatabase db) {
			insertOrders(db, orders);
			List<OrderItem> orderItems = new ArrayList<OrderItem>();
			for (Order order : orders) {
				orderItems.addAll(order.getOrderItems());
			}
			insertOrderItems(db, orderItems);
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

}
