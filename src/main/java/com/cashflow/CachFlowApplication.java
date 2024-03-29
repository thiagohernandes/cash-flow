package com.cashflow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@ComponentScan("com.cashflow")
@EnableAsync
public class CachFlowApplication {

	public static void main(String[] args) {
		SpringApplication.run(CachFlowApplication.class, args);
	}

}
