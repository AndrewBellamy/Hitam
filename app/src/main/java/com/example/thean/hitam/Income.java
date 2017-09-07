package com.example.thean.hitam;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.Calendar;
import java.util.Date;

public class Income extends AppCompatActivity {


    EditText amountNumber, taxNumber, deductionNumber, startDate;
    Spinner freqSelect;
    ArrayAdapter<CharSequence> adapter;
    DBControl local_db;
    Date dateObject;
    Integer rowID;

    DatePickerDialog datePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income);

        local_db = new DBControl(this);

        amountNumber = (EditText) findViewById(R.id.amountNumber);
        taxNumber = (EditText) findViewById(R.id.taxNumber);
        deductionNumber = (EditText) findViewById(R.id.deductionNumber);
        startDate = (EditText) findViewById(R.id.startDate);

        freqSelect = (Spinner) findViewById(R.id.feqSelect);
        adapter = ArrayAdapter.createFromResource(this, R.array.frequency_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        freqSelect.setAdapter(adapter);

        retrieveIncome();
    }

    public boolean retrieveIncome() {
        Bundle incomeBundle = local_db.getIncomeData();
        amountNumber.setText((CharSequence) String.valueOf(incomeBundle.getFloat("amount")));
        taxNumber.setText((CharSequence) String.valueOf(incomeBundle.getFloat("tax")));
        deductionNumber.setText((CharSequence) String.valueOf(incomeBundle.getFloat("deduction")));
        Integer frequency = incomeBundle.getInt("frequency");
        Long startDateLong = incomeBundle.getLong("startDate");
        dateObject = new Date(startDateLong);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(startDateLong);
        freqSelect.setSelection(frequency);
        startDate.setText((CharSequence) String.valueOf(calendar.get(Calendar.DAY_OF_MONTH) + "/" + calendar.get(Calendar.MONTH) + "/" + calendar.get(Calendar.YEAR)));
        rowID = incomeBundle.getInt("identifier");

        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        return true;
    }

    public boolean saveIncome() {
        Bundle incomeBundle = new Bundle();
        incomeBundle.putFloat("amount", Float.parseFloat(String.valueOf(amountNumber.getText())));
        incomeBundle.putFloat("tax", Float.parseFloat(String.valueOf(taxNumber.getText())));
        incomeBundle.putFloat("deduction", Float.parseFloat(String.valueOf(deductionNumber.getText())));
        incomeBundle.putInt("frequency", freqSelect.getSelectedItemPosition());
        incomeBundle.putLong("startDate", dateObject.getTime());
        incomeBundle.putInt("identifier", rowID);
        return local_db.updateIncome(incomeBundle);
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
