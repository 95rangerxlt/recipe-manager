package com.internal.recipes.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.internal.recipes.domain.EmailMessage;
import com.internal.recipes.domain.EmailMessageResponseStatus;
import com.internal.recipes.exception.EmailMessageSendFailureException;
import com.internal.recipes.util.SendGridParameters;

@Service
public class EmailServiceImpl implements EmailService {
	
	protected static Logger logger = Logger.getLogger(EmailService.class);
	private RestTemplate restTemplate = new RestTemplate();
	
	private @Value("${sendGrid.apiUsername}") String apiUsername;
	private @Value("${sendGrid.apiKey}") String apiKey;

	public EmailMessageResponseStatus send(EmailMessage message) throws EmailMessageSendFailureException {
		
		try {
			MultiValueMap<String, Object> vars = new LinkedMultiValueMap<String, Object>();
			vars.add(SendGridParameters.API_USER, this.apiUsername);
			vars.add(SendGridParameters.API_KEY, this.apiKey);
			vars.add(SendGridParameters.SENDER_NAME, message.getSenderName());
			vars.add(SendGridParameters.SENDER_EMAIL, message.getSenderEmail());
			vars.add(SendGridParameters.BLIND_COPY_EMAIL, message.getCcEmail());
			vars.add(SendGridParameters.SUBJECT, message.getSubject());
			vars.add(SendGridParameters.TEXT, "");
			vars.add(SendGridParameters.HTML, message.getBody());
			vars.add(SendGridParameters.RECEIVER_EMAIL, message.getReceiverEmail());
			vars.add(SendGridParameters.RECEIVER_NAME, message.getReceiverName());
			
			restTemplate.postForLocation(SendGridParameters.URL, vars);
			
		} catch (Exception ex) {
			logger.error(ex);
			throw new EmailMessageSendFailureException(ex.getMessage());
		}
		
		return new EmailMessageResponseStatus (true, "Message Sent");
	}

}
