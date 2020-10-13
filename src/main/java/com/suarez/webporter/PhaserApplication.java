package com.suarez.webporter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
@ComponentScan(basePackages = {"com.suarez.webporter.core","com.suarez.webporter.util"})
public class PhaserApplication {

	public static void main(String[] args) {
		SpringApplication.run(PhaserApplication.class, args);
	}

}
