package com.example.volunteerfinder.services.events;

import com.example.volunteerfinder.model.User;
import com.example.volunteerfinder.services.user.UserServiceResponse;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

public interface IEventService {

    void getEvents();

}
