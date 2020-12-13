package com.example.volunteerfinder.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.volunteerfinder.R;
import com.example.volunteerfinder.models.Event;
import com.example.volunteerfinder.models.User;
import com.example.volunteerfinder.services.event.EventService;
import com.example.volunteerfinder.services.event.IEventService;
import com.example.volunteerfinder.services.event.UpdateEventRequest;
import com.example.volunteerfinder.services.organization.OrganizationRegisterRequest;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class EventActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;
    private Event event;
    private TextView eventTitle;
    private TextView description;
    private TextView capacity;
    private TextView date;
    private Button organizationWebPageButton;
    private Button googleMapsButton;
    private Button volunteerButton;
    private ImageView eventImage;
    private User user;
    private SharedPreferences sp;
    private IEventService eventService = new EventService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        initSetup();
    }

    private void initSetup(){
        mediaPlayer= MediaPlayer.create(this,R.raw.achievementunlocked);
        sp = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        event = (Event)getIntent().getSerializableExtra("event");
        user = new Gson().fromJson(sp.getString("user", ""), User.class);
        eventTitle = findViewById(R.id.eventTitle);
        description = findViewById(R.id.eventDescription);
        capacity = findViewById(R.id.eventCapacity);
        date = findViewById(R.id.eventDate);
        eventImage = findViewById(R.id.eventImageView);

        capacity.setText("Volunteers Needed: " + computeCapacity(event));
        eventTitle.setText(event.getTitle());
        description.setText(event.getDescription());
        date.setText("Date: " + event.getEventDate());

        Picasso.get().load(event.getImage()).into(eventImage);

        organizationWebPageButton = findViewById(R.id.openWebPageButton);
        googleMapsButton = findViewById(R.id.googleMapsButton);
        volunteerButton = findViewById(R.id.eventVolunteerButton);

        organizationWebPageButton.setOnClickListener(e -> {
            Intent intent = new Intent(EventActivity.this, OrganizationWebPageActivity.class);
            intent.putExtra("organization", event.getOrganization());
            startActivity(intent);
        });

        googleMapsButton.setOnClickListener(e -> {
            Intent intent = new Intent(EventActivity.this, MapsActivity.class);
            intent.putExtra("location", event.getLocation());
            startActivity(intent);
        });

        if(event.getParticipants()!= null && event.getParticipants().stream().anyMatch(u -> u.getUserId().equals(user.getUserId()))){
            volunteerButton.setEnabled(false);
            volunteerButton.setText("Already Volunteering");
        }

        volunteerButton.setOnClickListener(e -> {
            mediaPlayer.start();
            ArrayList<User> participants = event.getParticipants();
            if(participants == null) participants = new ArrayList<>();
            participants.add(user);
            event.setParticipants(participants);
            eventService.updateEventById(buildUpdateEventRequest(), ev -> {
                volunteerButton.setEnabled(false);
                volunteerButton.setText("Already Volunteering");
                Toast.makeText(EventActivity.this, "Thank you for volunteering", Toast.LENGTH_SHORT).show();
            });
        });
    }

    private UpdateEventRequest buildUpdateEventRequest() {
        return UpdateEventRequest.builder()
                .title(event.getTitle())
                .capacity(event.getCapacity())
                .description(event.getDescription())
                .eventDate(event.getEventDate())
                .eventId(event.getEventId())
                .image(event.getImage())
                .location(event.getLocation())
                .organization(event.getOrganization())
                .participants(event.getParticipants())
                .postedDate(event.getPostedDate())
                .build();
    }

    private int computeCapacity(Event event) {
        if(event.getParticipants() != null)
            return  event.getCapacity() - event.getParticipants().size();
        else return 0;
    }
}