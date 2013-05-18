/*
 * 香港摩比科技有限公司 版权所有
 *
 * www.mobi-inf.com
 */
package org.herod.common.das;

import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.util.NumberUtils;

/**
 * @author Xiong Zhijun
 * 
 */
public class HerodSingleColumnRowMapper<T> extends SingleColumnRowMapper<T> {

	public HerodSingleColumnRowMapper() {
		super();
	}

	public HerodSingleColumnRowMapper(Class<T> requiredType) {
		super(requiredType);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected Object convertValueToRequiredType(Object value, Class requiredType) {
		if (String.class.equals(requiredType)) {
			return value.toString();
		} else if (Enum.class.isAssignableFrom(requiredType)) {
			return Enum.valueOf(requiredType, value.toString());
		} else if (Number.class.isAssignableFrom(requiredType)) {
			if (value instanceof Number) {
				// Convert original Number to target Number class.
				return NumberUtils.convertNumberToTargetClass(((Number) value),
						requiredType);
			} else {
				// Convert stringified value to target Number class.
				return NumberUtils.parseNumber(value.toString(), requiredType);
			}
		} else {
			throw new IllegalArgumentException("Value [" + value
					+ "] is of type [" + value.getClass().getName()
					+ "] and cannot be converted to required type ["
					+ requiredType.getName() + "]");
		}
	}
}
