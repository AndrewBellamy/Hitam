package com.example.thean.hitam;

import android.accounts.AccountManager;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;

public class Home extends AppCompatActivity {


    private static final int TAG_CLOUD = 1;
    private static final int SECTION_NAV = 2;
    private static final int INCOME_NAV = 3;
    private static final int ADD_DIALOG = 4;
    private static final int SUB_DIALOG = 5;

    DBControl local_db;
    TextView float_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Initialise DB
        local_db = new DBControl(this);

        float_text = (TextView) findViewById(R.id.floatOutput);
        float_text.setText((CharSequence) "no data");

        getBalance();
    }

    private class CalculateBalance extends AsyncTask<Void, Void, Float> {

        @Override
        protected Float doInBackground(Void... params) {
            Float balance = 0.00F;
            Float incomeAfter = 0.00F;
            Float commitments = 0.00F;

            Bundle incomeBundle = local_db.getIncomeData();
            Float income = incomeBundle.getFloat("amount");
            Float tax = incomeBundle.getFloat("tax");
            Float deductions = incomeBundle.getFloat("deduction");
            Integer frequencyPosition = incomeBundle.getInt("frequency");

            Float itemSum = local_db.getAllItemSum();
            commitments = itemSum * frequencyPosition;

            incomeAfter = (income - tax) - deductions;

            balance = incomeAfter - commitments;

            return balance;
        }

        @Override
        protected void onPostExecute(Float result) {
            if (result < 0) {
                float_text.setTextColor(Color.RED);
            } else {
                float_text.setTextColor(Color.BLACK);
            }
            String formatResult = String.format("%.2f", result);
            float_text.setText((CharSequence) formatResult);
        }
    }

    private class WriteBalance extends AsyncTask<Void, Void, Double> {

        @Override
        protected Double doInBackground(Void... params) {
            Float calcBalance = 0.00F;
            Float incomeAfter = 0.00F;
            Float commitments = 0.00F;

            Bundle incomeBundle = local_db.getIncomeData();
            Float income = incomeBundle.getFloat("amount");
            Float tax = incomeBundle.getFloat("tax");
            Float deductions = incomeBundle.getFloat("deduction");
            Integer frequencyPosition = incomeBundle.getInt("frequency");

            Float itemSum = local_db.getAllItemSum();
            commitments = itemSum * frequencyPosition;

            incomeAfter = (income - tax) - deductions;

            calcBalance = incomeAfter - commitments;

            Double balance = Double.parseDouble(String.valueOf(calcBalance));


            return balance;
        }

        @Override
        protected void onPostExecute(Double result) {
            Date today  = new Date();
            Long date = today.getTime();

            Preferences.hitamFB.writeNewEntry(result, date);
        }
    }

    public void getBalance() {
        CalculateBalance task = new CalculateBalance();
        task.execute();
    }

    /**
     * Navigates to the commitment listView activity
     * @param view
     */
    public void navigateToCommitments(View view) {
        Intent intentCommit = new Intent(this, Section.class);
        startActivityForResult(intentCommit, SECTION_NAV);
    }

    /**
     * Navigates to the income form
     * @param view
     */
    public void navigateToIncome(View view) {
        Intent intentCommit = new Intent(this, Income.class);
        startActivityForResult(intentCommit, INCOME_NAV);
    }

    public void writeToFirebase(View view) {
        if(Preferences.hitamFB.authenticUserSet()) {
            WriteBalance task = new WriteBalance();
            task.execute();
        } else {
            Toast.makeText(this, R.string.firebase_no_user,
                    Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == SECTION_NAV) {
            getBalance();
        }
        if(requestCode == INCOME_NAV) {
            getBalance();
        }
    }

    //Setting the menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
            case R.id.preferences_nav:
                Intent preferences = new Intent(this, Preferences.class);
                startActivity(preferences);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
