package com.example.blog.security;

import io.jsonwebtoken.MalformedJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import com.example.blog.util.JwtUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Component
@Profile("jwt-auth")
public class JwtAuthorizationFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtils jwtUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String clientId = null;
        String role = null;
        String jwt = null;
        String authorizationHeader = request.getHeader("Authorization");



        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){
            jwt = authorizationHeader.substring(7);
            // validate the token
            try{
                if(!jwtUtils.isTokenExpired(jwt)){
                    clientId = jwtUtils.extractClientId(jwt);
                    role = jwtUtils.extractRole(jwt);
                }
                // make sure we have a valid clientId & role
                if(clientId != null && role != null && SecurityContextHolder.getContext().getAuthentication() == null){
                    User user = new User(clientId, clientId, List.of(new SimpleGrantedAuthority(role)));
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
            catch(MalformedJwtException e){
                System.out.println("Auth Exception");
            }

        }
        filterChain.doFilter(request, response);
    }


}

