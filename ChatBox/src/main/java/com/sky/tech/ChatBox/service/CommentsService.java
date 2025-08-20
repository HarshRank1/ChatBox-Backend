package com.sky.tech.ChatBox.service;

import java.util.List;
import java.util.Optional;

import com.sky.tech.ChatBox.entity.Comments;


public interface CommentsService {
	List<Comments> findByTweetId(Long tweetId);
	Comments save(Comments theComment);
	void deleteComment(Long CommentId, String userName);
	List<Comments> findByUserName(String userName);
	Comments findByCommentId(Long commentId);

}
