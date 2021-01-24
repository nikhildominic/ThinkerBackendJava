package com.thinker.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.thinker.app.entity.Users;


public interface UserRepository extends JpaRepository<Users, String>{
	
	Users findByEmail(String email);

}
