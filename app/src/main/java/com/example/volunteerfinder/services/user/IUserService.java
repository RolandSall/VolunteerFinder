package com.example.volunteerfinder.services.user;

import com.example.volunteerfinder.models.User;

public interface IUserService {
    UserServiceResponse save(User request) throws Exception;
}
