package com.example.volunteerfinder.services.user;

import androidx.annotation.NonNull;

import com.example.volunteerfinder.models.User;
import com.example.volunteerfinder.services.helper.HashingService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.UUID;
import java.util.function.Consumer;

import lombok.SneakyThrows;

public class UserService implements IUserService {


    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference dbReference = database.getReference("Users");
    private HashingService hashingService = new HashingService();
    private UserServiceMapper userServiceMapper = new UserServiceMapper();


    @Override
    public void save(UserRegisterRequest request, Consumer<User> userConsumer) throws Exception {
        UUID uuid = UUID.randomUUID();
        hashPassword(request);
        dbReference.child(uuid.toString()).setValue(request).addOnCompleteListener(task -> userConsumer.accept(buildUserAfterSaveSuccess(uuid, request)));
    }

    private User buildUserAfterSaveSuccess(UUID uuid, UserRegisterRequest request) {
        return new User().builder()
                .lastName(request.getLastName())
                .firstName(request.getFirstName())
                .email(request.getEmail())
                .address(request.getAddress())
                .userId(uuid.toString())
                .build();
    }

    private void hashPassword(UserRegisterRequest request) throws Exception {
        request.setPassword(hashingService.generateHash(request.getPassword()));
    }

    @Override
    public void login(UserLoginRequest request, Consumer<User> consumer) {
        dbReference.orderByChild("email").equalTo(request.getEmail()).addValueEventListener(new ValueEventListener() {

            @SneakyThrows
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    consumer.accept(null);
                } else {
                    UserDAO userDAO = userServiceMapper.getUser(getUserId(snapshot), getUserDAO(snapshot));
                    if (isValidCredential(userDAO, request)) {
                        consumer.accept(buildUserFromUserDAO(userDAO));
                    } else {
                        consumer.accept(null);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private User buildUserFromUserDAO(UserDAO userDAO) {
        return User.builder()
                .userId(userDAO.getUserId())
                .email(userDAO.getEmail())
                .firstName(userDAO.getFirstName())
                .lastName(userDAO.getLastName())
                .address(userDAO.getAddress())
                .build();
    }

    private HashMap getUserDAO(@NonNull DataSnapshot snapshot) {
        return (HashMap) snapshot.getChildren().iterator().next().getValue();
    }

    private String getUserId(@NonNull DataSnapshot snapshot) {
        return snapshot.getChildren().iterator().next().getKey();
    }

    private boolean isValidCredential(UserDAO userDAO, UserLoginRequest request) throws Exception {
        if (userDAO != null) {
            if (userDAO.getPassword().equals(hashingService.generateHash(request.getPassword()))) {
                return true;
            }
        }
        return false;
    }

}
