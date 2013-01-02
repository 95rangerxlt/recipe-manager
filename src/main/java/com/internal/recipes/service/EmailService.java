package com.internal.recipes.service;

import com.internal.recipes.domain.EmailMessage;
import com.internal.recipes.domain.EmailMessageResponseStatus;

public interface EmailService {
	EmailMessageResponseStatus send(EmailMessage message);
}
