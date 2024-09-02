package com.task_organizer.utility;

import com.task_organizer.service.UserServiceImpl;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final JwtUtils jwtUtils;

    private final UserServiceImpl userDetailsService;

    public JwtAuthenticationFilter(JwtUtils jwtUtils, UserServiceImpl userDetailsService) {
        this.jwtUtils = jwtUtils;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String requestHeader = request.getHeader("Authorization");
        if (requestHeader != null && requestHeader.startsWith("Bearer ")) {
            String token = requestHeader.substring(7);

            if (! token.contains("null")) {
                String identifier = jwtUtils.getUsernameFromToken(token);
                try {
                    if (Boolean.TRUE.equals(jwtUtils.validateToken(token, identifier))
                            && SecurityContextHolder.getContext().getAuthentication() == null) {

                        String username = jwtUtils.getUsernameFromToken(token);

                        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null,
                                userDetails.getAuthorities());
                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                } catch (ExpiredJwtException e) {
                    logger.error("JWT token expired: {}", e.getMessage());
                } catch (MalformedJwtException e) {
                    logger.error("Invalid JWT token: {}", e.getMessage());
                } catch (Exception e) {
                    logger.error("Error processing JWT token: {}", e.getMessage());
                }
            }
        } else {
            logger.info("Invalid or missing Authorization header");
        }

        filterChain.doFilter(request, response);
    }

}