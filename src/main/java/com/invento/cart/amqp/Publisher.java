package com.invento.cart.amqp;

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Component
public class Publisher {

	private StreamBridge streamBridge;
	
	private final Logger log = LoggerFactory.getLogger(Publisher.class);
	
	Publisher(StreamBridge streamBridge) {
		this.streamBridge = streamBridge;
	}
	
	public void sendMessage(String topic, Message<?> message) {
		try {
			if(Objects.isNull(message)) {
				throw new Exception("Message is null.");
			}
			streamBridge.send(topic, message);
			log.info("Published message: {} to topic: {}", message, topic);
		} catch(Exception e) {
			log.error("Failed to publish message: {}", message);
		}
	}
}
