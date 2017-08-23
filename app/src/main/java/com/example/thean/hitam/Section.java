package com.example.thean.hitam;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
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

    AlertDialog.Builder builder;
    AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_section);

        local_db = new DBControl(this);

        builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.dialog_add_section, null))
        .setTitle(R.string.add_section_dialog_title)
        .setPositiveButton(R.string.add_section_dialog_affirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        })
        .setNegativeButton(R.string.add_section_dialog_negate, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });

        dialog = builder.create();

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

    public void addSection() {
        dialog.show();
    }

    //Setting the menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.section_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
            case R.id.add_section_action:
                addSection();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
