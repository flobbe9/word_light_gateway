package de.word_light.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import de.word_light.gateway.config.ApplicationInitializer;


@SpringBootApplication
public class GatewayApplication {

	public static void main(String[] args) {
        new ApplicationInitializer(args).init();
		SpringApplication.run(GatewayApplication.class, args);
	}
}