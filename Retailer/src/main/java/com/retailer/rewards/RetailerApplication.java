package com.retailer.rewards;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 * Main entry point for the Retailer Spring Boot application.

 *
 * This class bootstraps the Spring application context and starts the embedded server.
 
* When the application starts successfully, it prints a welcome message to the console.
 */

@SpringBootApplication
public class RetailerApplication {

	
public static void main(String[] args) {
	SpringApplication.run(RetailerApplication.class, args);
}

}
