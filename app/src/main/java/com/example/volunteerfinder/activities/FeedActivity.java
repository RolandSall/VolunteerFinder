package com.example.volunteerfinder.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.volunteerfinder.R;
import com.example.volunteerfinder.adapters.EventAdapter;
import com.example.volunteerfinder.models.Event;
import com.example.volunteerfinder.models.User;
import com.example.volunteerfinder.services.event.EventService;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class FeedActivity extends AppCompatActivity implements EventAdapter.OnCardListener {

    private RecyclerView eventRecyclerView;

    private Button ftch;
    private Button dummy;
    private Button fetchByOrgId;

    private EventService eventService = new EventService();
    private SharedPreferences sp;

    private ArrayList<Event> eventList;
    private EventAdapter eventAdapter;

    private ProgressDialog TempDialog;
    private CountDownTimer countDownTimer;
    private int counter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
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
                eventService.getEvents(list-> eventAdapter.update(new ArrayList<>(list)));
            }
        }.start();

        User user = new Gson().fromJson(sp.getString("user", ""), User.class);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        eventRecyclerView.setLayoutManager(layoutManager);
        eventRecyclerView.setItemAnimator(new DefaultItemAnimator());
        eventRecyclerView.setAdapter(eventAdapter);
        eventRecyclerView.addItemDecoration(new DividerItemDecoration(eventRecyclerView.getContext(), DividerItemDecoration.VERTICAL));



        ftch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventService.getEvents(list-> eventAdapter.update(new ArrayList<>(list)));
                Toast.makeText(FeedActivity.this, "There is " + eventList.size() + " Events", Toast.LENGTH_SHORT).show();
            }
        });

        fetchByOrgId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Pressed with dummy Id");
                eventService.getEvents(list -> {
                    eventAdapter.update(new ArrayList<>(list.stream().filter(event -> event.getOrganization().getOrganizationId().equals("1")).collect(Collectors.toList())));
                });
            }
        });



        dummy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventService.saveEvent(null);
                TempDialog.show();
                countDownTimer = new CountDownTimer(2000,1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        TempDialog.setMessage("Wait a Moment....");
                    }

                    @Override
                    public void onFinish() {
                        TempDialog.dismiss();
                        eventService.getEvents(list-> eventAdapter.update(new ArrayList<>(list)));
                    }
                }.start();
            }
        });
    }

    private void initSetup() {
        eventRecyclerView = findViewById(R.id.eventRecyclerView);
        sp = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);

        eventList = new ArrayList<>();
        eventAdapter = new EventAdapter(this, eventList, this);

        TempDialog = new ProgressDialog(FeedActivity.this);
        TempDialog.setMessage("Wait a Moment....");
        TempDialog.setCancelable(false);
        TempDialog.setProgress(counter);
        TempDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        TempDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.GRAY));

        ftch = findViewById(R.id.fetchB);
        dummy = findViewById(R.id.addbutton);
        fetchByOrgId = findViewById(R.id.fetchByOrgId);


        eventService.getEvents(list-> eventAdapter.update(new ArrayList<>(list)));
    }

    @Override
    public void onCardClick(int position) {
        Event event = eventList.get(position);
        Intent intent = new Intent(this, EventActivity.class);
        intent.putExtra("event", event);
        startActivity(intent);
    }
}