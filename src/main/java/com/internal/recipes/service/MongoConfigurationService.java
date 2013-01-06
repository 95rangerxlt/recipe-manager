package com.internal.recipes.service;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.CollectionOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Component;

import com.internal.recipes.domain.EventLog;

@Component
public class MongoConfigurationService implements DbConfigurationService {

	@Autowired
	MongoOperations mongoOperations;
	
	private static final Logger logger = LoggerFactory.getLogger(MongoConfigurationService.class);
	
	@PostConstruct
	public void init() {
		
		logger.info("Verifying mongodb configuration.");
		
		if (!mongoOperations.collectionExists(EventLog.class)) {
			CollectionOptions options = new CollectionOptions(100000, 200, true);
			mongoOperations.createCollection("eventLog", options);
			logger.info("Configured eventLog as a capped collection");
		}
		
	}

}
