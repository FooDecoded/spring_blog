package com.example.blog.service;

import com.example.blog.dto.AuthenticationResponse;
import com.example.blog.dto.LoginRequest;
import com.example.blog.dto.RegisterRequest;
import com.example.blog.exception.UserNotFoundException;
import com.example.blog.model.User;
import com.example.blog.repository.UserRepository;
import com.example.blog.security.UserDetailsServiceImp;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.blog.util.JwtUtils;

import java.util.Optional;

@Service
public class AuthService {

    public AuthService(UserRepository userRepository){
        this.userRepository = userRepository;
    }
    private UserRepository userRepository;

    @Autowired(required = false)
    private PasswordEncoder passwordEncoder;
    @Autowired(required = false)
    private JwtUtils jwtUtils;
    @Autowired(required = false)
    private UserDetailsServiceImp userDetailsServiceImp;

    public void createUserIfNotExist(String username){
        boolean isPresent = userRepository.findByUsername(username).isPresent();
        if(isPresent) return;

        User user = User.builder()
                .username(username)
                .build();

        userRepository.save(user);
    }


    public User getCurrentUser() {
        org.springframework.security.core.userdetails.User principal =
                (org.springframework.security.core.userdetails.User)
                SecurityContextHolder.
                getContext().getAuthentication().getPrincipal();

        return userRepository.findByUsername(principal.getUsername())
                .orElseThrow(() -> new UserNotFoundException("User name not found - " + principal.getUsername()));
    }

    public boolean isLoggedIn() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return !(authentication instanceof AnonymousAuthenticationToken) && authentication.isAuthenticated();
    }

    public void signup(RegisterRequest registerRequest) {
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));

        userRepository.save(user);
    }

    public AuthenticationResponse login(LoginRequest loginRequest) {
        UserDetails userDetails = userDetailsServiceImp.loadUserByUsername(loginRequest.getUsername());
        String passwordHash = userDetails.getPassword();

        if(passwordEncoder.matches(passwordHash, loginRequest.getPassword())){
            return AuthenticationResponse.builder()
                    .authenticationToken(jwtUtils.getJwtToken(userDetails.getUsername()))
                    .username(loginRequest.getUsername())
                    .build();
        } else {
            throw new RuntimeException("Authentication failed");
        }
    }

    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
