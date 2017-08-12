package com.example.thean.hitam;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Section extends AppCompatActivity {

    //constants
    private static final int ITEM_NAV = 2;

    private DBControl local_db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_section);


        local_db = new DBControl(this);
    }
}
