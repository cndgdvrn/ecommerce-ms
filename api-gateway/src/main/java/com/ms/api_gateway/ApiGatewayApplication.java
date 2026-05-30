package com.ms.api_gateway;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ApiGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiGatewayApplication.class, args);
	}


//	@PostConstruct
//	void logg(){
//		System.out.printf("cannnnnnnnnnnnnnnnnnnnnnnn");
//	}
}
