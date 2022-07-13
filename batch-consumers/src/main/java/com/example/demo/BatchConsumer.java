package com.example.demo;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.function.Consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;

import com.solace.spring.cloud.stream.binder.messaging.SolaceBinderHeaders;

@SpringBootApplication
public class BatchConsumer {
	public static void main(String[] args) {
		SpringApplication.run(BatchConsumer.class, args);
	}
	
	@Bean
	Consumer<Message<List<byte[]>>> batchConsume() {
		return batchMsg -> { // (1)
			List<?> data = batchMsg.getPayload();
			MessageHeaders headers = batchMsg.getHeaders();
			List<?> dataHeaders = (List<?>) headers.get(SolaceBinderHeaders.BATCHED_HEADERS);

			System.out.println("Batch Size: " + data.size());
			for (int i=0; i< data.size(); i++) {
				System.out.println("Batch Headers: " + dataHeaders.get(i));
				System.out.println("Batch Payload: " + new String((byte[]) data.get(i), StandardCharsets.UTF_8));
			}
		};
	}
}
