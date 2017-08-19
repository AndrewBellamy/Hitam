package com.example.thean.hitam;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.Date;

public class Income extends AppCompatActivity {


    EditText amountNumber, taxNumber, deductionNumber, startDate;
    Spinner freqSelect;
    ArrayAdapter<CharSequence> adapter;
    DBControl local_db;
    Date dateObject;
    Integer rowID;

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
        freqSelect.setSelection(frequency);
        startDate.setText((CharSequence) String.valueOf(dateObject));
        rowID = incomeBundle.getInt("identifier");
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
}
