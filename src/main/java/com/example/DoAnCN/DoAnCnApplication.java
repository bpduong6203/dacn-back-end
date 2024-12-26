package com.example.DoAnCN;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DoAnCnApplication {
	public static void main(String[] args) {
		SpringApplication.run(DoAnCnApplication.class, args);
	}
}
