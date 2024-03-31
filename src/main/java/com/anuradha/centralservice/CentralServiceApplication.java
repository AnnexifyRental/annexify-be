package com.anuradha.centralservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


@SpringBootApplication
public class CentralServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CentralServiceApplication.class, args);
	}

}
