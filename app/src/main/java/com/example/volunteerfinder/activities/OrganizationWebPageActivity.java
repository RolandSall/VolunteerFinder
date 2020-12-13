package com.example.volunteerfinder.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;

import com.example.volunteerfinder.R;
import com.example.volunteerfinder.models.Organization;

public class OrganizationWebPageActivity extends AppCompatActivity {

    private WebView organizationWebPage;

    private Organization organization;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organization_web_page);
        organizationWebPage = findViewById(R.id.organizationWebpage);

        organization = (Organization)getIntent().getSerializableExtra("organization");

        organizationWebPage.loadUrl(organization.getWebPage());
    }
}