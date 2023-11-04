/**
 * 
 */
package com.ght.user.management.usermanagement.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ght.user.management.usermanagement.model.User;
import com.ght.user.management.usermanagement.model.repository.UserRepository;
import com.ght.user.management.usermanagement.service.UserService;

/**
 * 
 */
@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserRepository userRepository;

	@Override
	public List<User> getUserList() {
		return userRepository.findAll();
	}

	@Override
	public Optional<User> findUserById(long id) {
		return userRepository.findById(id);
	}

	@Override
	public void save(User user) {
		userRepository.save(user);

	}

	@Override
	public void edit(User user) {
		userRepository.save(user);

	}

	@Override
	public void delete(long id) {
		userRepository.deleteById(id);

	}

}
