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
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class NewEventActivity extends AppCompatActivity {


    private Organization organization;

    private static final int CHOOSE_IMAGE_REQUEST = 1;
    private Button uploadBtn;
    private Button chooseFileBtn;
    private Button save;
    private ProgressBar progressBar;
    private ImageView imageView;


    private StorageReference mStorageRef;
    private StorageTask uploadTask;
    private DatabaseReference dbReference;
    private Uri mImageUri;

    private Event event;

    private EventService eventService = new EventService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);
        initSetup();


        uploadBtn.setOnClickListener(v -> {
            if (IsInvalidDownload()) {
                Toast.makeText(NewEventActivity.this, "Image Is Already Being Downloaded", Toast.LENGTH_LONG).show();
            } else {
                uploadFile(uri -> event.setImage(uri));
            }
        });

        chooseFileBtn.setOnClickListener(v -> openGallery());

        save.setOnClickListener(v -> {
            eventService.saveEvent(event);
        });
    }


    private void initSetup() {


        organization = (Organization) getIntent().getSerializableExtra("organization");

        uploadBtn = findViewById(R.id.uploadBtn);
        chooseFileBtn = findViewById(R.id.chooseFileBtn);
        progressBar = findViewById(R.id.progressBar);
        imageView = findViewById(R.id.imageView);
        save = findViewById(R.id.saveEventBtn);

        event = Event.builder()
                .capacity(10)
                .organization(organization)
                .description("Latest Version of Dummy Events")
                .location("Amchit")
                .title("Together-Stronger")
                .eventDate("12-25-2020")
                .postedDate("11-30-2020")
                .build();

        mStorageRef = FirebaseStorage.getInstance().getReference("Events");
        dbReference = FirebaseDatabase.getInstance().getReference().child("Uploads");



    }


    private void openGallery() {
        // Navigate to gallery and restrict only image type to appear
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, CHOOSE_IMAGE_REQUEST);
    }


    private void uploadFile(Consumer<String> consumer) {
        if (mImageUri != null) {

            String uuid = UUID.randomUUID().toString();
            StorageReference fileReference = mStorageRef.child(uuid + "." + getFileExtension(mImageUri));

            uploadTask = fileReference.putFile(mImageUri).addOnSuccessListener(taskSnapshot -> {
                // Create a handler for incrementing a visible progress bar
                Handler handler = new Handler();
                handler.postDelayed(() -> progressBar.setProgress(0), 500);
                // Toast when image download is finished
                Toast.makeText(NewEventActivity.this, "Image Uploaded", Toast.LENGTH_LONG).show();

                fileReference.getDownloadUrl().addOnSuccessListener(uri ->consumer.accept(uri.toString()));

            })
                    .addOnFailureListener(e -> Toast.makeText(NewEventActivity.this, e.getMessage(), Toast.LENGTH_LONG).show())

                    .addOnProgressListener(snapshot -> {
                        double progress = generateProgressForImageDownload(snapshot);
                        progressBar.setProgress((int) progress);
                    });

        } else {
            Toast.makeText(this, "No File Has Been Selected", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        // if image valid -> show data in ImageView
        super.onActivityResult(requestCode, resultCode, data);
        if (isImageValid(requestCode, resultCode, data)) {
            mImageUri = data.getData();
            Picasso.get().load(mImageUri).into(imageView);
        }
    }

    private boolean isImageValid(int requestCode, int resultCode, @Nullable Intent data) {
        return requestCode == CHOOSE_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null;
    }

    private boolean IsInvalidDownload() {
        // check if image downloaded is null and if there is an ongoing previous download
        return uploadTask != null && uploadTask.isInProgress();
    }

    private String getFileExtension(Uri uri) {
        // used to get image extensions .jpg, .jpeg
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private double generateProgressForImageDownload(@NonNull UploadTask.TaskSnapshot snapshot) {
        return 100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount();
    }

    private Event buildEvent() {
        // dummy witch chosen Image
        System.out.println("OBJECT: " + organization);
        return event.builder()
                .capacity(10)
                .organization(organization)
                .description("Latest Version of Dummy Events")
                .location("Amchit")
                .title("Together-Stronger")
                .eventDate("12-25-2020")
                .postedDate("11-30-2020")
                .build();
    }
}