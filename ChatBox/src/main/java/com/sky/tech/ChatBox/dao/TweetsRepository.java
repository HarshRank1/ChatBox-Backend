package com.sky.tech.ChatBox.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sky.tech.ChatBox.entity.Tweets;

public interface TweetsRepository extends JpaRepository<Tweets, Long> {
	List<Tweets> findByUser_UserName(String userName);
	void deleteById(Long tweetId);
	Optional<Tweets> findById(Long tweetId);
}
