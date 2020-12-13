package com.example.volunteerfinder.activities.fragments.userLogin;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.volunteerfinder.R;
import com.example.volunteerfinder.activities.FeedActivity;
import com.example.volunteerfinder.services.user.IUserService;
import com.example.volunteerfinder.services.user.UserRegisterRequest;
import com.example.volunteerfinder.services.user.UserService;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

public class SignUp extends Fragment {

    private TextInputEditText firstNameField;
    private TextInputEditText lastNameField;
    private TextInputEditText addressField;
    private TextInputEditText emailField;
    private TextInputEditText passwordField;
    private TextInputLayout firstNameLayout;
    private TextInputLayout lastNameLayout;
    private TextInputLayout addressLayout;
    private TextInputLayout emailLayout;
    private TextInputLayout passwordLayout;


    private Button registerButton;

    private final IUserService userService = new UserService();

    SharedPreferences sp;

    public SignUp() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_signup, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initSetup();
        registerButton.setOnClickListener(event -> registerUser());
    }

    private void registerUser() {
        if (validData()) {
            try {
                userService.save(buildRequestToRegister(), user -> {
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("user", new Gson().toJson(user));
                    editor.commit();
                    Intent intent = new Intent(getActivity(), FeedActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                });
            } catch (Exception e) {
                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private UserRegisterRequest buildRequestToRegister() {
        return UserRegisterRequest.builder()
                .firstName(firstNameField.getText().toString())
                .lastName(lastNameField.getText().toString())
                .address(addressField.getText().toString())
                .password(passwordField.getText().toString())
                .email(emailField.getText().toString())
                .build();
    }


    private boolean validData() {
        if (firstNameField.getText().toString().equals("")) {
            firstNameLayout.setError("You must enter you first name");
            return false;
        }
        if (lastNameField.getText().toString().equals("")) {
            lastNameLayout.setError("You must enter your last name");
            return false;
        }
        if (emailField.getText().toString().equals("")) {
            emailLayout.setError("You must enter your email");
            return false;
        }
        if (addressField.getText().toString().equals("")) {
            addressLayout.setError("You must enter your address");
            return false;
        }
        if (passwordField.getText().toString().equals("")) {
            passwordLayout.setError("You must enter a password");
            return false;
        }
        return true;
    }

    private void initSetup() {
        firstNameField = getView().findViewById(R.id.newEventCapacity);
        lastNameField = getView().findViewById(R.id.lastNameField);
        emailField = getView().findViewById(R.id.newEventDescription);
        addressField = getView().findViewById(R.id.addressField);
        passwordField = getView().findViewById(R.id.passwordField);
        firstNameLayout = getView().findViewById(R.id.firstNameLayout);
        lastNameLayout = getView().findViewById(R.id.lastNameLayout);
        emailLayout = getView().findViewById(R.id.emailLayout);
        addressLayout = getView().findViewById(R.id.addressLayout);
        passwordLayout = getView().findViewById(R.id.passwordLayout);

        registerButton = getView().findViewById(R.id.loginButton);

        sp = getActivity().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);

    }
}