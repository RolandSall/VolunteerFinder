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
    public Organization save(RegisterOrganizationRequest regOrganizationRequest) throws Exception {
        UUID uuid = UUID.randomUUID();
        OrganizationDAO organizationDAO = buildOrganizationDAOFromRequest(uuid,regOrganizationRequest);
        dbReference.child(uuid.toString()).setValue(organizationDAO);
        Organization response = buildOrganizationFromResponse(uuid, organizationDAO);
        return response;
    }

    private OrganizationDAO buildOrganizationDAOFromRequest(UUID uuid, RegisterOrganizationRequest organizationRequest) throws Exception {
        return new OrganizationDAO().builder()
                .address(organizationRequest.getAddress())
                .name(organizationRequest.getName())
                .webPage(organizationRequest.getWebPage())
                .organizationId(uuid.toString())
                .password(getHashedPassword(organizationRequest.getPassword()))
                .build();
    }

    private String getHashedPassword(String password) throws Exception {
        return hashingService.generateHash(password);
    }


    private Organization buildOrganizationFromResponse(UUID uuid, OrganizationDAO organization) {
        return new Organization().builder()
                .address(organization.getAddress())
                .name(organization.getName())
                .webPage(organization.getWebPage())
                .organizationId(uuid.toString())
                .build();
    }


}

