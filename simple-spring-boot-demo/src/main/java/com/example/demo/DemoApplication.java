package com.example.demo;

import java.util.function.Function;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.StaticMessageHeaderAccessor;
import org.springframework.integration.acks.AckUtils;
import org.springframework.integration.acks.AcknowledgmentCallback;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;

@SpringBootApplication
public class DemoApplication {	
	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}
	@Bean
	public Function<Message<String>, Message<String>> uppercase() {
		return message -> {
			System.out.println("Entering uppercase");
			String value = message.getPayload();
			System.out.println("uppercase input: " + value);
			value = value.toUpperCase();
			System.out.println("uppercase output: " + value);
			return MessageBuilder.withPayload(value)
				.build();
		};
	}
	@Bean
	public Function<Message<String>, Message<String>> reverse() {
		return message -> {
			System.out.println("Entering reverse");
			String value = message.getPayload();
			System.out.println("reverse input: " + value);
	        StringBuilder str = new StringBuilder(value);
			str.reverse();
			System.out.println("reverse output: " + str.toString());
			return MessageBuilder.withPayload(str.toString())
				.build();
		};
		
	}	
	@Bean
	public Function<Message<String>, Message<String>> lowercase() {
		return message -> {
			System.out.println("Entering lowercase");
			String value = message.getPayload();
			System.out.println("lowercase input: " + value);
			System.out.println("lowercase output: " + value.toLowerCase());
			return MessageBuilder.withPayload(value.toLowerCase())
				.build();
		};
	}
}
