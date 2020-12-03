package com.example.volunteerfinder.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;

import com.example.volunteerfinder.R;
import com.example.volunteerfinder.models.Event;
import com.example.volunteerfinder.models.Organization;
import com.example.volunteerfinder.services.event.EventService;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.UUID;

public class NewEventActivity extends AppCompatActivity {

    private SharedPreferences sp;
    private Button createEventButton;
    private Organization organization;

    private EventService eventService = new EventService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);

        initSetup();
    }

    private void initSetup(){
        sp = getSharedPreferences("OrganizationInfo", Context.MODE_PRIVATE);
        organization = new Gson().fromJson(sp.getString("organization", ""), Organization.class);
        createEventButton = findViewById(R.id.createEventButton);

        createEventButton.setOnClickListener(e -> {
            eventService.createDummyEvent(organization);
            finish();
        });
    }



}