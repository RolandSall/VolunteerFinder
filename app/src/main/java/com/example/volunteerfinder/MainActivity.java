package com.example.volunteerfinder;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private EditText firstNameField;
    private EditText lastNameField;
    private EditText emailField;
    private EditText addressField;
    private EditText passwordField;

    private TextView firstNameText;
    private TextView lastNameText;
    private TextView emailText;
    private TextView addressText;
    private TextView passwordText;

    private Button registerBtn;
    private Button signInBtn;

    private ConstraintLayout secondLayout;
    private ConstraintLayout parent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initSetup();
        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");
        myRef.setValue("Hello, World!");

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                regSetup();
            }
        });
    }

    private void regSetup() {
        if (validData()) {
            System.out.println("Valid");
            snackBarPop();
        }
    }

    private void snackBarPop() {
        // Remove warnings
        firstNameText.setText("First Name");
        lastNameText.setText("Last Name");
        addressText.setText("Address");
        emailText.setText("Email");
        passwordText.setText("Password");


         /** TODO: Snack bar not showing **/
        Snackbar.make(parent, "You Are Registered!", Snackbar.LENGTH_INDEFINITE);
    }

    private boolean validData() {
        if (firstNameField.getText().toString().equals("")) {
            firstNameText.setText("Enter Your First Name");
            return false;
        }
        if (lastNameField.getText().toString().equals("")) {
            lastNameText.setText("Enter Your Last Name");
            return false;
        }
        if (emailField.getText().toString().equals("")) {
            emailText.setText("Enter Your Email");
            return false;
        }
        if (addressField.getText().toString().equals("")) {
            addressText.setText("Enter Your Address");
            return false;
        }
        if (passwordField.getText().toString().equals("")) {
            passwordText.setText("Enter Your Password");
            return false;
        }
        return true;
    }

    private void initSetup() {
        firstNameField = findViewById(R.id.firstNameField);
        lastNameField = findViewById(R.id.lastNameField);
        emailField = findViewById(R.id.emailField);
        addressField = findViewById(R.id.addressField);
        passwordField = findViewById(R.id.passwordField);

        firstNameText = findViewById(R.id.firstNameText);
        lastNameText = findViewById(R.id.lastNameText);
        emailText = findViewById(R.id.emailText);
        addressText = findViewById(R.id.addressText);
        passwordText = findViewById(R.id.passwordText);

        registerBtn = findViewById(R.id.registerBtn);
        signInBtn = findViewById(R.id.signInBtn);

        secondLayout = findViewById(R.id.SecondLayout);
        parent = findViewById(R.id.parent);

    }
}