/**
 * 
 */
package com.gilead.spring.web.demo.demoProjectSpring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.gilead.spring.web.demo.demoProjectSpring.service.HelloService;

/**
 * 
 */
@Controller
public class HelloController {
	
	@Autowired
	private HelloService helloService;
	
	@GetMapping("/hello")
	public String hello(Model model) {
		String hello = helloService.sayHello();
		model.addAttribute("hello", hello);
		return "mypage";
	}
	
	@GetMapping("/")
	public String index() {
		return "index";
	}

}
