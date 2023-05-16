package com.example.travel.controllers;

import com.example.travel.Util.Util;
import com.example.travel.dto.LoginRequest;
import com.example.travel.dto.LoginResponse;
import com.example.travel.entity.User;
import com.example.travel.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Log4j2
public class UserController {

    private final UserService userService;

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest loginRequest) {
        log.info("Request fired on UserController method, login");

        log.info("Calling service method authenticateUser()");
        return userService.authenticateUser(loginRequest);
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> registerUser(@RequestBody User user) {
        log.info("Request fired on UserController method, register");

        log.info("Calling service method registerUser");
        userService.registerUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message", "User registered successfully"));
    }

    @PostMapping("/info")
    public String getUserInfo(@RequestParam String username) {
        log.info("Fetching user from username :{}", username);
        return userService.fetchUser(username);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, String>> updateUserInfo(@PathVariable String id, @RequestBody String user) {
        log.info("Requesting for update using id :{}", id);
        userService.updateUser(id, user);
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("message", "User updated successfully"));
    }

    @PutMapping("/pw/{id}")
    public ResponseEntity<Map<String, String>> updateUserWithoutPWChange(@PathVariable String id, @RequestBody String user) {
        log.info("Requesting for update using id :{}", id);
        userService.updateUserWithoutPWChange(id, user);
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("message", "User updated successfully"));
    }

    @GetMapping("/{role}")
    public String fetchAllUsers(@PathVariable String role) {
        if (role != null && role.equalsIgnoreCase(Util.USER)) {
            return userService.fetchAllUsers();
        } else if (role != null && role.equalsIgnoreCase(Util.VENDOR)) {
            return userService.fetchAllVendors();
        } else {
            return ResponseEntity.badRequest().toString();
        }
    }

}
