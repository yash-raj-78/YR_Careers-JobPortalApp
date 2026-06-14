package com.yash.jobportal.activites;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.yash.jobportal.MainActivity;
import com.yash.jobportal.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(FirebaseAuth.getInstance().getCurrentUser()!=null){
                    startActivity(new Intent(SplashActivity.this,MainActivity.class));
                }
                else{
                    startActivity(new Intent(SplashActivity.this,LoginActivity.class));
                }

                finish();
            }
        },3000);

    }
}