package com.example.volunteerfinder.services.user;

import com.example.volunteerfinder.models.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

public class RegisterUserServiceImpl implements IRegisterUserService {


    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference = database.getReference("Users");

    @Override
    public RegisterUserResponse save(User request) throws Exception {
        UUID uuid = UUID.randomUUID();
        String hashPassword = generateHash(request.getPassword());
        request.setPassword(hashPassword);
        reference.child(uuid.toString()).setValue(request);
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

    public static String generateHash(String input) throws Exception {
        StringBuilder hash = new StringBuilder();

        try {
            MessageDigest sha = MessageDigest.getInstance("SHA-1");
            byte[] hashedBytes = sha.digest(input.getBytes());
            char[] digits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                    'a', 'b', 'c', 'd', 'e', 'f'};
            for (int idx = 0; idx < hashedBytes.length; idx++) {
                byte b = hashedBytes[idx];
                hash.append(digits[(b & 0xf0) >> 4]);
                hash.append(digits[b & 0x0f]);
            }
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("Failed To Save User");
        }
        return hash.toString();
    }
}
