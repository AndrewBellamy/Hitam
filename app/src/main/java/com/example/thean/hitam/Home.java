package com.example.thean.hitam;

import android.accounts.AccountManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Home extends AppCompatActivity {

    //AccountManager am = AccountManager.get(this);
    //Bundle options = new Bundle();

    private static final int TAG_CLOUD = 1;
    private static final int SECTION_NAV = 2;
    private static final int INCOME_NAV = 3;
    private static final int ADD_DIALOG = 4;
    private static final int SUB_DIALOG = 5;

    DBControl local_db;
    Button add_button, sub_button, income_button, commit_button;
    TextView float_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Initialise DB
        local_db = new DBControl(this);

        commit_button = (Button) findViewById(R.id.commitButton);

        float_text = (TextView) findViewById(R.id.floatOutput);
        float_text.setText((CharSequence) "no data");
    }

    /**
     * Navigates to the commitment listView activity
     * @param view
     */
    public void navigateToCommitments(View view) {
        Intent intentCommit = new Intent(this, Section.class);
        startActivity(intentCommit);
    }

    /**
     * Navigates to the income form
     * @param view
     */
    public void navigateToIncome(View view) {
        Intent intentCommit = new Intent(this, Income.class);
        startActivity(intentCommit);
    }

    /**
     * Cancel arrows for all activities leading off from home.
     *
     * @param keycode
     * @param event
     * @return
     */
    public boolean onKeyDown(int keycode, KeyEvent event) {
        if (keycode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(true);
        }
        return super.onKeyDown(keycode, event);
    }
}
