package com.example.volunteerfinder.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
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
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;
import java.util.function.Consumer;

public class NewEventActivity extends AppCompatActivity implements OnMapReadyCallback {


    private Organization organization;

    private static final int CHOOSE_IMAGE_REQUEST = 2;
    private ProgressBar progressBar;

    private StorageReference mStorageRef;
    private StorageTask uploadTask;
    private DatabaseReference dbReference;
    private Uri mImageUri;

    private EventService eventService = new EventService();

    private MapView mapView;
    private GoogleMap gmap;

    private static final String MAP_VIEW_BUNDLE_KEY = "AIzaSyBUFJoVjD211sA6zlGPh97AxlN0s8biTuQ";

    private ImageButton mPickDateButton;
    private TextView mShowSelectedDateText;

    private ImageView eventImage;
    private TextView eventTitle;
    private TextView eventCapacity;
    private TextView eventDescription;
    private Button createEventButton;
    private Address eventLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_event);
        initSetup();

        initMap(savedInstanceState);
        initDatePicker();

        eventImage.setOnClickListener(view -> openGallery());

        createEventButton.setOnClickListener(e -> createEvent());

    }

    private void createEvent(){
        if(IsInvalidDownload()){
            Toast.makeText(NewEventActivity.this, "Event Is Being Generated", Toast.LENGTH_LONG).show();
        }else {
            uploadFile(imageUri -> {
                eventService.saveEvent(buildEvent(imageUri));
                finish();
            });
        }
    }

    private void initMap(Bundle savedInstanceState) {

        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY);
        }

        mapView.onCreate(mapViewBundle);
        mapView.getMapAsync(this);
    }

    private void initDatePicker() {
        MaterialDatePicker.Builder materialDateBuilder = MaterialDatePicker.Builder.datePicker();

        materialDateBuilder.setTitleText("SELECT A DATE");
        final MaterialDatePicker materialDatePicker = materialDateBuilder.build();

        mPickDateButton.setOnClickListener(v -> materialDatePicker.show(getSupportFragmentManager(), "DATE_PICKER"));

        materialDatePicker.addOnPositiveButtonClickListener(
                selection -> {
                    mShowSelectedDateText.setVisibility(View.VISIBLE);
                    mShowSelectedDateText.setText(materialDatePicker.getHeaderText());

                    showTimePicker("End Time");
                    showTimePicker("Start Time");

                });
    }


    private void showTimePicker(String time) {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(NewEventActivity.this,
                (timePicker, i, i1) ->
                        mShowSelectedDateText
                        .setText(mShowSelectedDateText.getText() + " - " + " " + i + ":" + i1),
                hour, minute, true
        );//Yes 24 hour time
        mTimePicker.setTitle(time);
        mTimePicker.show();
    }

    private void initSetup() {

        organization = (Organization) getIntent().getSerializableExtra("organization");

        eventImage = findViewById(R.id.newEventImage);
        eventTitle = findViewById(R.id.newEventTitle);
        eventCapacity = findViewById(R.id.newEventCapacity);
        eventDescription = findViewById(R.id.newEventDescription);

        createEventButton = findViewById(R.id.createEventButton);

        progressBar = findViewById(R.id.progressBar);
        mapView = findViewById(R.id.mapView);

        mPickDateButton = findViewById(R.id.dateTimePicker);
        mShowSelectedDateText = findViewById(R.id.datesSelected);

        mStorageRef = FirebaseStorage.getInstance().getReference("Events");
        dbReference = FirebaseDatabase.getInstance().getReference().child("Uploads");

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
                handler.postDelayed(() -> {
                    //progressBar.setProgress(0);
                }, 500);
                // Toast when image download is finished
                Toast.makeText(NewEventActivity.this, "Event Created", Toast.LENGTH_LONG).show();

                fileReference.getDownloadUrl().addOnSuccessListener(uri ->consumer.accept(uri.toString()));

            })
                    .addOnFailureListener(e -> Toast.makeText(NewEventActivity.this, e.getMessage(), Toast.LENGTH_LONG).show())

                    .addOnProgressListener(snapshot -> {
                        double progress = generateProgressForImageDownload(snapshot);
                        //progressBar.setProgress((int) progress);
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
            assert data != null;
            mImageUri = data.getData();
            Picasso.get().load(mImageUri).into(eventImage);
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

    private Event buildEvent(String imageUri) {
        return  Event.builder()
                .capacity(Integer.parseInt(eventCapacity.getText().toString()))
                .organization(organization)
                .description(eventDescription.getText().toString())
                .location(eventLocation.getAddressLine(0))
                .title(eventTitle.getText().toString())
                .eventDate(mShowSelectedDateText.getText().toString())
                .postedDate(new SimpleDateFormat("dd-MM-yyyy").format(new Date(System.currentTimeMillis())))
                .image(imageUri)
                .build();
    }

    // Map

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
        LatLng ny = new LatLng(40.7143528, -74.0059731);
        gmap.moveCamera(CameraUpdateFactory.newLatLng(ny));
        gmap.addMarker(new MarkerOptions().position(ny).draggable(true).title("Event Location"));
        gmap.moveCamera(CameraUpdateFactory.newLatLng(ny));
        gmap.getUiSettings().setZoomControlsEnabled(true);

        gmap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {
            }

            @Override
            public void onMarkerDrag(Marker marker) {
            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                LatLng latLng = marker.getPosition();
                Geocoder geocoder = new Geocoder(NewEventActivity.this, Locale.getDefault());
                try {
                    eventLocation = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1).get(0);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}