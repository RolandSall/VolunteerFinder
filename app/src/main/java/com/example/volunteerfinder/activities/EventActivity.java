package com.example.volunteerfinder.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.volunteerfinder.R;
import com.example.volunteerfinder.models.Event;

public class EventActivity extends AppCompatActivity {

    private Event event;
    private TextView eventTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        initSetup();
    }

    private void initSetup(){
        event = (Event)getIntent().getSerializableExtra("event");
        eventTitle = findViewById(R.id.singleEventTitle);

        eventTitle.setText(event.getTitle());
    }
}