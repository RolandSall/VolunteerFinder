package com.example.volunteerfinder.services.user;

import com.example.volunteerfinder.models.Event;
import com.example.volunteerfinder.models.User;

import java.util.List;
import java.util.function.Consumer;

public interface IUserService {

    RegisterUserResponse save(User request) throws Exception;


    void login(Consumer<User> consumer, UserLoginRequest request) throws Exception;


}
