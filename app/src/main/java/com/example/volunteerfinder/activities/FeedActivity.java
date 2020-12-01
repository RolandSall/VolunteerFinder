package com.example.volunteerfinder.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import com.example.volunteerfinder.R;
import com.example.volunteerfinder.adapters.EventAdapter;
import com.example.volunteerfinder.models.Events;
import com.example.volunteerfinder.models.User;
import com.example.volunteerfinder.services.events.EventService;
import com.example.volunteerfinder.services.events.EventsServiceResponse;
import com.example.volunteerfinder.services.events.IEventService;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class FeedActivity extends AppCompatActivity {

    private RecyclerView eventRecyclerView;

    private Button ftch;
    private Button dummy;

    private EventService eventService = new EventService();
    private SharedPreferences sp;

    private List<Events> eventsList = new ArrayList<>();

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
                eventsList = buildListOfEvents(eventService.getEventsServiceResponses());
            }
        }.start();

        User user = new Gson().fromJson(sp.getString("user", ""), User.class);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        eventRecyclerView.setLayoutManager(layoutManager);
        eventRecyclerView.setItemAnimator(new DefaultItemAnimator());
        eventRecyclerView.setAdapter(new EventAdapter(this, new ArrayList<>(eventsList)));
        eventRecyclerView.addItemDecoration(new DividerItemDecoration(eventRecyclerView.getContext(), DividerItemDecoration.VERTICAL));



        ftch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(eventsList.size());
                Toast.makeText(FeedActivity.this, "There is " + eventService.getEventsServiceResponses().size() + " Events", Toast.LENGTH_SHORT).show();
            }
        });

        dummy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventService.saveEvent(null);
                eventService.getEvents();
                eventsList = buildListOfEvents(eventService.getEventsServiceResponses());
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



        eventService.getEvents();

        ftch = findViewById(R.id.fetchB);
        dummy = findViewById(R.id.addbutton);


    }

    private List<Events> buildListOfEvents(List<EventsServiceResponse> eventsServiceResponses) {
        while (eventsServiceResponses.equals(null)){
            System.out.println("No Data To Be Shown");
        }
        List<Events> responseList = new ArrayList<>();
        for (EventsServiceResponse eventsServiceResponse : eventsServiceResponses) {
            responseList.add(getSingleEvent(eventsServiceResponse));
        }
        return responseList;
    }

    private Events getSingleEvent(EventsServiceResponse eventsServiceResponse) {
        return new Events().builder()
                .eventId(eventsServiceResponse.getEventId())
                .location(eventsServiceResponse.getLocation())
                .postedDate(eventsServiceResponse.getPostedDate())
                .eventDate(eventsServiceResponse.getEventDate())
                .capacity(eventsServiceResponse.getCapacity())
                .description(eventsServiceResponse.getDescription())
                .organization(eventsServiceResponse.getOrganization())
                .build();
    }
}