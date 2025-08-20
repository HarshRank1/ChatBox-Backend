package com.sky.tech.ChatBox.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sky.tech.ChatBox.dao.UsersRepository;
import com.sky.tech.ChatBox.entity.Users;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@Service
public class UsersServiceImpl implements UsersService {
	
//	@Autowired
//	private BCryptPasswordEncoder passwordEncoder;
	
//	private UsersDAO usersDAO;
	private UsersRepository usersRepository;

	@Autowired
	public UsersServiceImpl(@Qualifier("usersRepository") UsersRepository usersRepositoryObj) {
		usersRepository = usersRepositoryObj;
	}
	
	@Override
	@Transactional
	public List<Users> findAll() {
		return usersRepository.findAll();
	}

	@Override
	@Transactional
	public Users findByuserName(String userName) {
		return usersRepository.findByUserName(userName);
	}

	@Override
	@Transactional
	public void save(Users theUser) {
//		theUser.setPassword(passwordEncoder.encode(theUser.getPassword()));
		usersRepository.save(theUser);
	}

	@Override
	@Transactional
	public void deleteByuserName(String userName) {
		usersRepository.deleteByUserName(userName);
	}

}
