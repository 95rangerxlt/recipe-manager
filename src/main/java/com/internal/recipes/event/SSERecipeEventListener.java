package com.internal.recipes.event;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletOutputStream;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.internal.recipes.domain.EventLog;
import com.internal.recipes.domain.Recipe;
import com.internal.recipes.domain.RecipeManagerEvent;
import com.internal.recipes.domain.User;

@Component
public class SSERecipeEventListener  implements ApplicationListener<RecipeManagerEvent>   {
				
	private final Logger logger = LoggerFactory.getLogger(SSERecipeEventListener.class);	
	HashMap<String, ServletOutputStream> subscribers = new HashMap<String, ServletOutputStream>();

	
	public void subscribe(String key, ServletOutputStream sos) {
		if (! subscribers.containsKey(key))
			subscribers.put(key, sos);
		//logger.info("subscribe, count: " + subscribers.size());
		//logger.info("Map:" + subscribers.toString());
	}
	public void unsubscribe(String key) {
		subscribers.remove(key);
	}
		
	public void onApplicationEvent(RecipeManagerEvent event)  {		
		ObjectMapper om = new ObjectMapper();
		String returnJson = "";

		switch (event.getEventLog().getEventType()) {
			case EVENT_USER_ADMINISTATION:
			case EVENT_USER_CREATED:
			case EVENT_USER_MODIFIED:
			case EVENT_USER_DELETED:
				User user = (User)event.getSource();
				String userJson = "";
				try {
					userJson = om.writeValueAsString(user);
				} catch (Exception e) {
					e.printStackTrace();
					returnJson = "error: " + e.getMessage();
				}
				
				returnJson = "event: " + event.getEventLog().getLogType() + "\n" + "data: " + userJson + "\n\n";	
				break;

			case EVENT_RECIPE_ADMINISTRATION:
			case EVENT_RECIPE_CREATED:
			case EVENT_RECIPE_DELETED:
			case EVENT_RECIPE_MODIFIED:
				Recipe recipe = (Recipe)event.getSource();
				String recipeJson = "";
				try {
					recipeJson = om.writeValueAsString(recipe);
				} catch (Exception e) {
					e.printStackTrace();
					returnJson = "error: " + e.getMessage();
				}
								
				returnJson = "event: " + event.getEventLog().getLogType() + "\n" + "data: " + recipeJson + "\n\n";	
				break;
				
			case EVENT_SECURITY:
				EventLog eventLog = event.getEventLog();
				String eventLogJson = "";
				try {
					eventLogJson = om.writeValueAsString(eventLog);
				} catch (Exception e) {
					e.printStackTrace();
					returnJson = "error: " + e.getMessage();
				}
				
				returnJson = "event: " + event.getEventLog().getLogType() + "\n" +  "data: " + eventLogJson + "\n\n";	
				break;
				
			default:
				logger.info("SSE: received RecipeManagerEvent of unknown type " + event.getEventLog().getLogType());		
				break;				
				
		}
		
		for (String key :  subscribers.keySet()) {
			logger.info("SSERecipeEventListener: writing out to servlet output stream with key " + key);
			try {
				subscribers.get(key).write(returnJson.getBytes());
			} catch (IOException e) {
				logger.info("SSERecipeEventListener: error writing to output stream for client " + key);
			}
		}
		
		logger.info(returnJson);
	}
}