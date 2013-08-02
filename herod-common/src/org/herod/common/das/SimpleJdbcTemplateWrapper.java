/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved.
 */
package org.herod.common.das;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchUpdateUtils;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

/**
 * 
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 */
public class SimpleJdbcTemplateWrapper implements HerodJdbcTemplate {
	private SimpleJdbcTemplate simpleJdbcTemplate;

	@Override
	public int queryForInt(String sql, Map<String, ?> args)
			throws DataAccessException {
		return simpleJdbcTemplate.queryForInt(sql, convert(args));
	}

	@Override
	public int queryForInt(String sql, SqlParameterSource args)
			throws DataAccessException {
		return simpleJdbcTemplate.queryForInt(sql, convert(args));
	}

	@Override
	public int queryForInt(String sql, Object... args)
			throws DataAccessException {
		return simpleJdbcTemplate.queryForInt(sql, convert(args));
	}

	@Override
	public long queryForLong(String sql, Map<String, ?> args)
			throws DataAccessException {
		return simpleJdbcTemplate.queryForLong(sql, convert(args));
	}

	@Override
	public long queryForLong(String sql, SqlParameterSource args)
			throws DataAccessException {
		return simpleJdbcTemplate.queryForLong(sql, convert(args));
	}

	@Override
	public long queryForLong(String sql, Object... args)
			throws DataAccessException {
		return simpleJdbcTemplate.queryForLong(sql, convert(args));
	}

	@Override
	public <T> T queryForObject(String sql, Class<T> requiredType,
			Map<String, ?> args) throws DataAccessException {
		return simpleJdbcTemplate.queryForObject(sql, requiredType,
				convert(args));
	}

	@Override
	public <T> T queryForObject(String sql, Class<T> requiredType,
			SqlParameterSource args) throws DataAccessException {
		return simpleJdbcTemplate.queryForObject(sql, requiredType,
				convert(args));
	}

	@Override
	public <T> T queryForObject(String sql, Class<T> requiredType,
			Object... args) throws DataAccessException {
		return simpleJdbcTemplate.queryForObject(sql, requiredType,
				convert(args));
	}

	@Override
	public <T> T queryForObject(String sql, RowMapper<T> rm, Map<String, ?> args)
			throws DataAccessException {
		return queryForObject(sql, rm, convert(args));
	}

	@Override
	public <T> T queryForObject(String sql, RowMapper<T> rm,
			SqlParameterSource args) throws DataAccessException {
		return queryForObject(sql, rm, convert(args));
	}

	@Override
	public <T> T queryForObject(String sql, RowMapper<T> rm, Object... args)
			throws DataAccessException {
		return queryForObject(sql, rm, convert(args));
	}

	@Override
	public <T> List<T> query(String sql, RowMapper<T> rm, Map<String, ?> args)
			throws DataAccessException {
		return simpleJdbcTemplate.query(sql, rm, convert(args));
	}

	@Override
	public <T> List<T> query(String sql, RowMapper<T> rm,
			SqlParameterSource args) throws DataAccessException {
		return simpleJdbcTemplate.query(sql, rm, convert(args));
	}

	@Override
	public <T> List<T> query(String sql, RowMapper<T> rm, Object... args)
			throws DataAccessException {
		return simpleJdbcTemplate.query(sql, rm, convert(args));
	}

	@Override
	public Map<String, Object> queryForMap(String sql, Map<String, ?> args)
			throws DataAccessException {
		return simpleJdbcTemplate.queryForMap(sql, convert(args));
	}

	@Override
	public Map<String, Object> queryForMap(String sql, SqlParameterSource args)
			throws DataAccessException {
		return simpleJdbcTemplate.queryForMap(sql, convert(args));
	}

	@Override
	public Map<String, Object> queryForMap(String sql, Object... args)
			throws DataAccessException {
		return simpleJdbcTemplate.queryForMap(sql, convert(args));
	}

	@Override
	public List<Map<String, Object>> queryForList(String sql,
			Map<String, ?> args) throws DataAccessException {
		return simpleJdbcTemplate.queryForList(sql, convert(args));
	}

	@Override
	public List<Map<String, Object>> queryForList(String sql,
			SqlParameterSource args) throws DataAccessException {
		return simpleJdbcTemplate.queryForList(sql, convert(args));
	}

	@Override
	public List<Map<String, Object>> queryForList(String sql, Object... args)
			throws DataAccessException {
		return simpleJdbcTemplate.queryForList(sql, convert(args));
	}

	@Override
	public int update(String sql, Map<String, ?> args)
			throws DataAccessException {
		return simpleJdbcTemplate.update(sql, convert(args));
	}

	@Override
	public int update(String sql, SqlParameterSource args)
			throws DataAccessException {
		return simpleJdbcTemplate.update(sql, convert(args));
	}

	@Override
	public int update(String sql, Object... args) throws DataAccessException {
		return simpleJdbcTemplate.update(sql, convert(args));
	}

	@Override
	public int[] batchUpdate(String sql, List<Object[]> batchArgs) {
		return batchUpdate(sql, convert(batchArgs), new int[0]);
	}

	@Override
	public int[] batchUpdate(String sql, List<Object[]> batchArgs,
			int[] argTypes) {
		return BatchUpdateUtils.executeBatchUpdate(sql, convert(batchArgs),
				argTypes, simpleJdbcTemplate.getJdbcOperations());
	}

	@Override
	public int[] batchUpdate(String sql, Map<String, ?>[] batchValues) {
		return simpleJdbcTemplate.getNamedParameterJdbcOperations()
				.batchUpdate(sql, convert(batchValues));
	}

	@Override
	public int[] batchUpdate(String sql, SqlParameterSource[] batchArgs) {
		return simpleJdbcTemplate.getNamedParameterJdbcOperations()
				.batchUpdate(sql, convert(batchArgs));
	}

	@SuppressWarnings("unchecked")
	protected static Map<String, ?>[] convert(Map<String, ?>[] batchValues) {
		Map<String, ?>[] results = new Map[batchValues.length];
		for (int i = 0; i < batchValues.length; i++) {
			results[i] = convert(batchValues[i]);
		}
		return results;
	}

	protected static List<Object[]> convert(List<Object[]> batchArgs) {
		List<Object[]> results = new ArrayList<Object[]>();
		for (Object[] args : batchArgs) {
			results.add(convert(args));
		}
		return results;
	}

	protected static Map<String, ?> convert(Map<String, ?> args) {
		Map<String, Object> results = new HashMap<String, Object>();
		for (Entry<String, ?> entry : args.entrySet()) {
			Object value = entry.getValue();
			if (value != null && value instanceof Enum) {
				results.put(entry.getKey(), ((Enum<?>) entry.getValue()).name());
			}
		}
		return results;
	}

	protected static Object[] convert(Object[] args) {
		Object[] results = new Object[args.length];
		for (int i = 0; i < args.length; i++) {
			Object arg = args[i];
			if (arg != null && arg instanceof Enum) {
				results[i] = ((Enum<?>) arg).name();
			} else {
				results[i] = arg;
			}
		}
		return args;
	}

	protected static SqlParameterSource[] convert(SqlParameterSource[] args) {
		SqlParameterSource[] results = new SqlParameterSource[args.length];
		for (int i = 0; i < args.length; i++) {
			results[i] = convert(args[i]);
		}
		return results;
	}

	protected static SqlParameterSource convert(final SqlParameterSource args) {
		return new SqlParameterSource() {
			@Override
			public boolean hasValue(String paramName) {
				return args.hasValue(paramName);
			}

			@Override
			public Object getValue(String paramName)
					throws IllegalArgumentException {
				Object value = args.getValue(paramName);
				if (value != null && value instanceof Enum) {
					return ((Enum<?>) value).name();
				}
				return value;
			}

			@Override
			public String getTypeName(String paramName) {
				return args.getTypeName(paramName);
			}

			@Override
			public int getSqlType(String paramName) {
				return args.getSqlType(paramName);
			}
		};
	}

	@Override
	public SimpleJdbcTemplate getSimpleJdbcTemplate() {
		return simpleJdbcTemplate;
	}

	public void setSimpleJdbcTemplate(SimpleJdbcTemplate simpleJdbcTemplate) {
		this.simpleJdbcTemplate = simpleJdbcTemplate;
	}

}
