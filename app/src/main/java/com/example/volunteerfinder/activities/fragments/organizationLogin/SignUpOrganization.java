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
import com.example.volunteerfinder.activities.OrganizationProfileActivity;
import com.example.volunteerfinder.models.Organization;
import com.example.volunteerfinder.services.organization.IOrganizationService;
import com.example.volunteerfinder.services.organization.OrganizationService;
import com.example.volunteerfinder.services.organization.OrganizationRegisterRequest;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

public class SignUpOrganization extends Fragment {

    private TextInputEditText organizationNameField;
    private TextInputEditText webPageField;
    private TextInputEditText addressField;
    private TextInputEditText emailField;
    private TextInputEditText passwordField;
    private TextInputLayout organizationNameLayout;
    private TextInputLayout webPageLayout;
    private TextInputLayout addressLayout;
    private TextInputLayout emailLayout;
    private TextInputLayout passwordLayout;

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
                organizationService.save(buildOrganizationRequest(), organization -> {
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("organization", new Gson().toJson(organization));
                    editor.commit();
                    Intent intent = new Intent(getActivity(), OrganizationProfileActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                });
            } catch (Exception e) {
                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private OrganizationRegisterRequest buildOrganizationRequest() {
        return OrganizationRegisterRequest.builder()
                .address(addressField.getText().toString())
                .name(organizationNameField.getText().toString())
                .webPage(webPageField.getText().toString())
                .password(passwordField.getText().toString())
                .email(emailField.getText().toString())
                .build();
    }


    private boolean validData() {
        if (organizationNameField.getText().toString().equals("")) {
            organizationNameLayout.setError("You must enter your organization name");
            return false;
        }
        if (webPageField.getText().toString().equals("")) {
            webPageLayout.setError("You must enter your website");
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
        organizationNameField = getView().findViewById(R.id.nameField);
        webPageField = getView().findViewById(R.id.webPageField);
        emailField = getView().findViewById(R.id.emailField);
        addressField = getView().findViewById(R.id.addressField);
        passwordField = getView().findViewById(R.id.passwordField);
        organizationNameLayout = getView().findViewById(R.id.organizationWebpage);
        webPageLayout = getView().findViewById(R.id.webPageLayout);
        emailLayout = getView().findViewById(R.id.emailLayout);
        addressLayout = getView().findViewById(R.id.addressLayout);
        passwordLayout = getView().findViewById(R.id.passwordLayout);

        registerButton = getView().findViewById(R.id.signUpButton);

        sp = getActivity().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);

    }
}