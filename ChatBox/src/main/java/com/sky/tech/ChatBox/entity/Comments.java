package com.sky.tech.ChatBox.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;


@Entity
@Table(name = "comments")
public class Comments {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;
	
	@Column(nullable = false, length = 280)
	private String commentContent;
	
//	Comments(Many) -- Tweets(One)
	@ManyToOne
	@JsonBackReference
	@JoinColumn(name = "tweet_id", nullable = false)
	private Tweets tweet;
	
//	Comments(Many) -- Users(One)
	@ManyToOne
//	@JsonBackReference
	@JsonIgnoreProperties({"fullName", "active", "tweets", "password"})
	@JoinColumn(name = "username", referencedColumnName = "userName", nullable = false)
	private Users user;
	
	public Comments() {}

	public Comments(Long commentId, String commentContent, Tweets tweet, Users user) {
		super();
		this.commentId = commentId;
		this.commentContent = commentContent;
		this.tweet = tweet;
		this.user = user;
	}

	public Long getCommentId() {
		return commentId;
	}

	public void setCommentId(Long commentId) {
		this.commentId = commentId;
	}

	public String getCommentContent() {
		return commentContent;
	}

	public void setCommentContent(String commentContent) {
		this.commentContent = commentContent;
	}

	public Tweets getTweet() {
		return tweet;
	}

	public void setTweet(Tweets tweet) {
		this.tweet = tweet;
	}

	public Users getUser() {
		return user;
	}

	public void setUser(Users user) {
		this.user = user;
	}
}
