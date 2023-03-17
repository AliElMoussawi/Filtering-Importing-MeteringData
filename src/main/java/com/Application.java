package com;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
/**
 * Please see the {@link: https://github.com/AliElMoussawi/Filtering-Importing-MeteringData} Repo of the project
 * @author Captain America
 */
@SpringBootApplication
@EnableScheduling
@EnableMongoRepositories
@EnableSwagger2
@EnableAsync(proxyTargetClass = true)
public class Application {
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
