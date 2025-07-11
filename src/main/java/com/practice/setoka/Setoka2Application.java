package com.practice.setoka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class Setoka2Application {

	public static void main(String[] args) {
		SpringApplication.run(Setoka2Application.class, args);
	}

}
