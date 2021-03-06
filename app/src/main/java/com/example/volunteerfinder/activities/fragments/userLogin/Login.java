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
import com.example.volunteerfinder.activities.EventActivity;
import com.example.volunteerfinder.activities.FeedActivity;
import com.example.volunteerfinder.activities.LoginActivity;
import com.example.volunteerfinder.services.user.IUserService;
import com.example.volunteerfinder.services.user.UserLoginRequest;
import com.example.volunteerfinder.services.user.UserService;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;


public class Login extends Fragment {

    private TextInputEditText emailField;
    private TextInputEditText passwordField;
    private TextInputLayout emailLayout;
    private TextInputLayout passwordLayout;

    private Button loginButton;

    private final IUserService userService = new UserService();

    SharedPreferences sp;

    public Login() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initSetup();
        loginButton.setOnClickListener(event -> login());
    }

    private void login() {
        if (validData()) {
            userService.login(buildLoginRequest(), user -> {
                if(user != null) {
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("user", new Gson().toJson(user));
                editor.commit();
                    Intent intent = new Intent(getActivity(), FeedActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else {
                    emailLayout.setError("Might Be a Wrong Email");
                    passwordLayout.setError("Might Be a Wrong Password");
                }
            });
        }
    }

    private UserLoginRequest buildLoginRequest() {
        return new UserLoginRequest().builder()
                .password(passwordField.getText().toString())
                .email(emailField.getText().toString())
                .build();
    }

    private boolean validData() {
        if (emailField.getText().toString().equals("")) {
            emailLayout.setError("You must enter an email");
            return false;
        }
        if (passwordField.getText().toString().equals("")) {
            passwordLayout.setError("You must enter a password");
            return false;
        }
        return true;
    }

    private void initSetup() {
        emailField = getView().findViewById(R.id.newEventDescription);
        passwordField = getView().findViewById(R.id.passwordField);
        emailLayout = getView().findViewById(R.id.emailLayout);
        passwordLayout = getView().findViewById(R.id.passwordLayout);

        loginButton = getView().findViewById(R.id.loginButton);

        sp = getActivity().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);

    }
}