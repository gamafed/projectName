package com.companyName.projectName;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@Slf4j
public class ProjectNameApplication {

	public static void main(String[] args) {
		// http://localhost:8080/swagger-ui/index.html
		ConfigurableApplicationContext context = SpringApplication.run(ProjectNameApplication.class, args);
		log.info(context.getEnvironment().toString());
	}

}
