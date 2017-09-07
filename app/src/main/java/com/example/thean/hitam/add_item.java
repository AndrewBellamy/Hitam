package com.example.thean.hitam;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

public class add_item extends AppCompatActivity {

    EditText addItemName, addItemAmount, addItemFreq, addItemPriority;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        addItemAmount = (EditText) findViewById(R.id.addItemAmount);
        addItemName = (EditText) findViewById(R.id.addItemName);
        addItemFreq = (EditText) findViewById(R.id.addItemFreq);
        addItemPriority = (EditText) findViewById(R.id.addItemPriority);
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
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
