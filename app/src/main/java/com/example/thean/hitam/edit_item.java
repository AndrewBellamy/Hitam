package com.example.thean.hitam;

import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class edit_item extends AppCompatActivity {

    EditText editItemName, editItemAmount, editItemFreq, editItemPriority;
    String section;

    Integer freqValue = 0;
    Integer prioValue = 0;

    AlertDialog.Builder builder;
    AlertDialog frequency;
    AlertDialog priority;

    View frequencyView;
    View priorityView;

    ListView dialogList;

    ArrayAdapter<String> adapter;

    private DBControl local_db;
    utility hitamUtility;
    Bundle extras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        extras = getIntent().getExtras();
        local_db = new DBControl(this);
        hitamUtility = new utility(this);

        editItemAmount = (EditText) findViewById(R.id.editItemAmount);
        editItemName = (EditText) findViewById(R.id.editItemName);
        editItemFreq = (EditText) findViewById(R.id.editItemFreq);
        editItemPriority = (EditText) findViewById(R.id.editItemPriority);

        if (extras != null) {
            setEditableItem();
        } else {
            if(getParent() == null) {
                setResult(RESULT_CANCELED);
            } else {
                getParent().setResult(RESULT_CANCELED);
            }
            finish();
        }

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

    public void setEditableItem() {
        Cursor response = local_db.getItemDataByIdentifier(extras.getInt("selectedItem"));
        response.moveToFirst();

        Integer frequencyPosition = response.getInt(response.getColumnIndex(DBControl.ITEM_FREQ));
        Float amountSingular = response.getFloat(response.getColumnIndex(DBControl.ITEM_AMOUNT));
        Float amountDetermined = amountSingular * frequencyPosition;

        editItemName.setText((CharSequence) response.getString(response.getColumnIndex(DBControl.ITEM_NAME)));
        editItemAmount.setText((CharSequence) String.valueOf(amountDetermined));

        Integer priorityPosition = response.getInt(response.getColumnIndex(DBControl.ITEM_PRIORITY));
        section = response.getString(response.getColumnIndex(DBControl.ITEM_SECTION));

        hitamUtility.setFrequencyList();

        if(hitamUtility.values.size() > 0) {
            editItemFreq.setText((CharSequence) hitamUtility.values.get(hitamUtility.keys.indexOf(frequencyPosition)));
            freqValue = frequencyPosition;
        } else {
            editItemFreq.setText((CharSequence) "");
        }

        hitamUtility.setPriorityList();

        if(hitamUtility.values.size() > 0) {
            editItemPriority.setText((CharSequence) hitamUtility.values.get(hitamUtility.keys.indexOf(priorityPosition)));
            prioValue = priorityPosition;
        } else {
            editItemPriority.setText((CharSequence) "");
        }
    }

    //Generate the frequency dialog list
    public void selectEditItemFrequency(View view) {
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
                        editItemFreq.setText((CharSequence) hitamUtility.values.get(position));
                        freqValue = hitamUtility.keys.get(position);
                        frequency.dismiss();
                    }
                });
            }
            }
        });
        frequency.show();
    }


    public void selectEditItemPriority(View view) {
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
                        editItemPriority.setText((CharSequence) hitamUtility.values.get(position));
                        prioValue = hitamUtility.keys.get(position);
                        priority.dismiss();
                    }
                });
            }
            }
        });
        priority.show();
    }

    public boolean updateItem() {
        Float amountEntered = Float.parseFloat(String.valueOf(editItemAmount.getText()));
        Float amountSingular = amountEntered / freqValue;
        Bundle itemBundle = new Bundle();
        itemBundle.putString("name", String.valueOf(editItemName.getText()));
        itemBundle.putFloat("amount", amountSingular);
        itemBundle.putInt("frequency", freqValue);
        itemBundle.putInt("priority", prioValue);
        itemBundle.putInt("identifier", extras.getInt("selectedItem"));
        return local_db.updateItem(itemBundle);
    }

    public boolean deleteItem() {
        return local_db.deleteItem(extras.getInt("selectedItem"));
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
                if(updateItem()) {
                    if (getParent() == null) {
                        setResult(RESULT_OK);
                    } else {
                        getParent().setResult(RESULT_OK);
                    }
                    finish();
                } else {
                    Toast.makeText(this, R.string.generic_error, Toast.LENGTH_SHORT).show();
                    finish();
                }
                return true;
            case R.id.delete_item_action:
                if(deleteItem()) {
                    if (getParent() == null) {
                        setResult(RESULT_FIRST_USER);
                    } else {
                        getParent().setResult(RESULT_FIRST_USER);
                    }
                    finish();
                } else {
                    Toast.makeText(this, R.string.generic_error, Toast.LENGTH_SHORT).show();
                    finish();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
