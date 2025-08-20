package com.sky.tech.ChatBox.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.sky.tech.ChatBox.entity.Comments;

public interface CommentsRepository extends JpaRepository<Comments, Long>{
	List<Comments> findByTweet_TweetId(Long tweetId);
	List<Comments> findByUser_UserName(String userName);
	Optional<Comments> findById(Long commentId);
	void deleteById(Long commentId);

}
