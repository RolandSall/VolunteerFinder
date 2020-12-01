package com.example.volunteerfinder.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.volunteerfinder.R;
import com.example.volunteerfinder.adapters.EventAdapter;
import com.example.volunteerfinder.models.Event;
import com.example.volunteerfinder.models.User;
import com.example.volunteerfinder.services.event.EventService;
import com.google.gson.Gson;

import java.util.ArrayList;

public class FeedActivity extends AppCompatActivity {

    private RecyclerView eventRecyclerView;

    private Button ftch;
    private Button dummy;

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
        countDownTimer = new CountDownTimer(2000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                TempDialog.setMessage("Wait a Moment....");
            }

            @Override
            public void onFinish() {
                TempDialog.dismiss();
                eventAdapter.update(new ArrayList<>(eventService.getEvents()));
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
                eventAdapter.update(new ArrayList<>(eventService.getEvents()));
                Toast.makeText(FeedActivity.this, "There is " + eventService.getEvents().size() + " Events", Toast.LENGTH_SHORT).show();
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
                        eventAdapter.update(new ArrayList<>(eventService.getEvents()));
                    }
                }.start();
            }
        });
    }

    private void initSetup() {
        eventRecyclerView = findViewById(R.id.eventRecyclerView);
        sp = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);

        TempDialog = new ProgressDialog(FeedActivity.this);
        TempDialog.setMessage("Wait a Moment....");
        TempDialog.setCancelable(false);
        TempDialog.setProgress(counter);
        TempDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        TempDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.GRAY));



        ftch = findViewById(R.id.fetchB);
        dummy = findViewById(R.id.addbutton);

        eventList = new ArrayList<>(eventService.getEvents());
        eventAdapter = new EventAdapter(this, eventList);
    }
}