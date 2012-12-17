package com.internal.recipes.domain;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document
public class EventLog {

	@Id
	private String eventId;
	private String person;
	private Date   logDate;
	private String logData;
	
	// create default ctor used for Jackson mapping
	public EventLog() {}
	public EventLog(String person, String logData) {
		this.logDate = new Date(); 
		this.person = person;
		this.logData = logData;
	}
	
	public String getEventLogId() 		{return this.eventId;}
	public String getPerson()			{return this.person;}
	public Date getLogDate()			{return this.logDate;}
	public String getLogData()			{return this.logData;}
	
	public void setPerson(String p)		{this.person = p;}
	public void setLogDate(Date d)		{this.logDate = d;}
	public void setLogData(String ld)	{this.logData = ld;}

}
