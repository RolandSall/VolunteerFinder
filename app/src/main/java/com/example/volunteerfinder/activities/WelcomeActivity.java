package com.example.volunteerfinder.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageButton;

import com.example.volunteerfinder.R;

public class WelcomeActivity extends AppCompatActivity {

    private ImageButton volunteerButton;
    private ImageButton ngoButton;

    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        // checkUser();

        volunteerButton = findViewById(R.id.volunteerButton);
        ngoButton = findViewById(R.id.ngoButton);

        volunteerButton.setOnClickListener(event -> {
            startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
        });

        ngoButton.setOnClickListener(event -> {
            startActivity(new Intent(WelcomeActivity.this, LoginOrganizationActivity.class));
        });
    }

    private void checkUser() {
        String userString = sp.getString("user", "");
        if (!userString.equals("")) {
            startActivity(new Intent(WelcomeActivity.this, FeedActivity.class));
            finish();
        }
    }
}