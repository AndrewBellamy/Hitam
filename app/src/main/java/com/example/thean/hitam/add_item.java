package com.example.thean.hitam;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

public class add_item extends AppCompatActivity {

    private DBControl local_db;

    EditText addItemName, addItemAmount, addItemFreq, addItemPriority;
    String addItemSection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        Intent intentAdd = getIntent();

        local_db = new DBControl(this);

        addItemAmount = (EditText) findViewById(R.id.addItemAmount);
        addItemName = (EditText) findViewById(R.id.addItemName);
        addItemFreq = (EditText) findViewById(R.id.addItemFreq);
        addItemPriority = (EditText) findViewById(R.id.addItemPriority);
        addItemSection = intentAdd.getStringExtra("sectionName");
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
                local_db.insertItem(addItemName.getText(), Float.parseFloat(String.valueOf(addItemAmount.getText())), Integer.parseInt(String.valueOf(addItemFreq.getText())), Integer.parseInt(String.valueOf(addItemPriority.getText())), addItemSection);
                if (getParent() == null) {
                    setResult(RESULT_OK);
                } else {
                    getParent().setResult(RESULT_OK);
                }
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
