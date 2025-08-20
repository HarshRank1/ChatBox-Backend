package com.sky.tech.ChatBox.rest;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sky.tech.ChatBox.entity.Users;
import com.sky.tech.ChatBox.service.UsersService;

@RestController
@RequestMapping("/api1")
public class UsersRestController {
	
	private UsersService usersServiceObj;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	public UsersRestController(UsersService theUsersService) {
		usersServiceObj = theUsersService;
	}
	
	@GetMapping("/users") 
	public ResponseEntity<List<Users>> findAll() {
		List<Users> users = usersServiceObj.findAll();
		if (users.isEmpty()) {
	        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	    }
		return ResponseEntity.ok(users);
	}
	
	@GetMapping("/users/{userName}")
	public ResponseEntity<?> getUser(@PathVariable String userName) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    String loggedInUser = authentication.getName();
	    
	    List<String> userRoles = authentication.getAuthorities().stream()
	    	    .map(GrantedAuthority::getAuthority)
	    	    .collect(Collectors.toList());
	    
	    boolean isStaff = userRoles.contains("ROLE_staff");
	    
	    if(isStaff) {
	    	System.out.println("you can delete any user account as you are staff!");	    		    	
	    }
	    else if(!loggedInUser.equals(userName)) {
	    	return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You can't access other user's data!");
	    }
		
		Users theUserObj = usersServiceObj.findByuserName(userName);
		if(theUserObj == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found - " + userName);
		} 
		Users userResponse = new Users(theUserObj);
	    userResponse.setPassword(null);
	    return ResponseEntity.ok(userResponse);
	}
	
	@PostMapping("/users")
	public ResponseEntity<?> addUser(@RequestBody Users theUser) {
		if (theUser == null || theUser.getUserName() == null || theUser.getPassword() == null) {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid user data");
	    }
		Users existingUser = usersServiceObj.findByuserName(theUser.getUserName());
	    if (existingUser != null) {
	        return ResponseEntity.status(HttpStatus.CONFLICT).body("User already exists with username: " + theUser.getUserName());
	    }
		String hashedPassword = passwordEncoder.encode(theUser.getPassword());
	    theUser.setPassword(hashedPassword);
		usersServiceObj.save(theUser);
		return ResponseEntity.status(HttpStatus.CREATED).body(theUser);
	}
	
	@PutMapping("/users/pass")
	public ResponseEntity<?> updateUserPassword(@RequestBody Users updatedUser) {
		
		if (updatedUser == null || updatedUser.getUserName() == null || updatedUser.getPassword() == null) {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid data");
	    }
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String loggedInUser = authentication.getName();
		Users existingUser = usersServiceObj.findByuserName(updatedUser.getUserName());
		if(existingUser == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User Not Found!");
		}
		if (!loggedInUser.equals(updatedUser.getUserName())) {
	        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Not allowed! You can only update your own password.");
	    }
		String hashedPassword = passwordEncoder.encode(updatedUser.getPassword());
		existingUser.setPassword(hashedPassword);
		usersServiceObj.save(existingUser);
		return ResponseEntity.ok("Password updated successfully");
	}
	
	@PutMapping("/users/fullName")
	public ResponseEntity<?> updateUserFullName(@RequestBody Users updatedUser) {
		if (updatedUser == null || updatedUser.getUserName() == null || updatedUser.getFullName() == null) {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid data");
	    }
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String loggedInUser = authentication.getName();
		
		Users existingUser = usersServiceObj.findByuserName(updatedUser.getUserName());
		if(existingUser == null) {
			 return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User Not Found!");
		}
		if(!loggedInUser.equals(updatedUser.getUserName())) {
		       return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Not allowed! You can only update your own full name.");
		}
		existingUser.setFullName(updatedUser.getFullName());
		usersServiceObj.save(existingUser);
		return ResponseEntity.ok(existingUser);
	}
	
	
	@DeleteMapping("/users/{userName}")
	public ResponseEntity<?> deleteUser(@PathVariable String userName) {
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    String loggedInUser = authentication.getName(); 
	    
	    List<String> userRoles = authentication.getAuthorities().stream()
	    	    .map(GrantedAuthority::getAuthority)
	    	    .collect(Collectors.toList());
	    
	    boolean isStaff = userRoles.contains("ROLE_staff");
	    Users tempUserObj = usersServiceObj.findByuserName(userName);
	    
	    if(tempUserObj == null) {
//			throw new RuntimeException("User is not found - " + userName);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found - " + userName);
		}
	    
	    
	    if(isStaff) {
	    	System.out.println("you can delete any user account as you are staff!");	    		    	
	    }
	    else if (!loggedInUser.equals(tempUserObj.getUserName()) ) {
	    	 return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Not allowed! You cannot delete another user's account.");
	    }
		
//		here just do softDeletion
//		tempUserObj.setActive(0);
		usersServiceObj.deleteByuserName(userName);
		return ResponseEntity.ok("Deleted User " + userName);
	}
}













