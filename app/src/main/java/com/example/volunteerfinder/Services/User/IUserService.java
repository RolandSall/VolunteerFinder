package com.example.volunteerfinder.Services.User;

import com.example.volunteerfinder.model.User;

public interface IUserService {
    UserServiceResponse save(User request) throws Exception;
}
