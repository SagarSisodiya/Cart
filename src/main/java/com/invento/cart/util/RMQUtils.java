package com.invento.cart.util;

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Component
public class RMQUtils {
	
	private static final String ROUTING_KEY = "routingKey";
	private static final String DEFAULT_ROUTING_VALUE = "#";
	
	private final Logger log = LoggerFactory.getLogger(RMQUtils.class);

	public Message<?> createRMQMsg(Object object) {
		
		Message<?> message = null;
		if(Objects.isNull(object)) {
			log.error("Null object");
		} else{
			message = MessageBuilder
					.withPayload(object)
					.setHeader(ROUTING_KEY, DEFAULT_ROUTING_VALUE)
					.build();
		}
		return message;
	}
}
