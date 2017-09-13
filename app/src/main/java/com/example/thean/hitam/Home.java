package com.example.thean.hitam;

import android.accounts.AccountManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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
            String formatResult = String.format("%.2f", result);
            float_text.setText((CharSequence) formatResult);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == SECTION_NAV) {
            getBalance();
        }
        if(requestCode == INCOME_NAV) {
            getBalance();
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
