package com.suarez.webporter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
public class WebporterApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebporterApplication.class, args);
	}

}
