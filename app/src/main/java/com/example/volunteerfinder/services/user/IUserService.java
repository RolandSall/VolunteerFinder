package com.example.volunteerfinder.services.user;

import com.example.volunteerfinder.models.User;

import java.util.function.Consumer;

public interface IUserService {

    void save(UserRegisterRequest request, Consumer<User> userConsumer) throws Exception;

    void login(UserLoginRequest request, Consumer<User> consumer);


}
