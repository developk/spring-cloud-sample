package com.gndbiz.modules.company;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = {"com.gndbiz"})
public class CompanyApplication {
	public static void main(String[] args) {
		SpringApplication.run(CompanyApplication.class, args);
	}
}
