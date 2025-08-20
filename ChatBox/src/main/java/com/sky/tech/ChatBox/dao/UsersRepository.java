package com.sky.tech.ChatBox.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sky.tech.ChatBox.entity.Users;

import jakarta.transaction.Transactional;

public interface UsersRepository extends JpaRepository<Users, String> {
	Users findByUserName(String userName);
	
	@Transactional
	void deleteByUserName(String userName);
}
