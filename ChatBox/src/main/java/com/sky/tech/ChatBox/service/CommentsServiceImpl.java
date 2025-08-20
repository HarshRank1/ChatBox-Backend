package com.sky.tech.ChatBox.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.sky.tech.ChatBox.dao.CommentsRepository;
import com.sky.tech.ChatBox.dao.TweetsRepository;
import com.sky.tech.ChatBox.entity.Comments;


@Service
public class CommentsServiceImpl implements CommentsService {
	
	@Autowired
	private CommentsRepository commentsRepository;
	
	@Autowired
	public CommentsServiceImpl(@Qualifier("commentsRepository") CommentsRepository commentsRepository) {
		this.commentsRepository = commentsRepository;
	}

	@Override
	public List<Comments> findByTweetId(Long tweetId) {
		return commentsRepository.findByTweet_TweetId(tweetId);
	}

	@Override
	public Comments save(Comments theComment) {
		if (theComment == null || theComment.getCommentContent() == null || theComment.getTweet() == null) {
	        throw new RuntimeException("Invalid comment data");
	    }
		Comments comment = commentsRepository.save(theComment);
		return comment;
	}

	@Override
	public void deleteComment(Long commentId, String userName) {
		Optional<Comments> commentObj = commentsRepository.findById(commentId);
		if(commentObj == null) {
			throw new RuntimeException("Comment not found - " + commentId);
		}
		Comments comment = commentObj.get();
//		if(!comment.getUser().getUserName().equals(userName)) {
//			throw new RuntimeException("Not allowed!");
//		}
		commentsRepository.deleteById(commentId);
	}

	@Override
	public List<Comments> findByUserName(String userName) {
		return commentsRepository.findByUser_UserName(userName);
	}

	@Override
	public Comments findByCommentId(Long commentId) {
	    return commentsRepository.findById(commentId).orElse(null);
	}

}
