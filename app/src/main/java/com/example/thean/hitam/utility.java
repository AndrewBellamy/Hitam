package com.example.thean.hitam;

import android.content.Context;

import java.util.ArrayList;

/**
 * Created by Andrew Bellamy on 11/09/2017.
 * For Hitam | Assignment 2 SIT207
 * Student ID: 215240036
 */
public class utility {

    //Controls
    ArrayList<Integer> keys = new ArrayList<Integer>();
    ArrayList<String> values = new ArrayList<String>();
    //Context
    Context utilityContext;
    //DB
    private DBControl local_db;

    /**
     * Initialises the class, setting the context and DB interface.
     * @param context
     */
    public utility(Context context) {
        utilityContext = context;
        local_db = new DBControl(context);
    }

    /**
     * Clears the ArrayLists. Used prior to setting the Array Lists.
     */
    public void clearAddItemArrays() {
        values.clear();
        keys.clear();
    }

    /**
     * Gathers array data and pushes into the frequency ArrayList.
     */
    public void setFrequencyList() {
        clearAddItemArrays();
        String[] frequencyArray = utilityContext.getResources().getStringArray(R.array.frequency_array);
        for(String entry : frequencyArray) {
            String[] pair = entry.split(":");
            values.add(pair[0]);
            keys.add(Integer.parseInt(pair[1]));
        }
    }

    /**
     * Gathers array data and pushes into the priority ArrayList.
     */
    public void setPriorityList() {
        clearAddItemArrays();
        String[] priorityArray = utilityContext.getResources().getStringArray(R.array.priority_array);
        for(String entry : priorityArray) {
            String[] pair = entry.split(":");
            values.add(pair[0]);
            keys.add(Integer.parseInt(pair[1]));
        }
    }

    /**
     * Task pushed to background thread, for returning and setting Commitment priority and total in
     * the DB.
     * @param section
     */
    public void calculateSection(final String section) {
        if(section != "Unsorted") {
            Runnable calculate = new Runnable() {
                @Override
                public void run() {
                    Float total = local_db.getItemTotal(section);

                    Integer avgPriority = local_db.getItemPriority(section);

                    local_db.setSectionByItems(section, avgPriority, total);
                }
            };
            new Thread(calculate).start();
        }
    }
}
