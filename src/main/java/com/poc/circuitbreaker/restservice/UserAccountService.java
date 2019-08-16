package com.poc.circuitbreaker.restservice;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.poc.circuitbreaker.restservice.pojo.UserAccount;

@Service
public class UserAccountService {

	private final RestTemplate restTemplate;

	public UserAccountService(RestTemplate rest) {
		this.restTemplate = rest;
	}

	@HystrixCommand( fallbackMethod="defaultUser", threadPoolKey="userThreadPool",
			threadPoolProperties={@HystrixProperty(name="coreSize",value="1"),
					@HystrixProperty(name="maximumSize",value="2"),
					@HystrixProperty(name = "maxQueueSize", value = "5"),
					@HystrixProperty(name = "queueSizeRejectionThreshold", value = "8")})

	public String getUserById(String id) {
		URI uri = URI.create("http://localhost:8080/user/"+id);
		ResponseEntity<String> response=null;
		try {
		 response=  this.restTemplate.getForEntity(uri, String.class);
		}catch(Exception ex) {
			ex.printStackTrace();
			
			System.out.println("**** Error response status  **** " + response.getStatusCode()+response.getBody());
		}
		
		System.out.println("**** response status  **** " + response.getStatusCode()+response.getBody());
		
		return response.getBody();
	}

	public String defaultUser(String id) {
		System.out.println("**** Fall back ****");
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = null;
		try {
			jsonString = mapper.writeValueAsString(new UserAccount("firstName", "lastName", "userId"));
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return jsonString;
	}
}
