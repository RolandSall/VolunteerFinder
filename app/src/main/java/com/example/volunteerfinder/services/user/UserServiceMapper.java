package com.example.volunteerfinder.services.user;

import com.example.volunteerfinder.models.User;

import java.util.HashMap;

public class UserServiceMapper {


    public User getUser(String key, HashMap userMap) {
        return User.builder()
                .userId(key)
                .password(userMap.get("password").toString())
                .email(userMap.get("email").toString())
                .address(userMap.get("address").toString())
                .firstName(userMap.get("firstName").toString())
                .lastName(userMap.get("lastName").toString())
                .build();
    }
}
