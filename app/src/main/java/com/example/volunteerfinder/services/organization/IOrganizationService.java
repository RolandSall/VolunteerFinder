package com.example.volunteerfinder.services.organization;

import com.example.volunteerfinder.models.Organization;
import com.example.volunteerfinder.models.User;

public interface IOrganizationService {

    Organization save(RegisterOrganizationRequest regOrganizationRequest) throws Exception;
}
