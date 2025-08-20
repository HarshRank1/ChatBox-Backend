package com.sky.tech.ChatBox.service;
import java.util.List;
import com.sky.tech.ChatBox.entity.Users;

public interface UsersService {
	List<Users> findAll();
	Users findByuserName(String userName);
	void save(Users theUsers);
	void deleteByuserName(String userName);
}
