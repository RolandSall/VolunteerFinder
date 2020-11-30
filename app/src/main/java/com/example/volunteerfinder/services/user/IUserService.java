package com.example.volunteerfinder.services.user;

import com.example.volunteerfinder.model.User;

public interface IUserService {
    UserServiceResponse save(User request) throws Exception;
}
