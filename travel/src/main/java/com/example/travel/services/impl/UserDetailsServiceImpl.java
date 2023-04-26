package com.example.travel.services.impl;

import com.example.travel.Util.Util;
import com.example.travel.dto.CustomUserDetails;
import com.example.travel.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final MongoTemplate mongoTemplate;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Query query = new Query();
        query.addCriteria(Criteria.where("username").is(username));
        Optional<User> user = mongoTemplate.find(query, User.class, Util.USERS).stream().findFirst();

        if (user.isPresent()) {
            return new CustomUserDetails(user.get());
        } else {
            throw new BadCredentialsException("Cannot find user from given username.");
        }
    }
}
