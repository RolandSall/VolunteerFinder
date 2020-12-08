package com.example.volunteerfinder.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.volunteerfinder.R;
import com.example.volunteerfinder.models.Event;

public class EventActivity extends AppCompatActivity {

    private Event event;
    private TextView eventTitle;
    private TextView description;
    private TextView capacity;
    private Button organizationWebPageButton;
    private Button googleMapsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        initSetup();
    }

    private void initSetup(){
        event = (Event)getIntent().getSerializableExtra("event");
        eventTitle = findViewById(R.id.singleEventTitle);
        description = findViewById(R.id.Description);
        capacity = findViewById(R.id.Capacity);

        // TODO: Make the event in the UI then added it here

        capacity.setText("Volunteer Needed: " + computeCapacity(event));
        eventTitle.setText(event.getTitle());
        description.setText(event.getDescription());


        organizationWebPageButton = findViewById(R.id.openWebPageButton);
        googleMapsButton = findViewById(R.id.googleMapsButton);

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
    }

    private int computeCapacity(Event event) {
        return  event.getCapacity() - event.getParticipants().size();
    }
}