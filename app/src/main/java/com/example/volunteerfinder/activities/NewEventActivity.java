package com.example.volunteerfinder.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.volunteerfinder.R;
import com.example.volunteerfinder.models.Event;
import com.example.volunteerfinder.models.Organization;
import com.example.volunteerfinder.services.event.EventService;
import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class NewEventActivity extends AppCompatActivity implements OnMapReadyCallback {


    private Organization organization;

    private static final int CHOOSE_IMAGE_REQUEST = 2;
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


    private MapView mapView;
    private GoogleMap gmap;

    private static final String MAP_VIEW_BUNDLE_KEY = "AIzaSyBUFJoVjD211sA6zlGPh97AxlN0s8biTuQ";

    private ImageButton mPickDateButton;

    private TextView mShowSelectedDateText;

    private ImageView Gallery;
    private Button chooseFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_event);
        initSetup();

        mPickDateButton = findViewById(R.id.imageButton);
        mShowSelectedDateText = findViewById(R.id.datesSelected);
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY);
        }


        Gallery = findViewById(R.id.gallery);
        mapView = findViewById(R.id.mapView);
        mapView.onCreate(mapViewBundle);
        mapView.getMapAsync(this);
        MaterialDatePicker.Builder materialDateBuilder = MaterialDatePicker.Builder.datePicker();

        chooseFile = findViewById(R.id.chooseFileBtn);
        materialDateBuilder.setTitleText("SELECT A DATE");
        final MaterialDatePicker materialDatePicker = materialDateBuilder.build();

        mPickDateButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        materialDatePicker.show(getSupportFragmentManager(), "DATE_PICKER");
                    }
                });

        Gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });


        materialDatePicker.addOnPositiveButtonClickListener(
                new MaterialPickerOnPositiveButtonClickListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onPositiveButtonClick(Object selection) {

                        // if the user clicks on the positive
                        // button that is ok button update the
                        // selected date
                        mShowSelectedDateText.setVisibility(View.VISIBLE);
                        mShowSelectedDateText.setText(materialDatePicker.getHeaderText());

                        // in the above statement, getHeaderText
                        // will return selected date preview from the
                        // dial


                        showTimePicker();
                        showTimePicker2();
                    }
                });
//        uploadBtn.setOnClickListener(v -> {
//            if (IsInvalidDownload()) {
//                Toast.makeText(NewEventActivity.this, "Image Is Already Being Downloaded", Toast.LENGTH_LONG).show();
//            } else {
//                uploadFile(uri -> event.setImage(uri));
//            }
//        });
    }


    private void showTimePicker() {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(NewEventActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                mShowSelectedDateText.setText(mShowSelectedDateText.getText() + " " + i + ":" + i1);
            }

        }, hour, minute, true);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }

    private void showTimePicker2() {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(NewEventActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                mShowSelectedDateText.setText(mShowSelectedDateText.getText() + " " + i + ":" + i1);
            }

        }, hour, minute, true);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }
    private void initSetup() {


//        organization = (Organization) getIntent().getSerializableExtra("organization");
//
//        uploadBtn = findViewById(R.id.uploadBtn);
//        chooseFileBtn = findViewById(R.id.chooseFileBtn);
//        progressBar = findViewById(R.id.progressBar);
//        imageView = findViewById(R.id.imageView);
//        save = findViewById(R.id.saveEventBtn);
//
//        event = Event.builder()
//                .capacity(10)
//                .organization(organization)
//                .description("Latest Version of Dummy Events")
//                .location("Amchit")
//                .title("Together-Stronger")
//                .eventDate("12-25-2020")
//                .postedDate("11-30-2020")
//                .build();
//
//        mStorageRef = FirebaseStorage.getInstance().getReference("Events");
//        dbReference = FirebaseDatabase.getInstance().getReference().child("Uploads");



    }


    private void openGallery() {
        // Navigate to gallery and restrict only image type to appear
        Intent intent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
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
            Picasso.get().load(mImageUri).into(Gallery);
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

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);


        Bundle mapViewBundle = outState.getBundle(MAP_VIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAP_VIEW_BUNDLE_KEY, mapViewBundle);
        }

        mapView.onSaveInstanceState(mapViewBundle);
    }
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }
    @Override
    protected void onPause() {
        mapView.onPause();
        super.onPause();
    }
    @Override
    protected void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        gmap = googleMap;
        gmap.setMinZoomPreference(12);
        LatLng ny = new LatLng(40.7143528, -74.0059731);
        gmap.moveCamera(CameraUpdateFactory.newLatLng(ny));
    }

}