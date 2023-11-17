package com.example.aws.demo;

import java.util.Arrays;

import com.example.aws.demo.config.S3Properties;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@ConfigurationPropertiesScan
public class AwsDemoApplication {

	public static void main(String[] args) {
		var applicationContext = SpringApplication.run(AwsDemoApplication.class, args);
		Arrays.stream(applicationContext.getBeanDefinitionNames()).forEach(System.out::println);
	}

}
