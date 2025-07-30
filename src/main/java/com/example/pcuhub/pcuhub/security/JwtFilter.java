package com.example.pcuhub.pcuhub.security;

import java.io.IOException;

import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class JwtFilter extends OncePerRequestFilter{
     private final JwtUtil jwtUtil;
     private final UserDetailsService userDetailsService;
     public JwtFilter(JwtUtil jwtUtil, UserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
     }

@Override
protected void doFilterInternal(@NonNull HttpServletRequest request, 
                                @NonNull HttpServletResponse response, 
                                @NonNull FilterChain filterChain)throws ServletException, IOException {
                                    

                                    
                                    
String requestPath = request.getServletPath();
    
 // ❗️ Skip JWT checks for public endpoints
if (requestPath.startsWith("/api/v1/auth") || requestPath.equals("/api/v1/users/register") || requestPath.equals("/api/v1/users/verify") || requestPath.equals("/api/v1/otp/register")) {
        filterChain.doFilter(request, response);
        return;
    }                              
                                    
final String authHeader = request.getHeader("Authorization");
String jwt;
String username;

 if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
                    filterChain.doFilter(request, response);
                    return;
                }
    jwt = authHeader.substring(7);
    username = jwtUtil.extractUserName(jwt);

    if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        if (jwtUtil.isTokenValid(jwt, userDetails)) {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
            );
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }
    }
    filterChain.doFilter(request, response);
}
}