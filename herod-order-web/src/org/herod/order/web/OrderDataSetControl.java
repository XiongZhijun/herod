/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.order.web;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
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
	@Autowired
	private UserService userService;
	private List<String> agentManagerDataSet = DEFAULT_AGENT_MANAGE_DATA_SET;

	@Override
	public DataStore<?> search(String contextLocation, String dataSetCode,
			List<String> selectFields, ConditionDefinition conditionDef,
			SimpleOrders orders, int begin, int end) {
		ConditionDefinition newConditionDefinition = new ConditionDefinition();
		if (agentManagerDataSet.contains(dataSetCode.toUpperCase())) {
			ConditionDefinition agentIdConditionDef = createAgentIdConditionDef();
			if (conditionDef == null
					|| (conditionDef.getOp().equalsIgnoreCase("and") && CollectionUtils
							.isEmpty(conditionDef.getConditions()))) {
				newConditionDefinition = agentIdConditionDef;
			} else {
				newConditionDefinition.setOp("and");
				newConditionDefinition.addSubCondition(agentIdConditionDef);
				newConditionDefinition.addSubCondition(conditionDef);
			}
		}
		try {
		return super.search(contextLocation, dataSetCode, selectFields,
				newConditionDefinition, orders, begin, end);
		} catch(RuntimeException e) {
	e.printStackTrace();
			throw e;
		}
	}

	private ConditionDefinition createAgentIdConditionDef() {
		ConditionDefinition newConditionDefinition = new ConditionDefinition();
		newConditionDefinition.setPropertyName("AGENT_ID");
		newConditionDefinition.setOp("=");
		newConditionDefinition.setValue(new String[] { userService
				.getCurrentAgentId() + "" });
		return newConditionDefinition;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public void setAgentManagerDataSet(List<String> agentManagerDataSet) {
		this.agentManagerDataSet = agentManagerDataSet;
	}

}
