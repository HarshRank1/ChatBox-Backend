package com.sky.tech.ChatBox.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class Users {

	@Id
	@Column(name = "username",  length = 50)
	private String userName;

	@Column(name = "password")
	private String Password;
	
	@Column(name = "fullname")
	private String fullName;

	@Column(name = "active")
	private int active;

//	NOTE :
//	When orphanRemoval = true is used in a @OneToMany relationship,
//	it means if a child entity (e.g., Tweet or Comment) is removed from the parent’s collection, 
//	it will be automatically deleted from the database.

//	Users(One) -- Tweets(Many)
//	@JsonIgnore use in child side
	@JsonIgnore
//	@JsonBackReference
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Tweets> tweets;

//	Users(One) -- Comments(Many)
	@JsonIgnore
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Comments> comments;

	public Users() {}
	
	public Users(Users user) {
		super();
		this.userName = user.userName;
		Password = user.Password;
		this.fullName = user.fullName;
		this.active = user.active;
		this.tweets = user.tweets;
		this.comments = user.comments;
	}


	public Users(String userName, String password, int active, String fullName, List<Tweets> tweets, List<Comments> comments) {
		super();
		this.userName = userName;
		Password = password;
		this.fullName = fullName;
		this.active = active;
		this.tweets = tweets;
		this.comments = comments;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return Password;
	}

	public void setPassword(String password) {
		Password = password;
	}

	public int getActive() {
		return active;
	}

	public void setActive(int active) {
		this.active = active;
	}

	public List<Tweets> getTweets() {
		return tweets;
	}

	public void setTweets(List<Tweets> tweets) {
		this.tweets = tweets;
	}

	public List<Comments> getComments() {
		return comments;
	}

	public void setComments(List<Comments> comments) {
		this.comments = comments;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	@Override
	public String toString() {
		return "Users [userName=" + userName + ", Password=" + Password + ", FullName=" + fullName + ", Active=" + active + "]";
	}

}
