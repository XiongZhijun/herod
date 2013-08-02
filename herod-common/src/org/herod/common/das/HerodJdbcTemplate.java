/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved.
 */
package org.herod.common.das;

import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

/**
 * 
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 */
public interface HerodJdbcTemplate {

	int queryForInt(String sql, Map<String, ?> args) throws DataAccessException;

	int queryForInt(String sql, SqlParameterSource args)
			throws DataAccessException;

	int queryForInt(String sql, Object... args) throws DataAccessException;

	long queryForLong(String sql, Map<String, ?> args)
			throws DataAccessException;

	long queryForLong(String sql, SqlParameterSource args)
			throws DataAccessException;

	long queryForLong(String sql, Object... args) throws DataAccessException;

	<T> T queryForObject(String sql, Class<T> requiredType, Map<String, ?> args)
			throws DataAccessException;

	<T> T queryForObject(String sql, Class<T> requiredType,
			SqlParameterSource args) throws DataAccessException;

	<T> T queryForObject(String sql, Class<T> requiredType, Object... args)
			throws DataAccessException;

	<T> T queryForObject(String sql, RowMapper<T> rm, Map<String, ?> args)
			throws DataAccessException;

	<T> T queryForObject(String sql, RowMapper<T> rm, SqlParameterSource args)
			throws DataAccessException;

	<T> T queryForObject(String sql, RowMapper<T> rm, Object... args)
			throws DataAccessException;

	<T> List<T> query(String sql, RowMapper<T> rm, Map<String, ?> args)
			throws DataAccessException;

	<T> List<T> query(String sql, RowMapper<T> rm, SqlParameterSource args)
			throws DataAccessException;

	<T> List<T> query(String sql, RowMapper<T> rm, Object... args)
			throws DataAccessException;

	Map<String, Object> queryForMap(String sql, Map<String, ?> args)
			throws DataAccessException;

	Map<String, Object> queryForMap(String sql, SqlParameterSource args)
			throws DataAccessException;

	Map<String, Object> queryForMap(String sql, Object... args)
			throws DataAccessException;

	List<Map<String, Object>> queryForList(String sql, Map<String, ?> args)
			throws DataAccessException;

	List<Map<String, Object>> queryForList(String sql, SqlParameterSource args)
			throws DataAccessException;

	List<Map<String, Object>> queryForList(String sql, Object... args)
			throws DataAccessException;

	int update(String sql, Map<String, ?> args) throws DataAccessException;

	int update(String sql, SqlParameterSource args) throws DataAccessException;

	int update(String sql, Object... args) throws DataAccessException;

	int[] batchUpdate(String sql, List<Object[]> batchArgs);

	int[] batchUpdate(String sql, List<Object[]> batchArgs, int[] argTypes);

	int[] batchUpdate(String sql, Map<String, ?>[] batchValues);

	int[] batchUpdate(String sql, SqlParameterSource[] batchArgs);

	SimpleJdbcTemplate getSimpleJdbcTemplate();

}