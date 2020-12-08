package com.example.volunteerfinder.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
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
        sp = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);

        checkUser();

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
        if (!sp.getString("user", "").equals("")) {
            startActivity(new Intent(WelcomeActivity.this, FeedActivity.class));
            finish();
        }

        else if (!sp.getString("organization", "").equals("")) {
            startActivity(new Intent(WelcomeActivity.this, OrganizationProfileActivity.class));
            finish();
        }
    }
}