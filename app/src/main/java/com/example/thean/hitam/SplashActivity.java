package com.example.thean.hitam;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Andrew Bellamy on 12/08/2017.
 * For Hitam | Assignment 2 SIT207
 * Student ID: 215240036
 *
 * This class manages the initial startup splash with the app logo.
 */

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Start home activity
        startActivity(new Intent(SplashActivity.this, Home.class));
        // close splash activity
        finish();
    }
}
