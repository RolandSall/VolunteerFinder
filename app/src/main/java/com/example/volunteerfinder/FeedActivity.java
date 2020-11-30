package com.example.volunteerfinder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import com.example.volunteerfinder.model.User;
import com.google.gson.Gson;

public class FeedActivity extends AppCompatActivity {

    private TextView userInfoTextView;

    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        initSetup();

        User user = new Gson().fromJson(sp.getString("user", ""), User.class);

        String message = "You are: " + user.getFirstName();

        userInfoTextView.setText(message);
        
    }

    private void initSetup() {
        userInfoTextView = findViewById(R.id.userInfoTextView);

        sp = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);

    }
}