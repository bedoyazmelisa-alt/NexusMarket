package application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Application entry point. Persistence auto-configurations (MySQL via JPA and
 * MongoDB) are enabled; connection settings live in application.properties
 * and can be overridden with environment variables (MONGODB_URI,
 * SPRING_DATASOURCE_URL, ...).
 */
@SpringBootApplication
public class NexusmarketApplication {

	public static void main(String[] args) {
		SpringApplication.run(NexusmarketApplication.class, args);
	}
}
