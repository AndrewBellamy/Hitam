package com.example.thean.hitam;

import android.accounts.AccountManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Home extends AppCompatActivity {

    //AccountManager am = AccountManager.get(this);
    //Bundle options = new Bundle();

    private static final int TAG_CLOUD = 1;
    private static final int SECTION_NAV = 2;
    private static final int INCOME_NAV = 3;
    private static final int ADD_DIALOG = 4;
    private static final int SUB_DIALOG = 5;

    DBControl local_db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Initialise DB
        local_db = new DBControl(this);
    }
}
