package com.example.volunteerfinder.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.volunteerfinder.R;
import com.example.volunteerfinder.adapters.EventAdapter;
import com.example.volunteerfinder.models.User;
import com.google.gson.Gson;

import java.util.ArrayList;

public class FeedActivity extends AppCompatActivity {

    private RecyclerView eventRecyclerView;

    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        initSetup();

        User user = new Gson().fromJson(sp.getString("user", ""), User.class);

        ArrayList<String> list = new ArrayList<>();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        eventRecyclerView.setLayoutManager(layoutManager);
        eventRecyclerView.setItemAnimator(new DefaultItemAnimator());
        eventRecyclerView.setAdapter(new EventAdapter(this, list));
        eventRecyclerView.addItemDecoration(new DividerItemDecoration(eventRecyclerView.getContext(), DividerItemDecoration.VERTICAL));

        list.add("Roland");
        list.add("Abdallah");
        list.add("Hadi");
    }

    private void initSetup() {
        eventRecyclerView = findViewById(R.id.eventRecyclerView);
        sp = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
    }
}