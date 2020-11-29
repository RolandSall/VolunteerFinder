package com.example.volunteerfinder.Services;


import com.example.volunteerfinder.model.User;

public class UserService implements IUserService {

    @Override
    public void save(User request) {
        System.out.println(request);

    }
}
