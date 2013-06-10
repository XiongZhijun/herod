/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.order.web;

import java.util.Arrays;
import java.util.List;

import org.herod.order.web.OrderFormDataAccessService.OrderFormDataPostprocessor;
import org.herod.order.web.OrderFormDataAccessService.OrderFormDataPreprocessor;
import org.herod.order.web.pms.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import com.fpi.bear.db.data.FormData;

/**
 * 
 * 
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 * 
 */
public class OrderFormDataProcessor implements OrderFormDataPostprocessor,
		OrderFormDataPreprocessor {
	@Autowired
	private UserService userService;

	@Override
	public void preproccess(FormData formData) {
		if (AGENT_MANAGE_FORMS.contains(formData.getNodeTypeCode()
				.toUpperCase())) {
			long currentAgentId = userService.getCurrentAgentId();
			if (currentAgentId <= 0) {
				throw new RuntimeException("当前用户没有代理商管理权限！");
			}
			formData.addFieldData("AGENT_ID", "long", currentAgentId);
		}
	}

	@Override
	public void postproccess(FormData formData) {
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	private static final List<String> AGENT_MANAGE_FORMS = Arrays.asList(
			"ZRH_SHOP", "ZRH_GOODS_CATEGORY", "ZRH_GOODS",
			"ZRH_AGENT_DELIVERY_WORKER");
}
