package com.example.volunteerfinder.services.organization;

import com.example.volunteerfinder.models.Organization;
import com.example.volunteerfinder.models.User;
import com.example.volunteerfinder.services.helper.HashingService;
import com.example.volunteerfinder.services.user.RegisterUserResponse;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

public class OrganizationService implements  IOrganizationService {

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference dbReference = database.getReference("Organizations");
    private HashingService  hashingService = new HashingService();

    @Override
    public RegisterOrganizationResponse save(Organization organization) throws Exception {
        UUID uuid = UUID.randomUUID();
        String hashPassword = hashingService.generateHash(organization.getPassword());
        organization.setPassword(hashPassword);
        dbReference.child(uuid.toString()).setValue(organization);
        RegisterOrganizationResponse response = buildServiceResponseFromFireBaseResponse(uuid, organization);
        return response;
    }


    private RegisterOrganizationResponse buildServiceResponseFromFireBaseResponse(UUID uuid, Organization organization) {
        return new RegisterOrganizationResponse().builder()
                .address(organization.getAddress())
                .name(organization.getName())
                .webPage(organization.getWebPage())
                .organizationId(uuid.toString())
                .build();
    }
}

