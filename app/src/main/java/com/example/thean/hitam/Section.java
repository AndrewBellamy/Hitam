package com.example.thean.hitam;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class Section extends AppCompatActivity {

    //constants
    private static final int ITEM_NAV = 2;

    private DBControl local_db;
    ArrayList<String> sections;
    ArrayAdapter<String> arrayAdapter;
    ListView sectionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_section);

        local_db = new DBControl(this);
        sectionList = (ListView) findViewById(R.id.sectionList);

        retrieveSections();
    }

    public void retrieveSections() {
        sections = local_db.getSectionData();
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, sections);
        if (sections.size() == 0) {
            sectionList.setAdapter(arrayAdapter);
        } else {
            sectionList.setAdapter(arrayAdapter);
            sectionList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String sectionName = sections.get(position);
                    Intent intent = new Intent(getApplicationContext(), Item.class);
                    intent.putExtra("selectedSection", sectionName);
                    startActivity(intent);
                }
            });
        }
    }
}
