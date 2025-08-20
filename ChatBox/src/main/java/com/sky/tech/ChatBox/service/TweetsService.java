package com.sky.tech.ChatBox.service;

import java.util.List;
import com.sky.tech.ChatBox.entity.Tweets;

public interface TweetsService {
	List<Tweets> findAll();
	List<Tweets> findByuserName(String userName);
	Tweets save(Tweets tweet);
	void deleteTweet(Long tweetId);
	Tweets findByTweetId(Long tweetId);

}