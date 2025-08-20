package com.sky.tech.ChatBox.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.sky.tech.ChatBox.dao.TweetsRepository;
import com.sky.tech.ChatBox.entity.Tweets;


@Service
public class TweetsServiceImpl implements TweetsService {
	
	private TweetsRepository tweetsRepository;
	
	@Autowired
	public TweetsServiceImpl(@Qualifier("tweetsRepository") TweetsRepository tweetsRepository) {
		this.tweetsRepository = tweetsRepository;
	}

	@Override
	public List<Tweets> findAll() {
		return tweetsRepository.findAll();
	}

	@Override
	public List<Tweets> findByuserName(String userName) {
		return tweetsRepository.findByUser_UserName(userName);
	}

	@Override
	public Tweets save(Tweets theTweet) {
		return tweetsRepository.save(theTweet);
	}

	@Override
	public void deleteTweet(Long tweetId) {
		tweetsRepository.deleteById(tweetId);
	}

	@Override
	public Tweets findByTweetId(Long tweetId) {
		return tweetsRepository.findById(tweetId).orElse(null);
	}
	
	

}
