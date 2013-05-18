package org.herod.order.service;

import static org.easymock.EasyMock.expect;
import static org.testng.AssertJUnit.assertEquals;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.herod.order.model.OrderStatus;
import org.herod.order.service.SimpleOrderStatusChecker.OrderStatusFinder;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class TestSimpleOrderStatusChecker {
	private SimpleOrderStatusChecker checker;
	private IMocksControl control;
	private OrderStatusFinder orderStatusFinder;

	@BeforeMethod
	public void beforeMethod() {
		checker = new SimpleOrderStatusChecker();
		control = EasyMock.createControl();
		orderStatusFinder = control.createMock(OrderStatusFinder.class);
		checker.setOrderStatusFinder(orderStatusFinder);

	}

	@DataProvider
	public Object[][] canChangeStatusProvider() {
		return new Object[][] {
				{ "123", OrderStatus.Unsubmit, OrderStatus.Submitted, true },
				{ "123", OrderStatus.Unsubmit, OrderStatus.Acceptted, false },
				{ "123", OrderStatus.Unsubmit, OrderStatus.Cancelled, true },
				{ "123", OrderStatus.Submitted, OrderStatus.Acceptted, true },
				{ "123", OrderStatus.Submitted, OrderStatus.Rejected, false },
				{ "123", OrderStatus.Submitted, OrderStatus.Completed, false },
				{ "123", OrderStatus.Submitted, OrderStatus.Cancelled, true },
				{ "123", OrderStatus.Acceptted, OrderStatus.Completed, true },
				{ "123", OrderStatus.Acceptted, OrderStatus.Unsubmit, false },
				{ "123", OrderStatus.Acceptted, OrderStatus.Submitted, false },
				{ "123", OrderStatus.Acceptted, OrderStatus.Cancelled, true },
				{ "123", OrderStatus.Acceptted, OrderStatus.Rejected, true },
				{ "123", OrderStatus.Completed, OrderStatus.Acceptted, false } };
	}

	@Test(dataProvider = "canChangeStatusProvider")
	public void canChangeStatus(String serialNumber, OrderStatus currentStatus,
			OrderStatus destStatus, boolean expected) {
		expect(orderStatusFinder.findOrderStatus(serialNumber)).andReturn(
				currentStatus);
		control.replay();
		assertEquals(expected,
				checker.canChangeStatus(serialNumber, destStatus));
		control.verify();
	}

	@DataProvider
	public Object[][] canUpdateDataProvider() {
		return new Object[][] { { "123", OrderStatus.Unsubmit, true },
				{ "123", OrderStatus.Submitted, true },
				{ "123", OrderStatus.Acceptted, true },
				{ "123", OrderStatus.Completed, false },
				{ "123", OrderStatus.Rejected, true },
				{ "123", OrderStatus.Cancelled, false }, };
	}

	@Test(dataProvider = "canUpdateDataProvider")
	public void canUpdate(String serialNumber, OrderStatus currentStatus,
			Object expected) {
		expect(orderStatusFinder.findOrderStatus(serialNumber)).andReturn(
				currentStatus);
		control.replay();
		assertEquals(expected, checker.canUpdate(serialNumber));
		control.verify();
	}
}
