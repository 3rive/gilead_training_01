/**
 * 
 */
package com.ght.consumer.springboot.springexternalapiconsumer.rest.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.ght.consumer.springboot.springexternalapiconsumer.rest.DisplayJoke;
import com.ght.consumer.springboot.springexternalapiconsumer.rest.Employee;
import com.ght.consumer.springboot.springexternalapiconsumer.util.DateUtil;

/**
 * 
 */
@RestController
public class ConsumerController {
	
	
	@GetMapping("/client")
	public ResponseEntity<Object> displayEmployee() {
		RestTemplate restTemplate = new RestTemplate();
		Map<String, String> data = new HashMap<>();
		Employee employee = restTemplate.getForObject("https://reqres.in/api/users/3?delay=3", Employee.class);
		data.put("email",employee.getData().getEmail());
		data.put("message","This a message created by my api");
		String time = DateUtil.now();
		data.put("time", time);
		//data.put("time",DateUtil.now());)
		return new ResponseEntity<>(data, HttpStatus.OK);
		
	}
	
	@GetMapping("/joke")
	public ResponseEntity<Object> displayJoke() {
		RestTemplate restTemplate = new RestTemplate();
		Map<String, String> data = new HashMap<>();
		DisplayJoke randomJoke = restTemplate.getForObject("https://v2.jokeapi.dev/joke/Any?safe-mode", DisplayJoke.class);
		data.put("joke",randomJoke.getJoke());
		data.put("message","This a message created by my api");
		String time = DateUtil.now();
		data.put("time", time);
		//data.put("time",DateUtil.now());
		return new ResponseEntity<>(data, HttpStatus.OK);
	}

}
