/*
 * 香港摩比科技有限公司 版权所有
 *
 * www.mobi-inf.com
 */
package org.herod.common.das;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.NotReadablePropertyException;
import org.springframework.beans.NullValueInNestedPathException;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;

/**
 * @author Xiong Zhijun
 * 
 */
public class HerodBeanPropertySqlParameterSource extends
		BeanPropertySqlParameterSource {

	public HerodBeanPropertySqlParameterSource(Object object) {
		super(object);
	}

	public Object getValue(String paramName) throws IllegalArgumentException {
		try {
			return super.getValue(paramName);
		} catch (NullValueInNestedPathException e) {
			_log.warn("null value in nested path. param name is " + paramName);
			return null;
		} catch (NotReadablePropertyException ex) {
			throw new IllegalArgumentException(ex.getMessage());
		}
	}

	private static Log _log = LogFactory
			.getLog(HerodBeanPropertySqlParameterSource.class);
}
