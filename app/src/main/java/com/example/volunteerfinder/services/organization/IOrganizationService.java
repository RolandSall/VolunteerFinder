package com.example.volunteerfinder.services.organization;

import com.example.volunteerfinder.models.Organization;
import com.example.volunteerfinder.models.User;
import com.example.volunteerfinder.services.user.UserLoginRequest;

import java.util.function.Consumer;

public interface IOrganizationService {

    Organization save(RegisterOrganizationRequest regOrganizationRequest) throws Exception;

    void login(UserLoginRequest request, Consumer<Organization> consumer) throws Exception;
}
