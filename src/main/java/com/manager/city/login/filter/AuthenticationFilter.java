package com.manager.city.login.filter;

import com.manager.city.login.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class AuthenticationFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationFilter.class);

    private final JwtService jwtService;

    private final UserDetailsService userDetailsService;

    public AuthenticationFilter(JwtService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        jwtService.getEmail(request)
                .ifPresentOrElse(email -> {
                    UserDetails userDetails = userDetailsService.loadUserByUsername(email);
                    ApiAuthentication authentication = new ApiAuthentication(userDetails);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    LOGGER.debug("Request from [" + email + "]");
                }, () -> LOGGER.debug("Request from non authenticated user"));
        filterChain.doFilter(request, response);
    }
}