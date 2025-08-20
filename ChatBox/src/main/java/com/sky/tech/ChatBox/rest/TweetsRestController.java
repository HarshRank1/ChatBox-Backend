package com.sky.tech.ChatBox.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.sky.tech.ChatBox.entity.Tweets;
import com.sky.tech.ChatBox.entity.Users;
import com.sky.tech.ChatBox.service.TweetsService;

import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;


@RestController
@RequestMapping("/api")
public class TweetsRestController {
	
	private TweetsService tweetsServiceObj;
	

	@Autowired
	public TweetsRestController(TweetsService tweetsService) {
		tweetsServiceObj = tweetsService;
	}
	
	@GetMapping("/tweets") 
	public  ResponseEntity<Object> findAllTweet() {
		List<Tweets> tweets = tweetsServiceObj.findAll();
		if (tweets == null || tweets.isEmpty()) {
	        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No tweets found.");
	    }
		return ResponseEntity.ok(tweets);
	}
	
	@GetMapping("/tweets/{userName}")
	public ResponseEntity<?> getTweetByUserName(@PathVariable String userName) {
		List<Tweets> theTweetObj = tweetsServiceObj.findByuserName(userName);
		if(theTweetObj == null || theTweetObj.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tweets not found for user - " + userName);
		}
	    return ResponseEntity.ok(theTweetObj);
	}

	@PostMapping(value = "/tweets", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> addTweet(@RequestBody Tweets theTweet) {
		if (theTweet == null || theTweet.getTweetContent() == null) {
			return ResponseEntity.badRequest().body("Invalid tweet data");
	    }
		org.springframework.security.core.Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    String loggedInUser = authentication.getName();
		
	    // follwoing line help us to make postman without username just enter tweetContent
		theTweet.setUser(new Users(loggedInUser, loggedInUser, 0, loggedInUser, null, null));
		Tweets savedTweet = tweetsServiceObj.save(theTweet);
		return ResponseEntity.status(HttpStatus.CREATED).body(savedTweet);
	}
	
	@PutMapping("/tweets")
	public ResponseEntity<?> updateTweet(@RequestBody Tweets updatedTweet) {

		if (updatedTweet == null || updatedTweet.getTweetContent() == null) {
			 return ResponseEntity.badRequest().body("Invalid tweet data");
	    }
		org.springframework.security.core.Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    String loggedInUser = authentication.getName(); 

	    Tweets existingTweet = tweetsServiceObj.findByTweetId(updatedTweet.getTweetId());
	    if (existingTweet == null) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tweet not found!");
	    }
	    if (!existingTweet.getUser().getUserName().equals(loggedInUser)) {
	        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Not allowed! You can only update your own tweets.");
	    }
	    existingTweet.setTweetContent(updatedTweet.getTweetContent());
	    Tweets savedTweet = tweetsServiceObj.save(existingTweet);
		return ResponseEntity.ok(savedTweet);
	}
	
	@DeleteMapping("/tweets/{tweetId}")
	public ResponseEntity<?> deleteTweet(@PathVariable Long tweetId) {
	    org.springframework.security.core.Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    String loggedInUser = authentication.getName(); 
	    List<String> userRoles = authentication.getAuthorities().stream()
	    	    .map(GrantedAuthority::getAuthority)
	    	    .collect(Collectors.toList());

	    boolean isStaff = userRoles.contains("ROLE_staff");

	    Tweets tweet = tweetsServiceObj.findByTweetId(tweetId);
	    if(tweet == null) {
	    	return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tweet not found - " + tweetId);
	    }
	    if(isStaff) {
	    	System.out.println("you can delete tweet as you are staff!");	    		    	
	    }
	    else if (!loggedInUser.equals(tweet.getUser().getUserName()) ) {
	        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Not allowed! You can only delete your own tweets.");
	    }
		tweetsServiceObj.deleteTweet(tweetId);		
	    return ResponseEntity.ok("Deleted Tweet " + tweetId);
	}
	
	
	
	
}
