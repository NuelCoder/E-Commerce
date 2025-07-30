package com.example.pcuhub.pcuhub.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.pcuhub.pcuhub.repository.UserRepository;

@Configuration
public class AppConfig {
 private final UserRepository userRepository;

 public AppConfig(UserRepository userRepository) {
    this.userRepository = userRepository;
 }

public UserDetailsService userDetailsService(){
    return email -> userRepository.findByEmail(email)
    .orElseThrow(() -> new RuntimeException("User with " + email + " not found"));
}

@SuppressWarnings("deprecation")
@Bean
public AuthenticationProvider authenticationProvider(){
    DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
    authenticationProvider.setUserDetailsService(userDetailsService());
    authenticationProvider.setPasswordEncoder(passwordEncoder());
    return authenticationProvider;
}
@Bean
public AuthenticationManager authenticationManager(AuthenticationConfiguration config)throws Exception{
    return config.getAuthenticationManager();
}
@Bean
public PasswordEncoder passwordEncoder(){
    return new BCryptPasswordEncoder();
}
    }

