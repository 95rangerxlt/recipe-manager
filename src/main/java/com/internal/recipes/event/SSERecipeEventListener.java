package com.internal.recipes.event;

import java.io.IOException;

import javax.servlet.ServletOutputStream;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
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
public class SSERecipeEventListener implements ApplicationListener<RecipeManagerEvent>  {
				
	private final Logger mylogger = LoggerFactory.getLogger(SSERecipeEventListener.class);	
	private ServletOutputStream servletOutputStream;	
	private boolean subscribed = false;
	
	public void subscribe() {
		this.subscribed = true;
	}
	public void unsubscribe() {
		this.subscribed = false;
	}
	
	public void setServletOutputStream(ServletOutputStream sos) {
		this.servletOutputStream = sos;
	}
	
	public void onApplicationEvent(RecipeManagerEvent event)  {
		if (! subscribed)
			return;
		
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
				} catch (JsonGenerationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JsonMappingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
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
				} catch (JsonGenerationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JsonMappingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
								
				returnJson = "event: " + event.getEventLog().getLogType() + "\n" + "data: " + recipeJson + "\n\n";	
				break;
				
			case EVENT_SECURITY:
				EventLog eventLog = event.getEventLog();
				String eventLogJson = "";
				try {
					eventLogJson = om.writeValueAsString(eventLog);
				} catch (JsonGenerationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JsonMappingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				returnJson = "event: " + event.getEventLog().getLogType() + "\n" +  "data: " + eventLogJson + "\n\n";	
				break;
				
			default:
				mylogger.info("SSE: received RecipeManagerEvent of unknown type " + event.getEventLog().getLogType());		
				break;				
				
		}
		
		try {
			servletOutputStream.write(returnJson.getBytes());
			//servletOutputStream.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		mylogger.info(returnJson);
	}
}