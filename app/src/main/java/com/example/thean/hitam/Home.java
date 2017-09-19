package com.example.thean.hitam;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Andrew Bellamy.
 * For Hitam | Assignment 2 SIT207
 * Student ID: 215240036
 */

public class Home extends AppCompatActivity {

    //Constants
    private static final int SECTION_NAV = 2;
    private static final int INCOME_NAV = 3;

    //Global variables
    DBControl local_db;
    static FBControl hitamFB;
    //Controls
    TextView float_text;
    //Dialogs
    View spendingView;
    AlertDialog.Builder builder;
    AlertDialog spending;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Initialise DB
        local_db = new DBControl(this);

        float_text = (TextView) findViewById(R.id.floatOutput);
        float_text.setText((CharSequence) "no data");

        //Initialise Firebase
        hitamFB = new FBControl(this);

        //Set dialog for spending
        builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        spendingView = inflater.inflate(R.layout.dialog_spend_adjust, null);
        builder.setView(spendingView)
                .setPositiveButton(R.string.spend_positive, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Integer take;
                        EditText amount = (EditText) spendingView.findViewById(R.id.spendAmount);
                        CheckBox takeCheck = (CheckBox) spendingView.findViewById(R.id.spendTake);

                        if (takeCheck.isChecked()) {
                            take = 0;
                        } else {
                            take = 1;
                        }

                        local_db.insertSpend(take, Float.parseFloat(String.valueOf(amount.getText())));
                        getBalance();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(R.string.spend_negative, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });;
        spending = builder.create();

        //Finally
        getBalance();
    }

    /**
     * Extends the AsyncTask for calculating the balance and setting the result in the EditText
     * No parameters, uses DB and globals
     */
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

            Float spendAdd = local_db.getSpendAddTotal();
            Float spendTake = local_db.getSpendTakeTotal();

            commitments = itemSum * frequencyPosition;

            incomeAfter = (income - tax) - deductions;

            balance = incomeAfter - commitments - spendTake + spendAdd;

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

    /**
     * Extends the AsyncTask to handle calculating and pushing balance up to Firebase
     * No parameters, uses DB, Globals and FB
     */
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

            Float spendAdd = local_db.getSpendAddTotal();
            Float spendTake = local_db.getSpendTakeTotal();

            commitments = itemSum * frequencyPosition;

            incomeAfter = (income - tax) - deductions;

            calcBalance = incomeAfter - commitments - spendTake + spendAdd;

            return Double.parseDouble(String.valueOf(calcBalance));
        }

        @Override
        protected void onPostExecute(Double result) {
            //To delete timestamp from long
            Date today  = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(today.getTime());
            calendar.set(Calendar.HOUR, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            calendar.set(Calendar.HOUR_OF_DAY, 0);

            Long date = calendar.getTimeInMillis();

            hitamFB.writeNewEntry(result, date);
        }
    }

    /**
     * Handles CalculateBalance instance creation and execution
     */
    public void getBalance() {
        CalculateBalance task = new CalculateBalance();
        task.execute();
    }

    /**
     * Displays the spending dialog, refreshes the EditText
     * @param view
     */
    public void onSpend(View view) {
        spending.setOnShowListener(new DialogInterface.OnShowListener() {
               @Override
               public void onShow(DialogInterface dialogInterface) {
                   EditText spendAmount = (EditText) spendingView.findViewById(R.id.spendAmount);
                   spendAmount.setText((CharSequence) "");
               }
           });
        spending.show();
    }

    /**
     * Navigates to the Commitments activity
     * @param view
     */
    public void navigateToCommitments(View view) {
        Intent intentCommit = new Intent(this, Section.class);
        startActivityForResult(intentCommit, SECTION_NAV);
    }

    /**
     * Navigates to the Income activity
     * @param view
     */
    public void navigateToIncome(View view) {
        Intent intentCommit = new Intent(this, Income.class);
        startActivityForResult(intentCommit, INCOME_NAV);
    }

    /**
     * Checks conditions based on income start date and frequency interval, Firebase account sign in
     * before handling creation and execution of WriteBalance instance.
     * @param view
     */
    public void writeToFirebase(View view) {
        Date today = new Date();
        Calendar todayDate = Calendar.getInstance();
        todayDate.setTimeInMillis(today.getTime());
        if (compareDate(todayDate)) {
            if(hitamFB.authenticUserSet()) {
                WriteBalance task = new WriteBalance();
                task.execute();
            } else {
                Toast.makeText(this, R.string.firebase_no_user,
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, R.string.firebase_too_early,
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Compares the income date + frequency, to today's date. Returns true if current date is outside
     * of the interval. Used by writeToFirebase method.
     * @param currentDate
     * @return boolean
     */
    public boolean compareDate(Calendar currentDate) {
        Bundle incomeBundle = local_db.getIncomeData();
        Integer frequency = incomeBundle.getInt("frequency");
        Long startDateLong = incomeBundle.getLong("startDate");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(startDateLong);

        if(calendar.get(Calendar.YEAR) == currentDate.get(Calendar.YEAR) &&
        calendar.get(Calendar.MONTH) == currentDate.get(Calendar.MONTH) &&
                (calendar.get(Calendar.DAY_OF_MONTH) + frequency) <= currentDate.get(Calendar.DAY_OF_MONTH))
        {
            return true;
        }
        else {
            return false;
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
