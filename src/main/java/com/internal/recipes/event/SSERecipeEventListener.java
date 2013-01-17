package com.internal.recipes.event;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

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
	private HashMap<String, PrintWriter> subscribers = new HashMap<String, PrintWriter>();
	private int logCount = 0;

	
	public void subscribe(String key, PrintWriter pw) {
		if (! subscribers.containsKey(key))
			subscribers.put(key, pw);
		if (++logCount > 100) {
			logCount = 0;
			logger.info ("subscribe, Currently there are " + subscribers.size() + " subscribers listening");
		}
	}
	public void unsubscribe(String key) {
		subscribers.remove(key);
	}
		
	public void onApplicationEvent(RecipeManagerEvent event)  {		
		logger.info ("onApplicationEvent, Currently there are " + subscribers.size() + " subscribers listening");
		
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
								
				//returnJson = "data: " + "{" + "\"eventType\"" + ":" + "\"" + event.getEventLog().getLogType() + "\"" + "," + "\"eventData\"" + ":" + recipeJson + "}" + "\n\n";	
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
			PrintWriter pw = subscribers.get(key);
			pw.write(returnJson);
		}
		
		logger.info(returnJson);
	}
}