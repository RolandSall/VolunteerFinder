package com.example.volunteerfinder.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;

import com.example.volunteerfinder.R;
import com.example.volunteerfinder.adapters.EventAdapter;
import com.example.volunteerfinder.models.Event;
import com.example.volunteerfinder.models.Organization;
import com.example.volunteerfinder.services.event.EventService;
import com.google.gson.Gson;

import java.util.ArrayList;

public class OrganizationProfileActivity extends AppCompatActivity implements EventAdapter.OnCardListener {

    private RecyclerView eventRecyclerView;
    private Button addEventButton;


    private EventService eventService = new EventService();
    private SharedPreferences sp;

    private ArrayList<Event> eventList;
    private EventAdapter eventAdapter;

    private Organization organization;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organization_profile);
        initSetup();
    }

    private void initSetup() {
        eventList = new ArrayList<>();
        eventRecyclerView = findViewById(R.id.eventRecyclerView);
        addEventButton = findViewById(R.id.addEventButton);


        organization = (Organization) getIntent().getSerializableExtra("organization");

        eventService.getEvents(list-> eventAdapter.update(new ArrayList<>(list)));
        eventAdapter = new EventAdapter(this, eventList, this);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        eventRecyclerView.setLayoutManager(layoutManager);
        eventRecyclerView.setItemAnimator(new DefaultItemAnimator());
        eventRecyclerView.setAdapter(eventAdapter);
        eventRecyclerView.addItemDecoration(new DividerItemDecoration(eventRecyclerView.getContext(), DividerItemDecoration.VERTICAL));

        addEventButton.setOnClickListener(e -> {
            Intent intent = new Intent(OrganizationProfileActivity.this, NewEventActivity.class);
            intent.putExtra("organization", organization);
            startActivity(intent);
        });
    }

    @Override
    public void onCardClick(int position) {
        Event event = eventList.get(position);
        Intent intent = new Intent(this, EventActivity.class);
        intent.putExtra("event", event);
        startActivity(intent);
    }
}