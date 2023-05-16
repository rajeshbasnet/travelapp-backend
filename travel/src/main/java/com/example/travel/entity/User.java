package com.example.travel.entity;

import com.mongodb.lang.Nullable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class User {
    @Id
    private String id;
    private String firstname;
    private String lastname;
    private String username;
    private String password;
    private String role;
    private String number;
    @Nullable
    private String imageUri;
}
