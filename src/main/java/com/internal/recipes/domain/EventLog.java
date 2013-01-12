package com.internal.recipes.domain;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class EventLog {

	@Id
	private String eventId;
	private String actor;
	private EventType logType;
	private Date logDate;
	private String logData;

	// create default ctor used for Jackson mapping
	public EventLog() {
	}

	public EventLog(EventType logType, String logData) {
		this.logType = logType;
		this.logData = logData;
	}

	public String getActor() {
		return actor;
	}

	public void setActor(String actor) {
		this.actor = actor;
	}

	public String getEventLogId() {
		return this.eventId;
	}

	public String getLogType() {
		return this.logType.getType();
	}
	public EventType getEventType() {
		return this.logType;
	}

	public Date getLogDate() {
		return this.logDate;
	}

	public String getLogData() {
		return this.logData;
	}

	public void setLogType(EventType eventType) {
		this.logType = eventType;
	};

	public void setLogDate(Date date) {
		this.logDate = date;
	}

	public void setLogData(String logData) {
		this.logData = logData;
	}

}
