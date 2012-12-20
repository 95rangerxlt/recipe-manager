package com.internal.recipes.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.internal.recipes.domain.EventType;


@Document
public class EventLog {

	@Id
	private String eventId;
	private String person;
	private EventType logType;
	private Date   logDate;
	private String logData;
	
	// create default ctor used for Jackson mapping
	public EventLog() {}
	public EventLog(String person, EventType logType, String logData) {
		this.logDate = new Date(); 
		this.person = person;
		this.logType = logType;
		this.logData = logData;
	}
	
	public String getEventLogId() 			{return this.eventId;}
	public String getPerson()				{return this.person;}
	public String getLogType()	    		{return this.logType.getType();}
	public Date getLogDate()				{return this.logDate;}
	public String getLogData()				{return this.logData;}
	
	public void setPerson(String p)			{this.person = p;}
	public void setLogType(EventType et)	{this.logType = et;};
	public void setLogDate(Date d)			{this.logDate = d;}
	public void setLogData(String ld)		{this.logData = ld;}

}
