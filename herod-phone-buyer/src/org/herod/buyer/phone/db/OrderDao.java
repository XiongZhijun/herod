/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.buyer.phone.db;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.herod.framework.db.DatabaseAccessSupport;
import org.herod.framework.db.DatabaseUtils;
import org.herod.order.common.model.Order;
import org.herod.order.common.model.OrderItem;
import org.herod.order.common.model.OrderStatus;
import org.springframework.util.CollectionUtils;

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

	public void addOrders(Collection<Order> orders) {
		executeWrite(new NewOrdersWriter(orders));
	}

	public void deleteAllOrders() {
		executeWrite(new RemoveAllOrdersWriter());
	}

	public void deleteOrders(Set<String> serialNumbers) {
		if (CollectionUtils.isEmpty(serialNumbers)) {
			return;
		}
		executeWrite(new RemoveOrdersWithSerialNumberWriter(serialNumbers));
	}

	public List<Order> getAllOrders(OrderStatus... statuses) {
		List<Order> orders = executeRead(new QueryAllOrdersReader(statuses));
		Collections.sort(orders, new OrderComparator());
		return orders;
	}

	public Set<String> findAllUncompleteOrders() {
		return executeRead(new QueryAllUncompleteOrdersReader());
	}

	class NewOrdersWriter implements Writer {
		private Collection<Order> orders;

		public NewOrdersWriter(Collection<Order> orders) {
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

	protected void insertOrders(SQLiteDatabase db, Collection<Order> orders) {
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

	protected class RemoveOrdersWithSerialNumberWriter implements Writer {
		private Set<String> serialNumbers;

		public RemoveOrdersWithSerialNumberWriter(Set<String> serialNumbers) {
			super();
			this.serialNumbers = serialNumbers;
		}

		public void write(SQLiteDatabase db) {
			StringBuilder deleteOrderSelection = new StringBuilder();
			StringBuilder deleteOrderItemSelection = new StringBuilder();
			for (String sn : serialNumbers) {
				if (deleteOrderItemSelection.length() > 0) {
					deleteOrderItemSelection.append(" OR ");
				}
				deleteOrderItemSelection.append(" ORDER_SERIAL_NUMBER = '")
						.append(sn).append("'");
				if (deleteOrderSelection.length() > 0) {
					deleteOrderSelection.append(" OR ");
				}
				deleteOrderSelection.append(" SERIAL_NUMBER = '").append(sn)
						.append("'");
			}
			db.delete(ORDER_ITEMS, deleteOrderItemSelection.toString(), null);
			db.delete(ORDERS, deleteOrderSelection.toString(), null);
		}
	}

	class QueryAllUncompleteOrdersReader implements Reader<Set<String>> {
		public Set<String> read(SQLiteDatabase db) {
			Cursor cursor = db
					.query(ORDERS,
							new String[] { "SERIAL_NUMBER" },
							"STATUS = 'Submitted' or STATUS = 'Acceptted' or STATUS = 'Rejected'",
							null, null, null, null);
			Set<String> results = new HashSet<String>();
			while (cursor.moveToNext()) {
				results.add(cursor.getString(0));
			}
			return results;
		}

	}

	class QueryAllOrdersReader implements Reader<List<Order>> {
		private OrderStatus[] statuses;

		public QueryAllOrdersReader(OrderStatus[] statuses) {
			this.statuses = statuses;
		}

		public List<Order> read(SQLiteDatabase db) {
			StringBuilder selection = new StringBuilder();
			for (OrderStatus status : statuses) {
				if (selection.length() > 0) {
					selection.append(" OR ");
				}
				selection.append(" STATUS = '").append(status.name())
						.append("' ");
			}
			Cursor orderCursor = db.query(ORDERS, null, selection.toString(),
					null, null, null, "SERIAL_NUMBER DESC");
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

	class OrderComparator implements Comparator<Order> {
		public int compare(Order lhs, Order rhs) {
			return lhs.getStatus().getValue() - rhs.getStatus().getValue();
		}

	}

}
