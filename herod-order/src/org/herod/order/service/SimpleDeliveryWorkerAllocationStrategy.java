/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.order.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.herod.common.das.HerodBeanPropertyRowMapper;
import org.herod.order.model.AgentWorker;
import org.herod.order.model.Order;
import org.herod.order.service.SimplePhoneBuyerService.DeliveryWorkerAllocationStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

/**
 * 
 * 
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 * 
 */
public class SimpleDeliveryWorkerAllocationStrategy implements
		DeliveryWorkerAllocationStrategy {
	private static final String QUERY_WORKERS_SQL = "SELECT ID, NAME, PHONE, AGENT_ID FROM"
			+ " (SELECT W.ID,W.NAME,W.AGENT_ID ,W.PHONE, O.ORDER_COUNTS FROM ZRH_AGENT_DELIVERY_WORKER W"
			+ " LEFT JOIN"
			+ " (SELECT DELIVERY_WORKER_ID ,AGENT_ID, COUNT(DELIVERY_WORKER_ID) ORDER_COUNTS"
			+ " FROM ZRH_ORDER WHERE STATUS = 'Submitted' OR STATUS = 'Rejected' GROUP BY DELIVERY_WORKER_ID"
			+ " ) O ON W.ID = O.DELIVERY_WORKER_ID "
			+ " WHERE W.AGENT_ID IN (:agentIds)  ORDER BY ORDER_COUNTS"
			+ " ) R GROUP BY AGENT_ID ";
	@Autowired
	private SimpleJdbcTemplate simpleJdbcTemplate;

	@Override
	public Map<String, AgentWorker> allocate(List<Order> orders) {
		Set<Long> agentIds = findAllAgentIds(orders);
		RowMapper<AgentWorker> rm = new HerodBeanPropertyRowMapper<AgentWorker>(
				AgentWorker.class);
		List<AgentWorker> workers = simpleJdbcTemplate.query(QUERY_WORKERS_SQL,
				rm, Collections.singletonMap("agentIds", agentIds));
		Map<Long, AgentWorker> workerMap = new HashMap<Long, AgentWorker>();
		for (AgentWorker worker : workers) {
			workerMap.put(worker.getAgentId(), worker);
		}
		Map<String, AgentWorker> results = new HashMap<String, AgentWorker>();
		for (Order order : orders) {
			results.put(order.getSerialNumber(),
					workerMap.get(order.getAgentId()));
		}
		return results;
	}

	private Set<Long> findAllAgentIds(List<Order> orders) {
		Set<Long> agentIds = new HashSet<Long>();
		for (Order order : orders) {
			agentIds.add(order.getAgentId());
		}
		return agentIds;
	}

	public void setSimpleJdbcTemplate(SimpleJdbcTemplate simpleJdbcTemplate) {
		this.simpleJdbcTemplate = simpleJdbcTemplate;
	}

}
