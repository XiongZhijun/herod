/*
 * 香港摩比科技有限公司 版权所有
 *
 * www.mobi-inf.com
 */
package org.herod.common.das;

import java.beans.PropertyDescriptor;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.NotWritablePropertyException;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.JdbcUtils;

/**
 * @author Xiong Zhijun
 * 
 */
public class HerodBeanPropertyRowMapper<T> implements RowMapper<T> {

	private static final String MAPPING_FILE_SUFFIX = ".mapping";
	private Class<T> mappedClass;
	private PropertiesConfiguration configuration = new PropertiesConfiguration();

	public HerodBeanPropertyRowMapper() {
		super();
	}

	public HerodBeanPropertyRowMapper(Class<T> mappedClass) {
		super();
		setMappedClass(mappedClass);
	}

	public Class<T> getMappedClass() {
		return mappedClass;
	}

	public void setMappedClass(Class<T> mappedClass) {
		this.mappedClass = mappedClass;
		InputStream stream = mappedClass.getResourceAsStream(mappedClass
				.getSimpleName() + MAPPING_FILE_SUFFIX);
		if (stream != null) {
			try {
				configuration.load(stream);
			} catch (ConfigurationException e) {
				throw new RuntimeException(e.getMessage(), e);
			}
		}
	}

	@Override
	public T mapRow(ResultSet rs, int rowNum) throws SQLException {
		T mappedObject = BeanUtils.instantiate(this.mappedClass);
		BeanWrapper bw = PropertyAccessorFactory
				.forBeanPropertyAccess(mappedObject);
		ResultSetMetaData rsmd = rs.getMetaData();
		int columnCount = rsmd.getColumnCount();

		for (int index = 1; index <= columnCount; index++) {
			String column = JdbcUtils.lookupColumnName(rsmd, index);
			try {
				String property = getProperty(column);
				Object value = getColumnValue(rs, index,
						bw.getPropertyDescriptor(property));
				bw.setPropertyValue(property, value);
			} catch (NotWritablePropertyException ex) {
				throw new DataRetrievalFailureException("Unable to map column "
						+ column, ex);
			}
		}

		return mappedObject;
	}

	private String getProperty(String column) {
		String property = this.configuration.getString(column);
		if (StringUtils.isBlank(property)) {
			property = column;
		}
		return property;
	}

	protected Object getColumnValue(ResultSet rs, int index,
			PropertyDescriptor pd) throws SQLException {
		return JdbcUtils.getResultSetValue(rs, index, pd.getPropertyType());
	}

}
