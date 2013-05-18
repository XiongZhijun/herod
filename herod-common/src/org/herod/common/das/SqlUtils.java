/*
 * 香港摩比科技有限公司 版权所有
 *
 * www.mobi-inf.com
 */
package org.herod.common.das;

import static org.herod.common.das.Constants.COMMA;
import static org.herod.common.das.Constants.POSITIVE_PARENTHESIS;
import static org.herod.common.das.Constants.REVERSE_PARENTHESIS;
import static org.herod.common.das.Constants.SINGLE_QUOTES;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Xiong Zhijun
 * 
 */
public class SqlUtils {
	public static <T> String buildInSql(Collection<T> values) {
		Set<T> items = new HashSet<T>();
		items.addAll(values);
		StringBuilder sb = new StringBuilder();
		sb.append(POSITIVE_PARENTHESIS);
		int i = 0;
		for (T o : items) {
			if (i > 0) {
				sb.append(COMMA);
			}
			sb.append(SINGLE_QUOTES).append(o).append(SINGLE_QUOTES);
			i++;
		}
		sb.append(REVERSE_PARENTHESIS);
		return sb.toString();
	}
}
