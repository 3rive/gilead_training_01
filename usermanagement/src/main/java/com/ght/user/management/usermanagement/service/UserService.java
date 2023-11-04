package com.ght.user.management.usermanagement.service;

import java.util.List;
import java.util.Optional;

import com.ght.user.management.usermanagement.model.User;

public interface UserService {
	
	public List<User> getUserList();
	
	public Optional<User> findUserById(long id);
	
	public void save(User user);
	
	public void edit(User user);
	
	public void delete(long id);

}