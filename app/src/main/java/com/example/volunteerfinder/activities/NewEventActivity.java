package com.example.volunteerfinder.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.volunteerfinder.R;
import com.example.volunteerfinder.models.Event;
import com.example.volunteerfinder.models.Organization;
import com.example.volunteerfinder.services.event.EventService;
import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
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



    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    private StorageTask uploadTask;

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
                if(uploadTask !=null && uploadTask.isInProgress()) {
                    Toast.makeText(NewEventActivity.this,"Image Is Already Being Downloaded", Toast.LENGTH_LONG).show();
                } else{
                    uploadFile();

                }


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

        mStorageRef = FirebaseStorage.getInstance().getReference("uploads");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads");




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


    private String getFileExtension(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void uploadFile(){
        if(mImageUri != null){
            String  uuid = UUID.randomUUID().toString();
            StorageReference fileReference = mStorageRef.child(uuid+"."+getFileExtension(mImageUri));

            uploadTask = fileReference.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Handler handler = new Handler();
                    handler.postDelayed(() -> progressBar.setProgress(0), 500);

                    Toast.makeText(NewEventActivity.this,"Image Uploaded", Toast.LENGTH_LONG).show();
                    String downloadedImageUrl = fileReference.getDownloadUrl().toString();

                    System.out.println(downloadedImageUrl);

                    mDatabaseRef.child("Test").setValue(downloadedImageUrl);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(NewEventActivity.this,e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                    double progress = (100.0 * snapshot.getBytesTransferred()/snapshot.getTotalByteCount());
                    progressBar.setProgress((int) progress);
                }
            });

        }else {
            Toast.makeText(this,"No Selected File", Toast.LENGTH_LONG).show();
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(isImageValid(requestCode, resultCode, data)){
            mImageUri = data.getData();
            System.out.println(mImageUri);

            Picasso.get().load(mImageUri).into(imageView);
        }
    }

    private boolean isImageValid(int requestCode, int resultCode, @Nullable Intent data) {
        return requestCode == CHOOSE_IMAGE_REQUEST && resultCode == RESULT_OK && data !=null && data.getData() !=null;
    }
}