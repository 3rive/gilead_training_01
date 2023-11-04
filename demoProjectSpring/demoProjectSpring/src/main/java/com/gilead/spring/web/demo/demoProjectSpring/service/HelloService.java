package com.gilead.spring.web.demo.demoProjectSpring.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gilead.spring.web.demo.demoProjectSpring.dao.HelloRepository;
import com.gilead.spring.web.demo.demoProjectSpring.model.Hello;

@Service
public class HelloService {
	
	@Autowired
	private HelloRepository helloDao;
	
	public String sayHello() {
		System.out.println("Inside Service layer");
		@SuppressWarnings("deprecation")
		Hello hello =  helloDao.getById((long) 2);
		return hello.getText();
	}

}
