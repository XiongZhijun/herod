/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.order.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import com.fpi.bear.db.data.FormData;
import com.fpi.bear.db.impl.FormDataAccessServiceImpl;

/**
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 * 
 */
@Transactional
public class OrderFormDataAccessService extends FormDataAccessServiceImpl {

	@Autowired
	@Qualifier("simpleJdbcTemplate")
	private SimpleJdbcTemplate simpleJdbcTemplate;
	@Autowired
	private OrderFormDataPreprocessor orderFormDataPreprocessor;
	@Autowired
	private OrderFormDataPostprocessor orderFormDataPostprocessor;

	private static final String BATCH_UPDATE_DELETE_FLAGS = "UPDATE ${TABLE} SET ${FIELD} = 1 WHERE ID = ?";

	@Override
	public Object add(FormData formData) {
		orderFormDataPreprocessor.preproccess(formData);
		formData.addFieldData("TIMESTAMP", "date", new Date().getTime());
		Object id = super.add(formData);
		orderFormDataPostprocessor.postproccess(formData);
		return id;
	}

	@Override
	public void update(FormData formData) {
		orderFormDataPreprocessor.preproccess(formData);
		formData.addFieldData("TIMESTAMP", "date", new Date().getTime());
		super.update(formData);
		orderFormDataPostprocessor.postproccess(formData);
	}

	public void batchUpdateDeleteFlags(String nodeTypeCode, String deleteFlag,
			long[] ids) {
		List<Object[]> params = new ArrayList<Object[]>();

		for (int i = 0; i < ids.length; i++) {
			params.add(new Object[] { ids[i] });
		}

		String sql = BATCH_UPDATE_DELETE_FLAGS.replaceAll("\\$\\{TABLE\\}",
				nodeTypeCode).replaceAll("\\$\\{FIELD\\}", deleteFlag);

		simpleJdbcTemplate.batchUpdate(sql, params);
	}

	public static interface OrderFormDataPreprocessor {
		void preproccess(FormData formData);
	}

	public static interface OrderFormDataPostprocessor {
		void postproccess(FormData formData);
	}
}