package com.sky.tech.ChatBox.rest;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sky.tech.ChatBox.entity.Comments;
import com.sky.tech.ChatBox.entity.Tweets;
import com.sky.tech.ChatBox.entity.Users;
import com.sky.tech.ChatBox.service.CommentsService;
import com.sky.tech.ChatBox.service.TweetsService;
import com.sky.tech.ChatBox.service.UsersService;

@RestController
@RequestMapping("/api2")
public class CommentsRestController {
	
	private CommentsService commentsServiceObj;
	private UsersService uesrsServiceObj;
	private TweetsService tweetsServiceObj;
	
	@Autowired
	public CommentsRestController(CommentsService commentsService, UsersService uesrsService, TweetsService tweetsService) {
		this.commentsServiceObj = commentsService;
		this.uesrsServiceObj = uesrsService;
		this.tweetsServiceObj = tweetsService;
	}
	
	
//	one user can found it's own comments
	@GetMapping("/comments/user/{userName}") 
	public ResponseEntity<Object> findCommentByUserName(@PathVariable String userName) {
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    String loggedInUser = authentication.getName(); 
	    List<String> userRoles = authentication.getAuthorities().stream()
	    	    .map(GrantedAuthority::getAuthority)
	    	    .collect(Collectors.toList());
	    
	    boolean isStaff = userRoles.contains("ROLE_staff");
	    
	    if(isStaff) {
	    	System.out.println("Staff can see all other user's comments");	    		    	
	    }
	    else if (!loggedInUser.equals(userName) ) {
	    	return ResponseEntity.status(HttpStatus.FORBIDDEN)
	                .body(Collections.singletonMap("message", "Access denied! You can only view your own comments."));
	    }
	    List<Comments> comments = commentsServiceObj.findByUserName(userName);
	    if (comments.isEmpty()) {
	        return ResponseEntity.status(HttpStatus.NO_CONTENT)
	                .body(Collections.singletonMap("message", "No comments found for user '" + userName + "'."));
	    }
	    return ResponseEntity.ok(comments);			
	}
	
//	specific for respective tweet's comment
//	ONLY FOR COMMERCIAL USES
	@GetMapping("/comments/tweet/{tweetId}")
	public ResponseEntity<?> findCommentsByTweetId(@PathVariable Long tweetId) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    String loggedInUser = authentication.getName(); 
	    List<String> userRoles = authentication.getAuthorities().stream()
	    	    .map(GrantedAuthority::getAuthority)
	    	    .collect(Collectors.toList());
	    
	    boolean isStaff = userRoles.contains("ROLE_staff");
	    if(!isStaff) {
	    	throw new RuntimeException("ONLY STAFF CAN USE THIS.");
	    }
	    Tweets tweet = tweetsServiceObj.findByTweetId(tweetId);
	    if (tweet == null) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
	    }
	    List<Comments> comments = commentsServiceObj.findByTweetId(tweetId);
	    if (comments.isEmpty()) {
	        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(comments);
	    }
	    return ResponseEntity.ok(comments);
	}
	
//	following is specific for 1 comment 
//	ONLY FOR COMMERCIAL USES
	@GetMapping("/comments/{commentId}") 
	public ResponseEntity<?> findCommentByCommentId(@PathVariable Long commentId) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    String loggedInUser = authentication.getName(); 
	    List<String> userRoles = authentication.getAuthorities().stream()
	    	    .map(GrantedAuthority::getAuthority)
	    	    .collect(Collectors.toList());
	    
	    boolean isStaff = userRoles.contains("ROLE_staff");
	    if(!isStaff) {
	    	return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
	    }
	    Comments commentObj = commentsServiceObj.findByCommentId(commentId);
	    if (commentObj == null) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Comment not found - " + commentId);
	    }
		return ResponseEntity.ok(commentObj);
	}
	
	@PostMapping("/comments")
	public ResponseEntity<Object> addComment(@RequestBody Comments theComment) {
		if (theComment == null || theComment.getCommentContent() == null) {
			return ResponseEntity.badRequest().body("Invalid comment data");
	    }
		org.springframework.security.core.Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    String loggedInUser = authentication.getName();
	    
	    Users user = uesrsServiceObj.findByuserName(loggedInUser);
	    if (user == null) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");
	    }
	    theComment.setUser(user);
	    Comments savedComment = commentsServiceObj.save(theComment);
		return ResponseEntity.status(HttpStatus.CREATED).body(savedComment);
	}
	
	@PutMapping("/comments")
	public ResponseEntity<Object> updateComment(@RequestBody Comments updatedComment) {
		if (updatedComment == null || updatedComment.getCommentContent() == null) {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
	                             .body("Invalid comment data");
	    }
		Long commentId = updatedComment.getCommentId();
		Comments existingComment = commentsServiceObj.findByCommentId(commentId);
		if (existingComment == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Comment not found - " + commentId);
		}
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String loggedInUser = authentication.getName();
		
		if(!existingComment.getUser().getUserName().equals(loggedInUser)) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN)                    .body("Not your comment!");	
		}
		existingComment.setCommentContent(updatedComment.getCommentContent());
		commentsServiceObj.save(existingComment);
		return ResponseEntity.ok(existingComment);
	}
	
	@DeleteMapping("/comments/{commentId}")
	public ResponseEntity<?> deleteCommentf(@PathVariable Long commentId) {
		
		org.springframework.security.core.Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    String loggedInUser = authentication.getName(); 
	    
	    List<String> userRoles = authentication.getAuthorities().stream()
	    	    .map(GrantedAuthority::getAuthority)
	    	    .collect(Collectors.toList());
	    
	    boolean isStaff = userRoles.contains("ROLE_staff");

	    Comments commentObj = commentsServiceObj.findByCommentId(commentId);

	    if (commentObj == null) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                             .body("Comment not found - " + commentId);
	    }
	    if(isStaff) {
	    	System.out.println("you can delete tweet as you are staff!");	    		    	
	    }
	    else if (!loggedInUser.equals(commentObj.getUser().getUserName()) ) {
	    	return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Not allowed! You can only delete your own comments.");
	    }
	    
		commentsServiceObj.deleteComment(commentId, loggedInUser);		
		return ResponseEntity.ok("Deleted Comment with ID " + commentId + " by user " + loggedInUser);
	}
	
	
	
		
	
	
}
