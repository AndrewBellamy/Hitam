package com.example.thean.hitam;

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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class add_item extends AppCompatActivity {

    private DBControl local_db;

    EditText addItemName, addItemAmount, addItemFreq, addItemPriority;
    String addItemSection;
    Integer freqValue = 0;
    Integer prioValue = 0;

    AlertDialog.Builder builder;
    AlertDialog frequency;
    AlertDialog priority;

    View frequencyView;
    View priorityView;

    ListView dialogList;

    ArrayAdapter<String> adapter;

    utility hitamUtility;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        Intent intentAdd = getIntent();

        local_db = new DBControl(this);
        hitamUtility = new utility(this);

        addItemAmount = (EditText) findViewById(R.id.addItemAmount);
        addItemName = (EditText) findViewById(R.id.addItemName);
        addItemFreq = (EditText) findViewById(R.id.addItemFreq);
        addItemPriority = (EditText) findViewById(R.id.addItemPriority);
        addItemSection = intentAdd.getStringExtra("sectionName");

        //frequency selection dialog
        builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        frequencyView = inflater.inflate(R.layout.dialog_select_value, null);
        builder.setView(frequencyView)
                .setTitle(R.string.set_frequency_dialog_title);

        frequency = builder.create();

        //priority selection dialog
        builder = new AlertDialog.Builder(this);
        inflater = this.getLayoutInflater();
        priorityView = inflater.inflate(R.layout.dialog_select_value, null);
        builder.setView(priorityView)
                .setTitle(R.string.set_priority_dialog_title);

        priority = builder.create();
    }

    //Generate the frequency dialog list
    public void selectAddItemFrequency(View view) {
        //populate frequencies
        hitamUtility.setFrequencyList();

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
                        addItemFreq.setText((CharSequence) hitamUtility.values.get(position));
                        freqValue = hitamUtility.keys.get(position);
                        frequency.dismiss();
                    }
                });
            }
            }
        });
        frequency.show();
    }


    public void selectAddItemPriority(View view) {
        //populate priorities
        hitamUtility.setPriorityList();

        priority.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
            dialogList = (ListView) priorityView.findViewById(R.id.selectArrayList);
            adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.list_item, hitamUtility.values);
            if (hitamUtility.values.size() == 0) {
                dialogList.setAdapter(adapter);
            } else {
                dialogList.setAdapter(adapter);
                dialogList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        addItemPriority.setText((CharSequence) hitamUtility.values.get(position));
                        prioValue = hitamUtility.keys.get(position);
                        priority.dismiss();
                    }
                });
            }
            }
        });
        priority.show();
    }

    public void saveAddItem() {
        if(freqValue == 0 && prioValue == 0) {
            Toast.makeText(getApplicationContext(), R.string.add_item_validation, Toast.LENGTH_SHORT).show();
        } else {
            Float amountEntered = Float.parseFloat(String.valueOf(addItemAmount.getText()));
            Float amountSingular = amountEntered / freqValue;
            local_db.insertItem(addItemName.getText(), amountSingular, freqValue, prioValue, addItemSection);
            if (getParent() == null) {
                setResult(RESULT_OK);
            } else {
                getParent().setResult(RESULT_OK);
            }
            finish();
        }
    }

    //Setting the menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_item_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
            case R.id.insert_item_action:
                saveAddItem();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
