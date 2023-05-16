package com.example.travel.services;

import com.example.travel.dto.LoginRequest;
import com.example.travel.dto.LoginResponse;
import com.example.travel.entity.User;

public interface UserService {
    LoginResponse authenticateUser(LoginRequest loginRequest);

    void registerUser(User user);

    String fetchUser(String username);

    void updateUser(String id, String user);
    void updateUserWithoutPWChange(String id, String user);

    String fetchAllUsers();

    String fetchAllVendors();
}
