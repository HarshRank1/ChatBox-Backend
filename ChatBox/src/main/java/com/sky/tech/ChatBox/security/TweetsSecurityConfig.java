package com.sky.tech.ChatBox.security;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class TweetsSecurityConfig {
	
	@Bean
	public UserDetailsManager userDetailsManagerForTweets(DataSource dataSource) {
		
		JdbcUserDetailsManager jdbcUserDetailsManagerForTweets = new JdbcUserDetailsManager(dataSource);
		
		jdbcUserDetailsManagerForTweets.setUsersByUsernameQuery(
                "select username, password, active from users where username=?");
		
		jdbcUserDetailsManagerForTweets.setAuthoritiesByUsernameQuery(
//				if following format not ok then store ROLE_cust, ROLE_admin
                "select username, CONCAT('ROLE_', role) from roles where username=?");
		return jdbcUserDetailsManagerForTweets;
	}
	
	@Bean
	public SecurityFilterChain filterChainForTweets(HttpSecurity http) throws Exception {
		
		http.authorizeHttpRequests(configurer ->
        configurer
        		.requestMatchers(HttpMethod.POST, "/api1/users").permitAll()
        		
        		
        		.requestMatchers(HttpMethod.GET, "/api1/users").hasAnyRole("staff")
        		.requestMatchers(HttpMethod.GET, "/api1/users/**").hasAnyRole("cust", "staff")
        		.requestMatchers(HttpMethod.PUT, "/api1/users/pass").hasAnyRole("cust")
        		.requestMatchers(HttpMethod.PUT, "/api1/users/fullName").hasAnyRole("cust")
        		.requestMatchers(HttpMethod.DELETE, "/api1/users/**").hasAnyRole("cust", "staff")
        		
        		
        
        
                .requestMatchers(HttpMethod.GET, "/api/tweets").hasAnyRole("cust", "staff")
                .requestMatchers(HttpMethod.GET, "/api/tweets/**").hasAnyRole("cust", "staff")
                .requestMatchers(HttpMethod.POST, "/api/tweets").hasAnyRole("cust", "staff")
                .requestMatchers(HttpMethod.PUT, "/api/tweets").hasAnyRole("cust")
                .requestMatchers(HttpMethod.DELETE, "/api/tweets/**").hasAnyRole("cust","staff")
                
                
                
                
                .requestMatchers(HttpMethod.GET, "/api2/comments/user/**").hasAnyRole("cust", "staff")
//            	ONLY FOR COMMERCIAL USES
                .requestMatchers(HttpMethod.GET, "/api2/comments/tweet/**").hasAnyRole("staff")
                .requestMatchers(HttpMethod.GET, "/api2/comments/**").hasAnyRole("staff")
                .requestMatchers(HttpMethod.POST, "/api2/comments").hasAnyRole("cust","staff")
                .requestMatchers(HttpMethod.PUT, "/api2/comments").hasAnyRole("cust")
                .requestMatchers(HttpMethod.DELETE, "/api2/comments/**").hasAnyRole("cust","staff")
                
		);
		
		http.httpBasic(Customizer.withDefaults());
		
		 http.csrf(csrf -> csrf.disable());

		
		return http.build();
	}
	
	
	
	@Bean
	public BCryptPasswordEncoder passwordEncoderForTweets() {
		return new BCryptPasswordEncoder();
	}
}
