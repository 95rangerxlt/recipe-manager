package com.internal.recipes.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.internal.recipes.domain.EventLog;
import com.internal.recipes.repository.EventLogRepository;

@Service
public class EventLogService {
	
	@Autowired
	EventLogRepository eventLogRepository;
	
	public List<EventLog> getAllEventLogs() {
		return eventLogRepository.findAll();
	}

	public List<EventLog> getAllRecentEventLogs() {
		Pageable p = new PageRequest(0,100, new Sort(Direction.DESC, "logDate"));
		return eventLogRepository.findAll(p).getContent();
	}

	public EventLog create(EventLog eventLog) {
		return eventLogRepository.save(eventLog);
	}
	
	public Boolean delete(EventLog eventLog) {
		if (!eventLogRepository.exists(eventLog.getEventLogId())) {
			return false;
		}
		
		eventLogRepository.delete(eventLog);
		return true;
	}
	
	public EventLog update(EventLog eventLog) {
		if (!eventLogRepository.exists(eventLog.getEventLogId())) {
			throw new EventLogDoesNotExistException(eventLog.getEventLogId());
		}
		return eventLogRepository.save(eventLog);
	}
	
	public EventLog get(String id) {
		if (! eventLogRepository.exists(id)) {
			throw new EventLogDoesNotExistException(id);
		}
		return eventLogRepository.findOne(id);
	}
}
