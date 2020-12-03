package com.example.volunteerfinder.services.organization;

import com.example.volunteerfinder.models.Organization;
import com.example.volunteerfinder.models.User;

public interface IOrganizationService {

    RegisterOrganizationResponse save(Organization organization) throws Exception;
}
