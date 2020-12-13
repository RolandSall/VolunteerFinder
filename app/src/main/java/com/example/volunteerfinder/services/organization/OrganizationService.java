package com.example.volunteerfinder.services.organization;

import androidx.annotation.NonNull;

import com.example.volunteerfinder.models.Organization;
import com.example.volunteerfinder.services.helper.HashingService;
import com.example.volunteerfinder.services.user.UserLoginRequest;
import com.example.volunteerfinder.services.user.UserRegisterRequest;
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


    public void save(OrganizationRegisterRequest registerRequest, Consumer<Organization> organizationConsumer) throws Exception {
        UUID uuid = UUID.randomUUID();
        hashPassword(registerRequest);
        dbReference.child(uuid.toString()).setValue(registerRequest).addOnCompleteListener(task ->
                organizationConsumer.accept(buildOrganizationAfterSaveSuccess(uuid,registerRequest)));
    }

    private Organization buildOrganizationAfterSaveSuccess(UUID uuid, OrganizationRegisterRequest registerRequest) {
        return new Organization().builder()
                .organizationId(uuid.toString())
                .address(registerRequest.getAddress())
                .webPage(registerRequest.getWebPage())
                .name(registerRequest.getName())
                .email(registerRequest.getEmail())
                .build();
    }

    @Override
    public void login(UserLoginRequest request, Consumer<Organization> consumer) {
        dbReference.orderByChild("email").equalTo(request.getEmail()).addValueEventListener(new ValueEventListener() {

            @SneakyThrows
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.exists()){
                    consumer.accept(null);
                } else {
                    OrganizationDAO organization = organizationMapper.getOrganizationDAOById(getOrganizationId(snapshot), getOrganizationDAO(snapshot));
                    if(isValidCredential(organization, request)){
                        consumer.accept(buildOrganizationFromOrganizationDAO(organization));
                    }else {
                        consumer.accept(null);
                    }
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

    private void hashPassword(OrganizationRegisterRequest request) throws Exception {
        request.setPassword(hashingService.generateHash(request.getPassword()));
    }


}

