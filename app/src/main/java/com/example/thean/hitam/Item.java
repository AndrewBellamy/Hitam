package com.example.thean.hitam;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class Item extends AppCompatActivity {

    //Constants
    private static final int EDIT_ITEM = 1;
    private static final int ADD_ITEM = 2;

    private DBControl local_db;
    String section;
    ArrayList<String> items;
    ArrayList<String> identifiers;
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
        Bundle dataBundle = local_db.getItemData(section);
        items = dataBundle.getStringArrayList("items");
        identifiers = dataBundle.getStringArrayList("ids");
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
        if (items.size() == 0) {
            itemList.setAdapter(arrayAdapter);
        } else {
            itemList.setAdapter(arrayAdapter);
            itemList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Integer itemIdentity = Integer.parseInt(identifiers.get(position));
                    Intent intent = new Intent(getApplicationContext(), edit_item.class);
                    intent.putExtra("selectedItem", itemIdentity);
                    startActivity(intent);
                }
            });
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
                addItem.putExtra("sectionName", section);
                startActivityForResult(addItem, ADD_ITEM);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == ADD_ITEM) {
            if(resultCode == RESULT_OK) {
                retrieveItems();
            }
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
