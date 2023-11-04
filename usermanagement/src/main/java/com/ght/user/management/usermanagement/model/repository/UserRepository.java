/**
 * 
 */
package com.ght.user.management.usermanagement.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ght.user.management.usermanagement.model.User;

/**
 * 
 */
public interface UserRepository extends JpaRepository<User, Long> {

}
