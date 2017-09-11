package com.example.thean.hitam;

import android.content.Context;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by thean on 11/09/2017.
 */

public class utility {

    //Variables
    ArrayList<Integer> keys = new ArrayList<Integer>();
    ArrayList<String> values = new ArrayList<String>();

    Context utilityContext;

    public utility(Context context) {
        utilityContext = context;
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

    public void calculateBalance() {

    }

    public void calculateSectionPriority() {

    }

    public void caluclateSectionAmount() {

    }
}
