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
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.volunteerfinder.R;
import com.example.volunteerfinder.adapters.EventAdapter;
import com.example.volunteerfinder.models.Event;
import com.example.volunteerfinder.models.Organization;
import com.example.volunteerfinder.models.User;
import com.example.volunteerfinder.services.event.EventService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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

    FloatingActionButton fab1, fab2;
    View fabBGLayout;
    boolean isFABOpen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organization_profile);
        initSetup();
    }

    private void initSetup() {
        eventList = new ArrayList<>();
        eventRecyclerView = findViewById(R.id.eventRecyclerView);

        fab1 = findViewById(R.id.fab1);
        fab2 = findViewById(R.id.fab2);
        fabBGLayout = findViewById(R.id.fabBGLayout);
        sp = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        organization = new Gson().fromJson(sp.getString("organization", ""), Organization.class);;

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

        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isFABOpen) {
                    showFABMenu();
                } else {
                    closeFABMenu();
                }
            }
        });

        fabBGLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeFABMenu();
            }
        });
    }

    private void showFABMenu() {
        isFABOpen = true;
        fabBGLayout.setVisibility(View.GONE);
        fab1.animate().rotationBy(180);
        fab1.animate().translationY(-getResources().getDimension(R.dimen.standard_55));
        fab2.animate().translationY(-getResources().getDimension(R.dimen.standard_100));
        fab2.setVisibility(View.GONE);
    }

    private void closeFABMenu() {
        isFABOpen = false;
        fabBGLayout.setVisibility(View.GONE);
        fab1.animate().rotation(0);
        fab2.setVisibility(View.INVISIBLE);
        fab2.animate().translationY(0);
    }

    @Override
    public void onCardClick(int position) {
        Event event = eventList.get(position);
        Intent intent = new Intent(this, EventActivity.class);
        intent.putExtra("event", event);
        startActivity(intent);
    }
}