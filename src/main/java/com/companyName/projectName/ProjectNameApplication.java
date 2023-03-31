package com.companyName.projectName;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;

@SpringBootApplication
public class ProjectNameApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(ProjectNameApplication.class, args);
		System.out.println(context.getEnvironment());
	}

}
