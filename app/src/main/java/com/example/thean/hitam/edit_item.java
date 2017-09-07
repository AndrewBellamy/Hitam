package com.example.thean.hitam;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

public class edit_item extends AppCompatActivity {

    EditText editItemName, editItemAmount, editItemFreq, editItemPriority;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        editItemAmount = (EditText) findViewById(R.id.editItemAmount);
        editItemName = (EditText) findViewById(R.id.editItemName);
        editItemFreq = (EditText) findViewById(R.id.editItemFreq);
        editItemPriority = (EditText) findViewById(R.id.editItemPriority);
    }

    //Setting the menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_item_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
            case R.id.update_item_action:
                return true;
            case R.id.delete_item_action:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
