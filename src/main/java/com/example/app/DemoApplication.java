package com.example.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

import com.example.endpoint.CourseAPI;

@SpringBootApplication
//@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
@ComponentScan(basePackageClasses = CourseAPI.class)
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

}
