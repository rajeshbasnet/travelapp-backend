package com.example.travel.entity;

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
    private  int id;
    private  String username;
    private  String password;
    private String role;
    private String number;
}
