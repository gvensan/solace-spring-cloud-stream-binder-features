package com.example.demo;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;

import com.solace.spring.cloud.stream.binder.messaging.SolaceBinderHeaders;
import com.solace.spring.cloud.stream.binder.util.CorrelationData;

@SpringBootApplication
public class PublishConfirmation {
	public static void main(String[] args) {
		SpringApplication.run(PublishConfirmation.class, args);
	}
	
	@Bean
	public Consumer<String> publishConfirmationConsumer(StreamBridge sb) {
	    return v -> {
	        System.out.println("Received: " + v);

	        CorrelationData correlationData = new CorrelationData();
	        Message<String> message = MessageBuilder.withPayload("My Payload")
	                .setHeader(SolaceBinderHeaders.CONFIRM_CORRELATION, correlationData)
	                .build();

	        if (v.equals("fail")) {
	        	System.out.println("Sending to (fail): publish/topic/fail");
	            sb.send("publish/topic/fail", message);
	        } else {
	        	System.out.println("Sending to: publish/topic/success");
	            sb.send("publish/topic/success", message);
	        }
	        
	        try {
	            correlationData.getFuture().get(30, TimeUnit.SECONDS);
	            System.out.println("Publish Successful");
	            // Do success logic
	        } catch (InterruptedException | ExecutionException | TimeoutException e) {
	        	System.out.println("Publish Failed - Implement error handling logic here...");
	            // Do failure logic
	        }
	    };
	}
}
