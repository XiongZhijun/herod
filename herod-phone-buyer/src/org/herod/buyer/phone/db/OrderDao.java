/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.buyer.phone.db;

import java.util.ArrayList;
import java.util.List;

import org.herod.framework.db.DatabaseAccessSupport;
import org.herod.framework.db.DatabaseUtils;
import org.herod.order.common.model.Order;
import org.herod.order.common.model.OrderItem;

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
public class OrderDao extends DatabaseAccessSupport {

	private static final String ORDERS = "ORDERS";
	private static final String ORDER_ITEMS = "ORDER_ITEMS";

	public OrderDao(SQLiteOpenHelper openHelper) {
		super(openHelper);
	}

	public void addOrders(List<Order> orders) {
		executeWrite(new NewOrdersWriter(orders));
	}

	public void deleteAllOrders() {
		executeWrite(new RemoveAllOrdersWriter());
	}

	public List<Order> getAllOrders() {
		return executeRead(new QueryAllOrdersReader());
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
		List<String> columns = getAllColumnsByDatabase(db, ORDERS);
		for (Order order : orders) {
			ContentValues values = DatabaseUtils
					.toContentValues(order, columns);
			db.insert(ORDERS, null, values);
		}
	}

	protected void insertOrderItems(SQLiteDatabase db,
			List<OrderItem> orderItems) {
		List<String> columns = getAllColumnsByDatabase(db, ORDER_ITEMS);
		for (OrderItem orderItem : orderItems) {
			ContentValues values = DatabaseUtils.toContentValues(orderItem,
					columns);
			db.insert(ORDER_ITEMS, null, values);
		}
	}

	protected class RemoveAllOrdersWriter implements Writer {
		public void write(SQLiteDatabase db) {
			db.delete(ORDER_ITEMS, null, null);
			db.delete(ORDERS, null, null);
		}
	}

	class QueryAllOrdersReader implements Reader<List<Order>> {
		public List<Order> read(SQLiteDatabase db) {
			Cursor orderCursor = db.query(ORDERS, null, null, null, null, null,
					"SERIAL_NUMBER");
			Cursor itemsCursor = db.query(ORDER_ITEMS, null, null, null, null,
					null, null);
			List<Order> orders = DatabaseUtils.toList(orderCursor, -1, -1,
					Order.class);
			List<OrderItem> orderItems = DatabaseUtils.toList(itemsCursor, -1,
					-1, OrderItem.class);
			for (OrderItem item : orderItems) {
				Order order = findOrder(orders, item.getOrderSerialNumber());
				if (order != null) {
					order.addOrderItem(item);
				}
			}
			return orders;
		}

		Order findOrder(List<Order> orders, String serialNumber) {
			if (serialNumber == null) {
				return null;
			}
			for (Order order : orders) {
				if (serialNumber.equals(order.getSerialNumber())) {
					return order;
				}
			}
			return null;
		}

	}

}
