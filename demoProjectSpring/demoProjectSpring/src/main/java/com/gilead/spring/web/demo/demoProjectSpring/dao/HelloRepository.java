package com.gilead.spring.web.demo.demoProjectSpring.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gilead.spring.web.demo.demoProjectSpring.model.Hello;

public interface HelloRepository extends JpaRepository<Hello, Long> {

}
