package com.example.volunteerfinder.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.volunteerfinder.R;
import com.example.volunteerfinder.adapters.EventAdapter;
import com.example.volunteerfinder.adapters.OrganizationEventAdapter;
import com.example.volunteerfinder.models.Event;
import com.example.volunteerfinder.models.Organization;
import com.example.volunteerfinder.models.User;
import com.example.volunteerfinder.services.event.EventService;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import java.util.ArrayList;

public class OrganizationProfileActivity extends AppCompatActivity implements OrganizationEventAdapter.OnCardListener {

    private RecyclerView eventRecyclerView;

    private EventService eventService = new EventService();
    private SharedPreferences sp;

    private ArrayList<Event> eventList;
    private OrganizationEventAdapter eventAdapter;
    private Organization organization;


    private ProgressDialog TempDialog;
    private CountDownTimer countDownTimer;
    private int counter;

    FloatingActionButton fab1;
    ExtendedFloatingActionButton fab2;
    View fabBGLayout;
    boolean isFABOpen = false;

    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organization_profile);
        initSetup();

        TempDialog.show();
        countDownTimer = new CountDownTimer(1500,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                TempDialog.setMessage("Wait a Moment....");
            }

            @Override
            public void onFinish() {
                TempDialog.dismiss();
            }
        }.start();

    }

    private void initSetup() {
        eventList = new ArrayList<>();
        eventRecyclerView = findViewById(R.id.eventRecyclerView);

        fab1 = findViewById(R.id.fab1);
        fab2 = findViewById(R.id.fab2);
        fabBGLayout = findViewById(R.id.fabBGLayout);
        sp = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        organization = new Gson().fromJson(sp.getString("organization", ""), Organization.class);;

        eventService.getOrganizationEvents(organization, list-> eventAdapter.update(new ArrayList<>(list)));
        eventAdapter = new OrganizationEventAdapter(this, eventList, this, i -> deleteEvent(i));

        TempDialog = new ProgressDialog(OrganizationProfileActivity.this);
        TempDialog.setMessage("Wait a Moment....");
        TempDialog.setCancelable(false);
        TempDialog.setProgress(counter);
        TempDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        TempDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.GRAY));

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        eventRecyclerView.setLayoutManager(layoutManager);
        eventRecyclerView.setItemAnimator(new DefaultItemAnimator());
        eventRecyclerView.setAdapter(eventAdapter);
        eventRecyclerView.addItemDecoration(new DividerItemDecoration(eventRecyclerView.getContext(), DividerItemDecoration.VERTICAL));

        fab2.setOnClickListener(e -> {
            Intent intent = new Intent(OrganizationProfileActivity.this, NewEventActivity.class);
            intent.putExtra("organization", organization);
            startActivity(intent);
        });

        fab1.setOnClickListener(view -> {
            if (!isFABOpen) {
                showFABMenu();
            } else {
                closeFABMenu();
            }
        });

        fabBGLayout.setOnClickListener(view -> closeFABMenu());
    }

    private void showFABMenu() {
        isFABOpen = true;
        fabBGLayout.setVisibility(View.VISIBLE);
        fab1.animate().rotationBy(45);
        fab2.animate().translationY(-getResources().getDimension(R.dimen.standard_65));
        fab2.setVisibility(View.VISIBLE);
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

    private void deleteEvent(Integer position) {
        Event event = eventList.get(position);
        builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure that you want to delete this event?")
                .setCancelable(false)
                .setPositiveButton("Yes", (dialog, id) -> {
                    eventService.deleteEvent(event.getEventId());
                    dialog.dismiss();
                })
                .setNegativeButton("No", (dialog, id) -> dialog.cancel());
        AlertDialog alert = builder.create();
        alert.setTitle("Delete Event");
        alert.show();
    }
}