package com.example.volunteerfinder.services.organization;

import androidx.annotation.NonNull;

import com.example.volunteerfinder.models.Organization;
import com.example.volunteerfinder.models.User;
import com.example.volunteerfinder.services.helper.HashingService;
import com.example.volunteerfinder.services.user.UserLoginRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.UUID;
import java.util.function.Consumer;

import lombok.SneakyThrows;

public class OrganizationService implements  IOrganizationService {

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference dbReference = database.getReference("Organizations");
    private OrganizationMapper organizationMapper = new OrganizationMapper();
    private HashingService  hashingService = new HashingService();

    @Override
    public Organization save(RegisterOrganizationRequest regOrganizationRequest) throws Exception {
        UUID uuid = UUID.randomUUID();
        OrganizationDAO organizationDAO = buildOrganizationDAOFromRequest(uuid,regOrganizationRequest);
        dbReference.child(uuid.toString()).setValue(organizationDAO);
        Organization response = buildOrganizationFromResponse(uuid, organizationDAO);
        return response;
    }

    @Override
    public void login(UserLoginRequest request, Consumer<Organization> consumer) throws Exception {
        dbReference.orderByChild("email").equalTo(request.getEmail()).addValueEventListener(new ValueEventListener() {

            @SneakyThrows
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                OrganizationDAO organization = organizationMapper.getOrganizationDAOById(getOrganizationId(snapshot), getOrganizationDAO(snapshot));
                if(isValidCredential(organization, request)){
                    consumer.accept(buildOrganizationFromOrganizationDAO(organization));
                }else {
                    consumer.accept(null);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private Organization buildOrganizationFromOrganizationDAO(OrganizationDAO organizationDAO) {
        return new Organization().builder()
                .organizationId(organizationDAO.getOrganizationId())
                .name(organizationDAO.getName())
                .webPage(organizationDAO.getWebPage())
                .address(organizationDAO.getAddress())
                .build();
    }

    private HashMap getOrganizationDAO(@NonNull DataSnapshot snapshot) {
        return (HashMap) snapshot.getChildren().iterator().next().getValue();
    }

    private String getOrganizationId(@NonNull DataSnapshot snapshot) {
        return snapshot.getChildren().iterator().next().getKey();
    }

    private boolean isValidCredential(OrganizationDAO organizationDAO, UserLoginRequest request) throws Exception {
        if (organizationDAO != null) {
            if (organizationDAO.getPassword().equals(hashingService.generateHash(request.getPassword()))) {
                return true;
            }
        }
        return false;
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

