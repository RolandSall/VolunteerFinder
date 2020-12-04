package com.example.volunteerfinder.activities.fragments;

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
import com.example.volunteerfinder.models.User;
import com.example.volunteerfinder.services.user.IUserService;
import com.example.volunteerfinder.services.user.RegisterUserResponse;
import com.example.volunteerfinder.services.user.UserService;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

public class SignUp extends Fragment {

    private TextInputEditText firstNameField;
    private TextInputEditText lastNameField;
    private TextInputEditText addressField;
    private TextInputEditText emailField;
    private TextInputEditText passwordField;

    private Button registerButton;

    private final IUserService userService = new UserService();

    SharedPreferences sp;

    public SignUp() {}

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
                RegisterUserResponse response = userService.save(buildUserRequestToSignIn());
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("user", new Gson().toJson(getUserFromResponse(response)));
                editor.commit();
                startActivity(new Intent(getActivity(), FeedActivity.class));
                getActivity().finish();
            } catch (Exception e) {
                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private User getUserFromResponse(RegisterUserResponse response) {
        return User.builder()
                .userId(response.getUserId())
                .firstName(firstNameField.getText().toString())
                .lastName(lastNameField.getText().toString())
                .address(addressField.getText().toString())
                .password(passwordField.getText().toString())
                .email(emailField.getText().toString())
                .build();
    }

    private User buildUserRequestToSignIn() {
        return User.builder()
                .firstName(firstNameField.getText().toString())
                .lastName(lastNameField.getText().toString())
                .address(addressField.getText().toString())
                .password(passwordField.getText().toString())
                .email(emailField.getText().toString())
                .build();
    }

    private boolean validData() {
        if (firstNameField.getText().toString().equals("")) {
            return false;
        }
        if (lastNameField.getText().toString().equals("")) {
            return false;
        }
        if (emailField.getText().toString().equals("")) {
            return false;
        }
        if (addressField.getText().toString().equals("")) {
            return false;
        }
        if (passwordField.getText().toString().equals("")) {
            return false;
        }
        return true;
    }

    private void initSetup() {
        firstNameField = getView().findViewById(R.id.firstNameField);
        lastNameField = getView().findViewById(R.id.lastNameField);
        emailField = getView().findViewById(R.id.emailField);
        addressField = getView().findViewById(R.id.addressField);
        passwordField = getView().findViewById(R.id.passwordField);

        registerButton = getView().findViewById(R.id.loginButton);

        sp = getActivity().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);

    }
}