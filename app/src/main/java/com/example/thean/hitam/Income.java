package com.example.thean.hitam;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Income extends AppCompatActivity {

    //Variables
    EditText amountNumber, taxNumber, deductionNumber, startDate, freqSelect;
    Integer freqValue, rowID;
    ArrayAdapter<String> adapter;
    DBControl local_db;
    Date dateObject;
    ListView dialogList;
    View frequencyView;

    AlertDialog.Builder builder;
    AlertDialog frequency;

    DatePickerDialog datePickerDialog;
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
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(startDateLong);
        startDate.setText((CharSequence) String.valueOf(calendar.get(Calendar.DAY_OF_MONTH) + "/" + calendar.get(Calendar.MONTH) + "/" + calendar.get(Calendar.YEAR)));

        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                startDate.setText((CharSequence) String.valueOf(dayOfMonth) + "/" + String.valueOf(month) + "/" + String.valueOf(year));
                datePickerDialog.dismiss();
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        return true;
    }

    public boolean saveIncome() {
        Bundle incomeBundle = new Bundle();
        incomeBundle.putFloat("amount", Float.parseFloat(String.valueOf(amountNumber.getText())));
        incomeBundle.putFloat("tax", Float.parseFloat(String.valueOf(taxNumber.getText())));
        incomeBundle.putFloat("deduction", Float.parseFloat(String.valueOf(deductionNumber.getText())));
        incomeBundle.putInt("frequency", freqValue);
        incomeBundle.putLong("startDate", dateObject.getTime());
        incomeBundle.putInt("identifier", rowID);
        return local_db.updateIncome(incomeBundle);
    }

    //Dialog Methods
    public void selectStartDate(View view) {
        datePickerDialog.show();
    }

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
