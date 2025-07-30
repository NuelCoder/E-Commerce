package com.example.pcuhub.pcuhub.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.pcuhub.pcuhub.security.JwtFilter;

@Configuration
public class SecurityConfig {
    private final JwtFilter jwtFilter;

    private final AuthenticationProvider authenticationProvider;

    public SecurityConfig(JwtFilter jwtFilter, AuthenticationProvider authenticationProvider) {
        this.jwtFilter = jwtFilter;
        this.authenticationProvider = authenticationProvider;
    }

 @Bean
 public  SecurityFilterChain securityFilterChain(HttpSecurity http)throws Exception{
    http
    .csrf(AbstractHttpConfigurer::disable)
    .authorizeHttpRequests(auth -> auth
    .requestMatchers("/api/v1/auth/**", "/api/v1/users/register","/api/v1/users/verify","/api/v1/otp/**").permitAll()
    .anyRequest().authenticated())
    .sessionManagement(session -> session
    .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
    .authenticationProvider(authenticationProvider)
    .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

return http.build();
 }
}
