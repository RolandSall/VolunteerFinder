package com.example.volunteerfinder.services.organization;

import com.example.volunteerfinder.models.Organization;
import com.example.volunteerfinder.services.user.UserLoginRequest;

import java.util.function.Consumer;

public interface IOrganizationService {

    void save(OrganizationRegisterRequest registerRequest, Consumer<Organization> organizationConsumer) throws Exception;

    void login(UserLoginRequest request, Consumer<Organization> consumer);
}
