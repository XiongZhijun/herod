/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.order.web;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.herod.order.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import com.fpi.bear.dataset.control.DataSetControl;
import com.fpi.bear.dataset.spi.DataStore;
import com.fpi.bear.sql.SimpleOrders;
import com.fpi.bear.sql.condition.ConditionDefinition;

/**
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 * 
 */
public class OrderDataSetControl extends DataSetControl {
	private static final List<String> DEFAULT_AGENT_MANAGE_DATA_SET = Arrays
			.asList("ZRH_SHOP", "ZRH_GOODS_CATEGORY", "ZRH_GOODS",
					"ZRH_AGENT_DELIVERY_WORKER", "SHOP_VIEW",
					"AGENT_ORDER_MANGER_LIST_VIEW");
	private static final List<String> ADD_DELETE_FLAG_DATA_SET = Arrays.asList(
			"ZRH_AGENT", "ZRH_SHOP", "ZRH_GOODS_CATEGORY", "ZRH_GOODS",
			"ZRH_AGENT_DELIVERY_WORKER", "ZRH_ORDER", "ZRH_ORDER_ITEM",
			"ZRH_ORDER_LOG", "ZRH_SHOP_TYPE");
	@Autowired
	private UserService userService;
	private List<String> agentManagerDataSet = DEFAULT_AGENT_MANAGE_DATA_SET;

	@Override
	public DataStore<?> search(String contextLocation, String dataSetCode,
			List<String> selectFields, ConditionDefinition conditionDef,
			SimpleOrders orders, int begin, int end) {
		ConditionDefinition newConditionDefinition = buildCondition(
				dataSetCode, conditionDef);
		try {
			return super.search(contextLocation, dataSetCode, selectFields,
					newConditionDefinition, orders, begin, end);
		} catch (RuntimeException e) {
			e.printStackTrace();
			throw e;
		}
	}

	private ConditionDefinition buildCondition(String dataSetCode,
			ConditionDefinition conditionDef) {
		List<ConditionDefinition> conditionDefinitions = new ArrayList<ConditionDefinition>();
		if (ADD_DELETE_FLAG_DATA_SET.contains(dataSetCode.toUpperCase())) {
			conditionDefinitions.add(createDeleteFlageConditionDef());
		}
		if (agentManagerDataSet.contains(dataSetCode.toUpperCase())) {
			conditionDefinitions.add(createAgentIdConditionDef());
		}

		if (conditionDef != null
				&& !(conditionDef.getOp().equalsIgnoreCase("and") && CollectionUtils
						.isEmpty(conditionDef.getConditions()))) {
			conditionDefinitions.add(conditionDef);
		}
		if (conditionDefinitions.size() == 0) {
			return null;
		}
		if (conditionDefinitions.size() == 1) {
			return conditionDefinitions.get(0);
		}
		ConditionDefinition newCondition = new ConditionDefinition();
		newCondition.setOp("and");
		newCondition.setConditions(conditionDefinitions);
		return newCondition;
	}

	private ConditionDefinition createDeleteFlageConditionDef() {
		ConditionDefinition cd = new ConditionDefinition();
		cd.setPropertyName("DELETE_FLAG");
		cd.setOp("=");
		cd.setValue(new String[] { "0" });
		return cd;
	}

	private ConditionDefinition createAgentIdConditionDef() {
		ConditionDefinition cd = new ConditionDefinition();
		cd.setPropertyName("AGENT_ID");
		cd.setOp("=");
		cd.setValue(new String[] { userService.getCurrentAgentId() + "" });
		return cd;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public void setAgentManagerDataSet(List<String> agentManagerDataSet) {
		this.agentManagerDataSet = agentManagerDataSet;
	}

}
