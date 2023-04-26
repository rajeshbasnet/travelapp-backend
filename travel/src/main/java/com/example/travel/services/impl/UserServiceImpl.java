package com.example.travel.services.impl;

import com.example.travel.Util.JWTUtil;
import com.example.travel.Util.Util;
import com.example.travel.dto.CustomUserDetails;
import com.example.travel.dto.LoginRequest;
import com.example.travel.dto.LoginResponse;
import com.example.travel.entity.User;
import com.example.travel.exception.EmailAlreadyExistsException;
import com.example.travel.exception.NotAuthenticatedException;
import com.example.travel.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.bson.Document;
import org.json.JSONObject;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Log4j2
public class UserServiceImpl implements UserService {

    private final AuthenticationManager authenticationManager;

    private final JWTUtil jwtUtil;

    private final MongoTemplate mongoTemplate;

    private final PasswordEncoder passwordEncoder;

    @Override
    public LoginResponse authenticateUser(LoginRequest loginRequest) {
        log.info("Executing service method, authenticateUser...");

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
            );

            log.info("User authenticated successfully");
            String token = generateToken(authentication);
            return new LoginResponse(token);

        } catch (UsernameNotFoundException exception) {
            throw new NotAuthenticatedException("Cannot find user from given username");

        } catch (BadCredentialsException exception) {
            throw new NotAuthenticatedException("Cannot authorize request body due to bad credentials");
        }
    }

    @Override
    public void registerUser(User user) {
        if (isEmailAlreadyRegistered(user.getUsername())) {
            throw new EmailAlreadyExistsException("Email has already been registered");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = mongoTemplate.save(user, Util.USERS);
        log.debug("Applicant saved into database successfully with id :{}", savedUser.getId());
    }


    private String generateToken(Authentication authentication) {
        final CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        Map<String, Object> payload = new HashMap<>();
        payload.put("userId", userDetails.getId());
        payload.put("role", userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining()));

        //Sending jwt token as a proper response
        return jwtUtil.generateToken(userDetails.getUsername(), payload);
    }

    private boolean isEmailAlreadyRegistered(String email) {
        return Optional.ofNullable(
                mongoTemplate.findOne(new Query(
                        Criteria.where("username")
                                .is(email)), Document.class, Util.USERS)).isPresent();
    }
}
