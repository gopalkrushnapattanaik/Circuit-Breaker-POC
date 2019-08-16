package com.poc.circuitbreaker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.poc.circuitbreaker.restservice.UserAccountService;
import com.poc.circuitbreaker.restservice.pojo.UserAccount;

@SpringBootApplication
@EnableCircuitBreaker
@EnableHystrix
@RestController
@Configuration
@EnableHystrixDashboard
public class CircuitBreakerApplication {

	@Autowired
	private UserAccountService userService;

	public static void main(String[] args) {
		SpringApplication.run(CircuitBreakerApplication.class, args);
	}
	
	@Bean
	  public RestTemplate rest(RestTemplateBuilder builder) {
	    return builder.build();
	  }

	  @RequestMapping(value = "/user/{id}")
	  public String getUserById(@PathVariable("id") String id) {
		  System.out.println("getUserById*******"+id);
	    return userService.getUserById(id);
	  }

}
