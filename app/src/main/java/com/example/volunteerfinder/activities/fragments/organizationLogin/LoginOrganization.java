package com.example.volunteerfinder.activities.fragments.organizationLogin;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.volunteerfinder.R;
import com.example.volunteerfinder.activities.OrganizationProfileActivity;
import com.example.volunteerfinder.services.organization.IOrganizationService;
import com.example.volunteerfinder.services.organization.OrganizationService;
import com.example.volunteerfinder.services.user.UserLoginRequest;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;


public class LoginOrganization extends Fragment {

    private TextInputEditText emailField;
    private TextInputEditText passwordField;
    private TextInputLayout emailLayout;
    private TextInputLayout passwordLayout;

    private Button loginButton;

    private final IOrganizationService organizationService = new OrganizationService();

    SharedPreferences sp;

    public LoginOrganization() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login_organization, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initSetup();
        loginButton.setOnClickListener(event -> login());
    }

    private void login() {
        if(validData()) {
            organizationService.login(buildLoginRequest(), organization -> {
                if(organization != null) {
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("organization", new Gson().toJson(organization));
                    editor.commit();
                    Intent intent = new Intent(getActivity(), OrganizationProfileActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else{
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