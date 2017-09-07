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

import java.util.ArrayList;

public class Section extends AppCompatActivity {

    //constants
    private static final int ITEM_NAV = 2;

    private DBControl local_db;
    ArrayList<String> sections;
    ArrayList<String> identifiers;
    ArrayAdapter<String> arrayAdapter;
    ListView sectionList;
    EditText sectionName;
    View editView;
    Integer editPosition;

    AlertDialog.Builder builder;
    AlertDialog dialog;

    AlertDialog.Builder builder_edit;
    AlertDialog dialog_edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_section);

        local_db = new DBControl(this);

        //Add dialog
        builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View addView = inflater.inflate(R.layout.dialog_add_section, null);
        builder.setView(addView)
        .setTitle(R.string.add_section_dialog_title)
        .setPositiveButton(R.string.add_section_dialog_affirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                //Insert new section name on positive
                sectionName = (EditText) addView.findViewById(R.id.section_name);
                local_db.insertSection(sectionName.getText());
                retrieveSections();
                dialog.cancel();
            }
        })
        .setNegativeButton(R.string.add_section_dialog_negate, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });

        dialog = builder.create();

        //Edit dialog
        builder_edit = new AlertDialog.Builder(this);
        editView = inflater.inflate(R.layout.dialog_add_section, null);
        builder_edit.setView(editView)
        .setTitle(R.string.edit_section_dialog_title)
        .setPositiveButton(R.string.edit_section_dialog_affirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                sectionName = (EditText) editView.findViewById(R.id.section_name);
                Log.i("id", String.valueOf(editPosition));
                local_db.updateSection(editPosition, sectionName.getText());
                retrieveSections();
                dialog.cancel();
            }
        })
        .setNegativeButton(R.string.edit_section_dialog_negate, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });

        dialog_edit = builder_edit.create();

        sectionList = (ListView) findViewById(R.id.sectionList);

        retrieveSections();
    }

    public void retrieveSections() {
        Bundle dataBundle = local_db.getSectionData();
        sections = dataBundle.getStringArrayList("sections");
        identifiers = dataBundle.getStringArrayList("ids");
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
            sectionList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    Log.i("id", identifiers.get(position));
                    if(id != 0) {
                        String sectionName = sections.get(position);
                        editPosition = Integer.parseInt(identifiers.get(position));
                        editSection(sectionName);
                    }
                    return true;
                }
            });
        }
    }

    public void addSection() {
        dialog.show();
    }

    public void editSection(final String selectedSection) {
        dialog_edit.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                sectionName = (EditText) editView.findViewById(R.id.section_name);
                sectionName.setText((CharSequence) selectedSection);
            }
        });
        dialog_edit.show();
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
