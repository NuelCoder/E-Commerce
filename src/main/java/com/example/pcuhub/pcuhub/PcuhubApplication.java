package com.example.pcuhub.pcuhub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PcuhubApplication {

	public static void main(String[] args) {
		SpringApplication.run(PcuhubApplication.class, args);
	}

}
