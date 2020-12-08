package com.example.volunteerfinder.services.user;

import com.example.volunteerfinder.models.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;

public class UserServiceMapper {
    public UserDAO getUser(String userId, HashMap userMap) {
        return UserDAO.builder()
                .userId(userId)
                .password(userMap.get("password").toString())
                .email(userMap.get("email").toString())
                .address(userMap.get("address").toString())
                .firstName(userMap.get("firstName").toString())
                .lastName(userMap.get("lastName").toString())
                .build();
    }

    public ArrayList<User> getUsers(ArrayList<HashMap> users) {
        return new ArrayList<>(users.stream().map(this::getUserModel).collect(Collectors.toList()));
    }

    private User getUserModel(HashMap userMap) {
        return User.builder()
                .userId(userMap.get("userId").toString())
                .email(userMap.get("email").toString())
                .address(userMap.get("address").toString())
                .firstName(userMap.get("firstName").toString())
                .lastName(userMap.get("lastName").toString())
                .build();
    }
}
