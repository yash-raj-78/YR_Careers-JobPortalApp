package com.yash.jobportal;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.yash.jobportal.fragments.AppliedFragment;
import com.yash.jobportal.fragments.HomeFragment;
import com.yash.jobportal.fragments.JobsFragment;
import com.yash.jobportal.fragments.ProfileFragment;
import com.yash.jobportal.fragments.SavedFragment;


public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    TextView tvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNavigation);
        tvTitle = findViewById(R.id.tvTitle);

        SpannableString ss = new SpannableString("YR Careers");

        ss.setSpan(
                new ForegroundColorSpan(Color.parseColor("#003092")),
                0, 2,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        ss.setSpan(
                new ForegroundColorSpan(Color.parseColor("#FC4100")),
                3, 10,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        tvTitle.setText(ss);

        loadFragment(new HomeFragment());
        bottomNavigationView.setSelectedItemId(R.id.home);

        bottomNavigationView.setOnItemSelectedListener(menuItem -> {

            Fragment fragment;

            int id = menuItem.getItemId();

            if (id == R.id.home){
                fragment = new HomeFragment();

            } else if (id == R.id.jobs) {
                fragment = new JobsFragment();

            } else if (id == R.id.applied) {
                fragment = new AppliedFragment();

            } else if (id == R.id.saved) {
                fragment = new SavedFragment();

            } else {
                fragment = new ProfileFragment();

            }

            loadFragment(fragment);

            return true;
        });
    }

    private void loadFragment(Fragment fragment) {

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frameLayout,fragment)
                .commit();
    }
}