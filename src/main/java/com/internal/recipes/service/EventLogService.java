package com.internal.recipes.service;

import java.util.List;

import com.internal.recipes.domain.EventLog;

public interface EventLogService {
	
	List<EventLog> getAllEventLogs();
	
	List<EventLog> getAllRecentEventLogs();

}
