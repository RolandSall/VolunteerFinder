package com.example.volunteerfinder.Services;


import com.example.volunteerfinder.model.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

public class UserService implements IUserService {


    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference = database.getReference("Users");

    @Override
    public void save(User request) {
        UUID uuid=UUID.randomUUID();
        reference.child(uuid.toString()).setValue(request);
        System.out.println(request);

    }
}
