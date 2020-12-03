package com.example.volunteerfinder.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.volunteerfinder.R;
import com.example.volunteerfinder.services.event.EventService;
import com.example.volunteerfinder.services.user.IUserService;
import com.example.volunteerfinder.services.user.UserService;
import com.example.volunteerfinder.services.user.RegisterUserResponse;
import com.example.volunteerfinder.models.User;
import com.google.gson.Gson;

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

    private IUserService iUserService = new UserService();
    private EventService eventService = new EventService();
    private User LoggedInUser;

    private Button loginAct;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initSetup();
        checkUser();

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (registerUser()) {
                    startActivity(new Intent(MainActivity.this, FeedActivity.class));
                    snackBarPop();
                }
            }
        });

        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        loginAct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
        });

    }

    private boolean registerUser() {
        if (validData()) {
            try {
                RegisterUserResponse response = iUserService.save(buildUserRequestToSignIn());
                LoggedInUser = getUserFromResponse(response);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("user", new Gson().toJson(LoggedInUser));
                editor.commit();
                return true;
            } catch (Exception e) {
                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        return false;
    }

    private void snackBarPop() {
        // Remove warnings
        firstNameText.setText("First Name");
        lastNameText.setText("Last Name");
        addressText.setText("Address");
        emailText.setText("Email");
        passwordText.setText("Password");


        /** TODO: Snack bar not showing **/
/*        Snackbar.make(parent, "You Are Registered!", Snackbar.LENGTH_INDEFINITE).setAction(
                "Dismiss", new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {

                    }
                }
        ).show();*/
    }

    private User getUserFromResponse(RegisterUserResponse response) {
        return new User().builder()
                .userId(response.getUserId())
                .firstName(firstNameField.getText().toString())
                .lastName(lastNameField.getText().toString())
                .address(addressField.getText().toString())
                .password(passwordField.getText().toString())
                .email(emailField.getText().toString())
                .build();
    }

    private User buildUserRequestToSignIn() {
        return new User().builder()
                .firstName(firstNameField.getText().toString())
                .lastName(lastNameField.getText().toString())
                .address(addressField.getText().toString())
                .password(passwordField.getText().toString())
                .email(emailField.getText().toString())
                .build();
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

        loginAct = findViewById(R.id.loginActivityBtn);
        secondLayout = findViewById(R.id.SecondLayout);
        parent = findViewById(R.id.parent);

        sp = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);

    }

    private void checkUser() {
        String userString = sp.getString("user", "");
        if (!userString.equals("")) {
            startActivity(new Intent(MainActivity.this, FeedActivity.class));
            finish();
        }
    }
}