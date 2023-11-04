/**
 * 
 */
package com.ght.user.management.usermanagement.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ght.user.management.usermanagement.model.User;
import com.ght.user.management.usermanagement.service.UserService;


/**
 * 
 */
@Controller
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@RequestMapping("/")
	public String index() {
		return "redirect:/list";		
	}
	
	@RequestMapping("/list")
	public String list(Model model) {
		List<User> users = userService.getUserList();
		model.addAttribute("users",users);
		return "user/list";		
	}
	
	@RequestMapping("/toAdd")
	public String addPage() {
		return "user/userAdd";		
	}
	
	@RequestMapping("/toEdit")
	public String editPage() {
		return "user/userEdit";		
	}
	

	@RequestMapping("/add")
	public String add(User user) {
		userService.save(user);
		return "redirect:/list";		
	}
}
