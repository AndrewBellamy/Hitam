package com.example.thean.hitam;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by thean on 11/09/2017.
 */

public class utility {

    //Variables
    ArrayList<Integer> keys = new ArrayList<Integer>();
    ArrayList<String> values = new ArrayList<String>();

    Context utilityContext;

    private DBControl local_db;


    public utility(Context context) {
        utilityContext = context;
        local_db = new DBControl(context);
    }

    public void clearAddItemArrays() {
        values.clear();
        keys.clear();
    }

    public void setFrequencyList() {
        clearAddItemArrays();
        String[] frequencyArray = utilityContext.getResources().getStringArray(R.array.frequency_array);
        for(String entry : frequencyArray) {
            String[] pair = entry.split(":");
            values.add(pair[0]);
            keys.add(Integer.parseInt(pair[1]));
        }
    }

    public void setPriorityList() {
        clearAddItemArrays();
        String[] priorityArray = utilityContext.getResources().getStringArray(R.array.priority_array);
        for(String entry : priorityArray) {
            String[] pair = entry.split(":");
            values.add(pair[0]);
            keys.add(Integer.parseInt(pair[1]));
        }
    }

    public Float calculateBalance() {

        return 0.00F;
    }

    public void calculateSection(final String section) {
        if(section != "Unsorted") {
            Runnable calculate = new Runnable() {
                @Override
                public void run() {
                    Float total = local_db.getItemTotal(section);

                    Log.i("total", String.valueOf(total));

                    Integer avgPriority = local_db.getItemPriority(section);

                    Log.i("average priority", String.valueOf(avgPriority));

                    local_db.setSectionByItems(section, avgPriority, total);
                }
            };
            new Thread(calculate).start();
        }
    }
}
