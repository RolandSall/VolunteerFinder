package com.example.volunteerfinder.services.User;

import com.example.volunteerfinder.models.User;

public interface IUserService {
    UserServiceResponse save(User request) throws Exception;
}
