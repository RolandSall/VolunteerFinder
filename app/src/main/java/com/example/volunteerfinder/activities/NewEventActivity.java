package com.example.volunteerfinder.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.volunteerfinder.R;
import com.example.volunteerfinder.models.Event;
import com.example.volunteerfinder.models.Organization;
import com.example.volunteerfinder.services.event.EventService;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.UUID;
import java.util.stream.Collectors;

public class NewEventActivity extends AppCompatActivity {

    private SharedPreferences sp;
    private Button createEventButton;
    private Organization organization;

    private static final int CHOOSE_IMAGE_REQUEST = 1;
    private Button uploadBtn;
    private Button chooseFileBtn;
    private ProgressBar progressBar;
    private ImageView imageView;

    private Uri mImageUri;

    private EventService eventService = new EventService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);

        initSetup();


        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



            }
        });

        chooseFileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Choose File Clicked");
                openGallery();
            }
        });
    }

    private void initSetup(){
        sp = getSharedPreferences("OrganizationInfo", Context.MODE_PRIVATE);

        uploadBtn = findViewById(R.id.uploadBtn);
        chooseFileBtn = findViewById(R.id.chooseFileBtn);
        progressBar = findViewById(R.id.progressBar);
        imageView = findViewById(R.id.imageView);




        organization = new Gson().fromJson(sp.getString("organization", ""), Organization.class);
        createEventButton = findViewById(R.id.createEventButton);

        createEventButton.setOnClickListener(e -> {
            eventService.createDummyEvent(organization);
            finish();
        });
    }


    private void openGallery(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,CHOOSE_IMAGE_REQUEST);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(isImageValid(requestCode, resultCode, data)){
            mImageUri = data.getData();

            Picasso.get().load(mImageUri).into(imageView);
        }
    }

    private boolean isImageValid(int requestCode, int resultCode, @Nullable Intent data) {
        return requestCode == CHOOSE_IMAGE_REQUEST && resultCode == RESULT_OK && data !=null && data.getData() !=null;
    }
}