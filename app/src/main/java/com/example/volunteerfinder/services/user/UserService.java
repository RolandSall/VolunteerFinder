package com.example.volunteerfinder.services.user;

import androidx.annotation.NonNull;

import com.example.volunteerfinder.models.Event;
import com.example.volunteerfinder.models.User;
import com.example.volunteerfinder.services.event.EventServiceResponseMapper;
import com.example.volunteerfinder.services.event.EventsServiceResponse;
import com.example.volunteerfinder.services.helper.HashingService;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

import lombok.SneakyThrows;

public class UserService implements IUserService {


    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference dbReference = database.getReference("Users");
    private HashingService hashingService = new HashingService();
    private UserServiceMapper userServiceMapper = new UserServiceMapper();


    @Override
    public RegisterUserResponse save(User request) throws Exception {
        UUID uuid = UUID.randomUUID();
        String hashPassword = hashingService.generateHash(request.getPassword());
        request.setPassword(hashPassword);
        dbReference.child(uuid.toString()).setValue(request);
        RegisterUserResponse response = buildServiceResponseFromFireBaseResponse(uuid, request);
        return response;

    }

    @Override
    public void login(UserLoginRequest request, Consumer<User> consumer) throws Exception {
        dbReference.orderByChild("email").equalTo(request.getEmail()).addValueEventListener(new ValueEventListener() {

            @SneakyThrows
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = userServiceMapper.getUser(snapshot.getChildren().iterator().next().getKey(), (HashMap) snapshot.getChildren().iterator().next().getValue());
                if(isValidCredential(user, request)){
                    consumer.accept(user);
                }else {
                  consumer.accept(null);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private boolean isValidCredential(User user, UserLoginRequest request) throws Exception {
        if (user != null) {
            if (user.getPassword().equals(hashingService.generateHash(request.getPassword()))) {
                return true;
            }
        }
        return false;
    }

   /* private List<User> buildUserFromFireBaseResponse(ArrayList<EventsServiceResponse> eventList) {
    }*/


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
