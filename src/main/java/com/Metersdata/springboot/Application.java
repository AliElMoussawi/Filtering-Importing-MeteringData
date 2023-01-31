package com.Metersdata.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableScheduling
@EnableMongoRepositories
@EnableSwagger2
public class Application {
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
