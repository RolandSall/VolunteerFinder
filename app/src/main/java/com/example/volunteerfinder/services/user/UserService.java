package com.example.volunteerfinder.services.user;

import com.example.volunteerfinder.models.User;
import com.example.volunteerfinder.services.helper.HashingService;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

public class UserService implements IUserService {


    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference dbReference = database.getReference("Users");
    private HashingService hashingService = new HashingService();

    @Override
    public RegisterUserResponse save(User request) throws Exception {
        UUID uuid = UUID.randomUUID();
        String hashPassword = hashingService.generateHash(request.getPassword());
        request.setPassword(hashPassword);
        dbReference.child(uuid.toString()).setValue(request);
        RegisterUserResponse response = buildServiceResponseFromFireBaseResponse(uuid, request);
        return response;

    }

    private RegisterUserResponse buildServiceResponseFromFireBaseResponse(UUID uuid, User user) throws NoSuchFieldException {
        return new RegisterUserResponse().builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .userId(uuid.toString())
                .address(user.getAddress())
                .email(user.getEmail())
                .build();
    }

}
