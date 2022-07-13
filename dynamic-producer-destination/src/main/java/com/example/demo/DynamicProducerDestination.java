package com.example.demo;

import java.util.function.Function;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.binder.BinderHeaders;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;

@SpringBootApplication
public class DynamicProducerDestination {
	public static void main(String[] args) {
		SpringApplication.run(DynamicProducerDestination.class, args);
	}
	
	@Bean
	public Function<Message<String>, Message<String>> dynamicDestination() {
	    return v -> {
	    	System.out.println("Received message on topic default/destination: " + v.getPayload());
	    	System.out.println("CorrelationID: " + v.getHeaders().get("solace_correlationId"));
	        
	        // Use whatever business logic you'd like to figure out the topic!
	        String cid = (String) v.getHeaders().get("solace_correlationId");
	        if (cid == null) {
	          cid = Integer.toString(1);
	        }
	        String myTopic = "dynamic/destination/".concat(cid);
	        System.out.println("Publishing to: " + myTopic);
	        return MessageBuilder.withPayload(v.getPayload())
	        						.setHeader(BinderHeaders.TARGET_DESTINATION, myTopic)
	        						.build();	        
	    };
	}
}
