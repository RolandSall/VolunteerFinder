package com.example.volunteerfinder.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.volunteerfinder.R;
import com.example.volunteerfinder.activities.fragments.organizationLogin.LoginOrganization;
import com.example.volunteerfinder.activities.fragments.organizationLogin.SignUpOrganization;
import com.example.volunteerfinder.activities.fragments.userLogin.Login;
import com.example.volunteerfinder.activities.fragments.userLogin.SignUp;
import com.example.volunteerfinder.models.Organization;
import com.example.volunteerfinder.services.organization.OrganizationService;
import com.example.volunteerfinder.services.organization.RegisterOrganizationRequest;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class LoginOrganizationActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private LoginOrganization loginFragment;
    private SignUpOrganization signUpFragment;

    private OrganizationService organizationService = new OrganizationService();
    SharedPreferences sp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_organization);

        initSetup();
        initFragments();
    }


    private class ViewPagerAdapter extends FragmentPagerAdapter {

        private List<Fragment> fragments = new ArrayList<>();
        private List<String> fragmentTitle = new ArrayList<>();

        public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        public void addFragment(Fragment fragment, String title) {
            fragments.add(fragment);
            fragmentTitle.add(title);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitle.get(position);
        }
    }

    private void initSetup(){
        sp = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewPager = findViewById(R.id.view_pager);
        tabLayout = findViewById(R.id.tab_layout);
    }

    private void initFragments(){
        loginFragment = new LoginOrganization();
        signUpFragment = new SignUpOrganization();

        tabLayout.setupWithViewPager(viewPager);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), 0);
        viewPagerAdapter.addFragment(loginFragment, "Login");
        viewPagerAdapter.addFragment(signUpFragment, "Sign Up");
        viewPager.setAdapter(viewPagerAdapter);
    }


}
