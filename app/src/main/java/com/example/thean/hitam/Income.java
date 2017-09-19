package com.example.thean.hitam;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Andrew Bellamy.
 * For Hitam | Assignment 2 SIT207
 * Student ID: 215240036
 */

public class Income extends AppCompatActivity {

    //Variables
    Integer freqValue, rowID;
    Date dateObject;
    //Controls
    EditText amountNumber, taxNumber, deductionNumber, startDate, freqSelect;
    ArrayAdapter<String> adapter;
    ListView dialogList;
    View frequencyView;
    //Dialog
    AlertDialog.Builder builder;
    AlertDialog frequency;
    DatePickerDialog datePickerDialog;
    //DB
    DBControl local_db;
    //Utilities
    utility hitamUtility;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income);

        //Setup database and utility classes
        local_db = new DBControl(this);
        hitamUtility = new utility(this);

        //Create control delegates
        amountNumber = (EditText) findViewById(R.id.amountNumber);
        taxNumber = (EditText) findViewById(R.id.taxNumber);
        deductionNumber = (EditText) findViewById(R.id.deductionNumber);
        startDate = (EditText) findViewById(R.id.startDate);
        freqSelect = (EditText) findViewById(R.id.incomeFrequency);

        //populate array lists for frequency
        hitamUtility.setFrequencyList();

        //Set frequency
        builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        frequencyView = inflater.inflate(R.layout.dialog_select_value, null);
        builder.setView(frequencyView)
                .setTitle(R.string.set_frequency_dialog_title);
        frequency = builder.create();

        //Finally
        retrieveIncome();
    }

    /**
     * Handles call to DB for income data, and populates the appropriate Controls. Refers to utility
     * class for handling key:value pairs. Also Initialises the datePickerDialog.
     * @return
     */
    public boolean retrieveIncome() {
        //Get the income data
        Bundle incomeBundle = local_db.getIncomeData();
        amountNumber.setText((CharSequence) String.valueOf(incomeBundle.getFloat("amount")));
        taxNumber.setText((CharSequence) String.valueOf(incomeBundle.getFloat("tax")));
        deductionNumber.setText((CharSequence) String.valueOf(incomeBundle.getFloat("deduction")));
        freqValue = incomeBundle.getInt("frequency");
        Long startDateLong = incomeBundle.getLong("startDate");
        rowID = incomeBundle.getInt("identifier");

        //Set the integer to frequency string
        if(hitamUtility.values.size() > 0) {
            freqSelect.setText((CharSequence) hitamUtility.values.get(hitamUtility.keys.indexOf(freqValue)));
        } else {
            freqSelect.setText((CharSequence) "");
        }

        //Create the date object and dialog
        dateObject = new Date(startDateLong);
        final Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(startDateLong);
        startDate.setText((CharSequence) String.valueOf(calendar.get(Calendar.DAY_OF_MONTH) + "/" + (calendar.get(Calendar.MONTH)+1) + "/" + calendar.get(Calendar.YEAR)));

        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                startDate.setText((CharSequence) String.valueOf(dayOfMonth) + "/" + String.valueOf(month+1) + "/" + String.valueOf(year));
                calendar.set(year, month, dayOfMonth);
                dateObject = new Date(calendar.getTimeInMillis());
                datePickerDialog.dismiss();
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        return true;
    }

    /**
     * Handles the call to DB to update the income data set. Also deletes all previous spending.
     * @return
     */
    public boolean saveIncome() {
        Bundle incomeBundle = new Bundle();
        incomeBundle.putFloat("amount", Float.parseFloat(String.valueOf(amountNumber.getText())));
        incomeBundle.putFloat("tax", Float.parseFloat(String.valueOf(taxNumber.getText())));
        incomeBundle.putFloat("deduction", Float.parseFloat(String.valueOf(deductionNumber.getText())));
        incomeBundle.putInt("frequency", freqValue);
        incomeBundle.putLong("startDate", dateObject.getTime());
        incomeBundle.putInt("identifier", rowID);
        local_db.deleteSpend();
        return local_db.updateIncome(incomeBundle);
    }

    /**
     * Calls the datePickerDialog show method.
     * @param view
     */
    public void selectStartDate(View view) {
        datePickerDialog.show();
    }

    /**
     * Uses utility to set the ArrayList, and adapt to the ListView in the selection dialog. Uses the
     * dialog's onShowListener to adjust the dialog before display
     * @param view
     */
    public void selectFrequency(View view) {
        frequency.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
            dialogList = (ListView) frequencyView.findViewById(R.id.selectArrayList);
            adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.list_item, hitamUtility.values);
            if (hitamUtility.values.size() == 0) {
                dialogList.setAdapter(adapter);
            } else {
                dialogList.setAdapter(adapter);
                dialogList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        freqSelect.setText((CharSequence) hitamUtility.values.get(position));
                        freqValue = hitamUtility.keys.get(position);
                        frequency.dismiss();
                    }
                });
            }
            }
        });
        frequency.show();
    }

    //Setting the menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.income_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
            case R.id.save_action:
                saveIncome();
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
