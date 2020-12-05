package com.example.volunteerfinder.activities.fragments.organizationLogin;

import android.content.Context;
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
import com.example.volunteerfinder.models.Organization;
import com.example.volunteerfinder.services.organization.IOrganizationService;
import com.example.volunteerfinder.services.organization.OrganizationService;
import com.example.volunteerfinder.services.user.IUserService;
import com.example.volunteerfinder.services.user.UserService;
import com.google.android.material.textfield.TextInputEditText;

public class SignUpOrganization extends Fragment {

    private TextInputEditText firstNameField;
    private TextInputEditText lastNameField;
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
        return inflater.inflate(R.layout.fragment_signup, container, false);
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
               /* Organization organization =  organizationService.save(buildOrganizationRequest());*/
            } catch (Exception e) {
                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

/*    private RegisterOrganizationRequest buildOrganizationRequest() {
        // data comes from TextField i.e: address(addressTextField.getText().toString())
        return new RegisterOrganizationRequest().builder()
                .address()
                .name()
                .webPage()
                .password()
                .build();
    }
    */

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