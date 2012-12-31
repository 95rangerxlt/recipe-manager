package com.internal.recipes.repository;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.stereotype.Component;

@Component(value="RememberMeTokenRepository")
public class RememberMeTokenRepository implements PersistentTokenRepository {
	
	@Autowired
	MongoTemplate mongoTemplate;
	
	public void createNewToken(PersistentRememberMeToken t) {
		this.mongoTemplate.insert(t, "rememberMeToken");		
	}

	public PersistentRememberMeToken getTokenForSeries(String seriesId) {
		PersistentRememberMeToken token = this.mongoTemplate.findOne(new Query(Criteria.where("series").is(seriesId)), PersistentRememberMeToken.class, "rememberMeToken");
		return token;
	}

	public void removeUserTokens(String username) {
	       this.mongoTemplate.remove( new Query(Criteria.where("username").is(username)),"rememberMeToken");		
	}

	public void updateToken(String series, String tokenValue, Date lastUsed) {
		Update update = new Update();
		update.set("tokenValue", tokenValue);
		update.set("lastUsed", lastUsed);
		Query query = new Query();
		query.addCriteria(Criteria.where("series").is(series));
		this.mongoTemplate.updateFirst(query, update, "rememberMeToken");		
	}
	
	
}
