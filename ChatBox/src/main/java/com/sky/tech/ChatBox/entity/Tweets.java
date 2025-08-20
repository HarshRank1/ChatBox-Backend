package com.sky.tech.ChatBox.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "tweets")
public class Tweets {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "tweet_id")
	private Long tweetId;

	@Column(name = "tweet_content", nullable = false, length = 280)
	private String tweetContent;
	
//	here main difference between @JsonBackReference and @JsonIgnoreProperties
//	in @JsonIgnoreProperties we can list which we not want
//	and in @JsonBackReference we can just denied to other table or entity's data

	// Tweets(One) -- Comments(Many)
	@JsonIgnoreProperties({"commentId"})
//	@JsonIgnore
	@OneToMany(mappedBy = "tweet", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Comments> comments;

	// Tweets(Many) -- User(One)
	@ManyToOne
	@JsonIgnoreProperties({"fullName", "active", "comments", "password"})
//	@JsonBackReference // -- if do this then not shown username
	@JoinColumn(name = "username", referencedColumnName = "userName", nullable = false)
	private Users user;
	
	public Tweets() {}

	public Tweets(Long tweetId, String tweetContent, List<Comments> comments, Users user) {
		super();
		this.tweetId = tweetId;
		this.tweetContent = tweetContent;
		this.comments = comments;
		this.user = user;
	}

	public Long getTweetId() {
		return tweetId;
	}

	public void setTweetId(Long tweetId) {
		this.tweetId = tweetId;
	}

	public String getTweetContent() {
		return tweetContent;
	}

	public void setTweetContent(String tweetContent) {
		this.tweetContent = tweetContent;
	}

	public List<Comments> getComments() {
		return comments;
	}

	public void setComments(List<Comments> comments) {
		this.comments = comments;
	}

	public Users getUser() {
		return user;
	}

	public void setUser(Users user) {
		this.user = user;
	}

}
