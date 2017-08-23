package com.example.thean.hitam;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class Item extends AppCompatActivity {

    //constants
    private static final int ITEM_NAV = 2;

    private DBControl local_db;
    String section;
    ArrayList<String> items;
    ArrayAdapter<String> arrayAdapter;
    ListView itemList;
    TextView sectionName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items);

        section = getIntent().getStringExtra("selectedSection");

        sectionName = (TextView) findViewById(R.id.sectionName);
        sectionName.setText(section);


        local_db = new DBControl(this);
        itemList = (ListView) findViewById(R.id.itemList);

        retrieveItems();
    }

    public void retrieveItems() {
        items = local_db.getItemData(section);
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
        if (items.size() == 0) {
            itemList.setAdapter(arrayAdapter);
        } else {
            itemList.setAdapter(arrayAdapter);
        }
    }

    //Setting the menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.item_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
            case R.id.add_item_action:
                Intent addItem = new Intent(this, add_item.class);
                startActivity(addItem);
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
