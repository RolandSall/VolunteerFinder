package com.example.volunteerfinder.activities.fragments.organizationLogin;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.volunteerfinder.R;
import com.example.volunteerfinder.activities.FeedActivity;
import com.example.volunteerfinder.activities.OrganizationProfileActivity;
import com.example.volunteerfinder.models.Organization;
import com.example.volunteerfinder.services.organization.IOrganizationService;
import com.example.volunteerfinder.services.organization.OrganizationService;
import com.example.volunteerfinder.services.organization.RegisterOrganizationRequest;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

public class SignUpOrganization extends Fragment {

    private TextInputEditText organizationNameField;
    private TextInputEditText webPageField;
    private TextInputEditText addressField;
    private TextInputEditText emailField;
    private TextInputEditText passwordField;

    private Button registerButton;

    private IOrganizationService organizationService = new OrganizationService();

    SharedPreferences sp;

    public SignUpOrganization() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_signup_organization, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initSetup();
        registerButton.setOnClickListener(event -> registerOrganization());
    }

    private void registerOrganization() {
        if (validData()) {
            try {
                Organization organization =  organizationService.save(buildOrganizationRequest());
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("organization", new Gson().toJson(organization));
                editor.commit();
                startActivity(new Intent(getActivity(), OrganizationProfileActivity.class));
                getActivity().finish();
            } catch (Exception e) {
                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private RegisterOrganizationRequest buildOrganizationRequest() {
        return RegisterOrganizationRequest.builder()
                .address(addressField.getText().toString())
                .name(organizationNameField.getText().toString())
                .webPage(webPageField.getText().toString())
                .password(passwordField.getText().toString())
                .build();
    }


    private boolean validData() {
        if (organizationNameField.getText().toString().equals("")) {
            return false;
        }
        if (webPageField.getText().toString().equals("")) {
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
        organizationNameField = getView().findViewById(R.id.nameField);
        webPageField = getView().findViewById(R.id.webPageField);
        emailField = getView().findViewById(R.id.emailField);
        addressField = getView().findViewById(R.id.emailField);
        passwordField = getView().findViewById(R.id.passwordField);

        registerButton = getView().findViewById(R.id.loginButton);

        sp = getActivity().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);

    }
}