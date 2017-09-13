package com.example.thean.hitam;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;

import java.sql.Date;
import java.util.ArrayList;

/**
 * Created by Andrew Bellamy on 9/08/2017.
 * For Hitam | Assignment 2 SIT207
 * Student ID: 215240036
 */

public class DBControl extends SQLiteOpenHelper {
    //Constants - Schema and tables
    public static final String DATABASE_NAME = "HitamSchema.db";

    //Income
    public static final String INCOME_TABLE = "incomeTable";
    public static final String INCOME_AMOUNT = "amount";
    public static final String INCOME_TAX = "tax";
    public static final String INCOME_DEDUCTION = "deductions";
    public static final String INCOME_FREQ = "frequency";
    public static final String INCOME_STARTDATE = "startdate";

    //Sections
    public static final String SECTION_TABLE = "sectionTable";
    public static final String SECTION_NAME = "name";
    public static final String SECTION_PRIORITY = "priority";
    public static final String SECTION_TOTAL = "total";

    //Items
    public static final String ITEM_TABLE = "itemTable";
    public static final String ITEM_NAME = "name";
    public static final String ITEM_AMOUNT = "amount";
    public static final String ITEM_FREQ = "frequency";
    public static final String ITEM_PRIORITY = "priority";
    public static final String ITEM_SECTION = "section";


    public DBControl(Context context) {
        super(context, DATABASE_NAME , null, 1);
    }

    //Firebase Instance

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE "+INCOME_TABLE+" (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"+INCOME_AMOUNT+" FLOAT, "+INCOME_TAX+" FLOAT, "+INCOME_DEDUCTION+" FLOAT," +
                        " "+INCOME_FREQ+" INTEGER, "+INCOME_STARTDATE+" INTEGER)"
        );

        db.execSQL(
                "CREATE TABLE "+SECTION_TABLE+" (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"+SECTION_NAME+" TEXT, "+SECTION_PRIORITY+" INTEGER, "+SECTION_TOTAL+" FLOAT)"
        );

        db.execSQL(
                "CREATE TABLE "+ITEM_TABLE+" (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "+ITEM_NAME+" TEXT, "+ITEM_AMOUNT+" FLOAT, "+ITEM_FREQ+" INTEGER," +
                        " "+ITEM_PRIORITY+" INTEGER,  "+ITEM_SECTION+" TEXT)"
        );

        ContentValues sectionValues = new ContentValues();
        sectionValues.put(SECTION_NAME, "Unsorted");
        sectionValues.put(SECTION_PRIORITY, 3);
        sectionValues.put(SECTION_TOTAL, 0.00F);
        db.insert(SECTION_TABLE, null, sectionValues);

        ContentValues incomeValues = new ContentValues();
        incomeValues.put(INCOME_AMOUNT, 0.00F);
        incomeValues.put(INCOME_TAX, 0.00F);
        incomeValues.put(INCOME_DEDUCTION, 0.00F);
        incomeValues.put(INCOME_FREQ, 1);
        java.util.Date dateObject = new java.util.Date();
        incomeValues.put(INCOME_STARTDATE, dateObject.getTime());
        db.insert(INCOME_TABLE, null, incomeValues);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+INCOME_TABLE+"");
        db.execSQL("DROP TABLE IF EXISTS "+SECTION_TABLE+"");
        db.execSQL("DROP TABLE IF EXISTS "+ITEM_TABLE+"");
        onCreate(db);
    }

    //CUD methods

    public boolean updateIncome(Bundle updateBundle) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(INCOME_AMOUNT, updateBundle.getFloat("amount"));
        contentValues.put(INCOME_TAX, updateBundle.getFloat("tax"));
        contentValues.put(INCOME_DEDUCTION, updateBundle.getFloat("deduction"));
        contentValues.put(INCOME_FREQ, updateBundle.getInt("frequency"));
        contentValues.put(INCOME_STARTDATE, updateBundle.getLong("startDate"));
        db.update(INCOME_TABLE, contentValues, "id = ?", new String[] {Integer.toString(updateBundle.getInt("identifier"))});
        return true;
    }

    public boolean insertSection(Editable sectionName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues sectionValues = new ContentValues();
        sectionValues.put(SECTION_NAME, String.valueOf(sectionName));
        sectionValues.put(SECTION_PRIORITY, 1);
        sectionValues.put(SECTION_TOTAL, 0.00F);
        db.insert(SECTION_TABLE, null, sectionValues);
        return true;
    }

    public boolean updateSection(Integer id, Editable sectionName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SECTION_NAME, String.valueOf(sectionName));
        db.update(SECTION_TABLE, contentValues, "id = ?", new String[] {Integer.toString(id)});
        return true;
    }

    public boolean setSectionByItems(String section, Integer priority, Float total) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SECTION_PRIORITY, priority);
        contentValues.put(SECTION_TOTAL, total);
        db.update(SECTION_TABLE, contentValues, SECTION_NAME+" = ?", new String[] {section});
        return true;
    }

    public boolean deleteSection(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(SECTION_TABLE, "id = ? ", new String[] {Integer.toString(id)});
        return true;
    }

    public boolean insertItem(Editable itemName, Float amount, Integer frequency, Integer priority, String section) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues itemValues = new ContentValues();
        itemValues.put(ITEM_NAME, String.valueOf(itemName));
        itemValues.put(ITEM_AMOUNT, amount);
        itemValues.put(ITEM_FREQ, frequency);
        itemValues.put(ITEM_PRIORITY, priority);
        itemValues.put(ITEM_SECTION, section);
        db.insert(ITEM_TABLE, null, itemValues);
        return true;
    }

    public boolean updateItem(Bundle updateBundle) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues itemValues = new ContentValues();
        itemValues.put(ITEM_NAME, updateBundle.getString("name"));
        itemValues.put(ITEM_AMOUNT, updateBundle.getFloat("amount"));
        itemValues.put(ITEM_FREQ, updateBundle.getInt("frequency"));
        itemValues.put(ITEM_PRIORITY, updateBundle.getInt("priority"));
        db.update(ITEM_TABLE, itemValues, "id = ?", new String[] {Integer.toString(updateBundle.getInt("identifier"))});
        return true;
    }

    public boolean unsortItems(String section) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues itemValues = new ContentValues();
        itemValues.put(ITEM_SECTION, "Unsorted");
        db.update(ITEM_TABLE, itemValues, ITEM_SECTION+" = ?", new String[] {section});
        return true;
    }

    public boolean updateItemSection(String currentSection, Editable newSection) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues itemValues = new ContentValues();
        itemValues.put(ITEM_SECTION, String.valueOf(newSection));
        db.update(ITEM_TABLE, itemValues, ITEM_SECTION+" = ?", new String[] {currentSection});
        return true;
    }

    public boolean deleteItem(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(ITEM_TABLE, "id = ? ", new String[] {Integer.toString(id)});
        return true;
    }
    /*
    public Integer deleteSection(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(SECTION_TABLE, "id = ? ", new String[] {Integer.toString(id)});
    }

    public Integer deleteItem(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(ITEM_TABLE, "id = ? ", new String[] {Integer.toString(id)});
    }

    public boolean insertItem(Editable sectionName, Float amount, Integer frequency, Integer priority, String section) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues itemValues = new ContentValues();
        itemValues.put(ITEM_NAME, String.valueOf(sectionName));
        itemValues.put(ITEM_AMOUNT, amount);
        itemValues.put(ITEM_FREQ, frequency);
        itemValues.put(ITEM_PRIORITY, priority);
        itemValues.put(ITEM_SECTION, section);
        db.insert(ITEM_TABLE, null, sectionValues);
        return true;
    }

    public boolean insertSection(Editable sectionName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues sectionValues = new ContentValues();
        sectionValues.put(SECTION_NAME, String.valueOf(sectionName));
        sectionValues.put(SECTION_PRIORITY, 1);
        sectionValues.put(SECTION_TOTAL, 0.00F);
        db.insert(SECTION_TABLE, null, sectionValues);
        return true;
    }

    public boolean updateNote(Integer id, Editable entry) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NOTES_ENTRY, String.valueOf(entry));
        db.update("notes", contentValues, "id = ?", new String[] {Integer.toString(id)});
        return true;
    }

    public Integer deleteNote (Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("notes", "id = ? ", new String[] {Integer.toString(id)});
    }
    */
    //Predicate methods
    public Bundle getIncomeData() {
        Bundle responseBundle = new Bundle();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor response = db.rawQuery("SELECT * FROM "+INCOME_TABLE+"", null);
        Integer identifier = 0;
        Float amount = 0F;
        Float tax = 0F;
        Float deduction = 0F;
        Integer frequency = 2;
        Long startDate = 0L;

        Integer count = 0;

        response.moveToFirst();

        while(response != null && count == 0) {
            identifier = response.getInt(response.getColumnIndex("id"));
            amount = response.getFloat(response.getColumnIndex(INCOME_AMOUNT));
            tax = response.getFloat(response.getColumnIndex(INCOME_TAX));
            deduction = response.getFloat(response.getColumnIndex(INCOME_DEDUCTION));
            frequency = response.getInt(response.getColumnIndex(INCOME_FREQ));
            startDate = response.getLong(response.getColumnIndex(INCOME_STARTDATE));
            response.moveToNext();
            count++;
            Log.i("income id: ", String.valueOf(response.getColumnIndex("id")));
        }
        responseBundle.putInt("identifier", identifier);
        responseBundle.putFloat("amount", amount);
        responseBundle.putFloat("tax", tax);
        responseBundle.putFloat("deduction", deduction);
        responseBundle.putInt("frequency", frequency);
        responseBundle.putLong("startDate", startDate);
        return responseBundle;
    }

    public Bundle getSectionData() {
        ArrayList<String> array_sections = new ArrayList<String> ();
        ArrayList<String> array_ids = new ArrayList<String> ();
        Bundle responseBundle = new Bundle();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor response = db.rawQuery("SELECT * FROM "+SECTION_TABLE+" ORDER BY "+SECTION_PRIORITY+" DESC", null);
        response.moveToFirst();

        while(response.isAfterLast() == false) {
            array_sections.add(response.getString(response.getColumnIndex(SECTION_NAME)));
            array_ids.add(response.getString(response.getColumnIndex("id")));
            response.moveToNext();
        }
        responseBundle.putStringArrayList("ids", array_ids);
        responseBundle.putStringArrayList("sections", array_sections);
        return responseBundle;
    }

    public Bundle getItemData(String section) {
        ArrayList<String> array_items = new ArrayList<String> ();
        ArrayList<String> array_ids = new ArrayList<String> ();
        Bundle responseBundle = new Bundle();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor response = db.rawQuery("SELECT * FROM "+ITEM_TABLE+" WHERE "+ITEM_SECTION+"='"+section+"' ORDER BY "+ITEM_PRIORITY+" DESC", null);
        response.moveToFirst();

        while(response.isAfterLast() == false) {
            array_items.add(response.getString(response.getColumnIndex(ITEM_NAME)));
            array_ids.add(response.getString(response.getColumnIndex("id")));
            response.moveToNext();
        }
        responseBundle.putStringArrayList("ids", array_ids);
        responseBundle.putStringArrayList("items", array_items);
        return responseBundle;
    }

    public Cursor getItemDataByIdentifier(Integer identifier) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor response = db.rawQuery("SELECT * FROM "+ITEM_TABLE+" WHERE id='"+identifier+"'", null);
        return response;
    }

    public Float getItemTotal(String section) {
        ArrayList<Float> array_amounts = new ArrayList<Float> ();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor response = db.rawQuery("SELECT TOTAL("+ITEM_AMOUNT+") FROM "+ITEM_TABLE+" WHERE "+ITEM_SECTION+"='"+section+"'", null);
        response.moveToFirst();

        return response.getFloat(0);
    }

    public Float getAllItemSum() {
        ArrayList<Float> array_amounts = new ArrayList<Float> ();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor response = db.rawQuery("SELECT TOTAL("+ITEM_AMOUNT+") FROM "+ITEM_TABLE+"", null);
        response.moveToFirst();

        return response.getFloat(0);
    }

    public Integer getItemPriority(String section) {
        ArrayList<Integer> array_priority = new ArrayList<Integer>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor response = db.rawQuery("SELECT AVG("+ITEM_PRIORITY+") FROM "+ITEM_TABLE+" WHERE "+ITEM_SECTION+"='"+section+"'", null);
        response.moveToFirst();

        return response.getInt(0);
    }
    /*



    public Cursor getData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor response = db.rawQuery("SELECT * FROM notes WHERE id="+id+"", null);
        return response;
    }

    public Bundle getDataByDate(long selectedDateLong) {
        ArrayList<String> array_entries = new ArrayList<String> ();
        ArrayList<String> array_ids = new ArrayList<String> ();
        Bundle responseBundle = new Bundle();
        Date selectedDate = new Date(selectedDateLong);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor response = db.rawQuery("SELECT * FROM notes WHERE entrydate=Date('"+String.valueOf(selectedDate)+"')", null);
        response.moveToFirst();

        while(response.isAfterLast() == false) {
            array_entries.add(response.getString(response.getColumnIndex(NOTES_ENTRY)));
            array_ids.add(response.getString(response.getColumnIndex(NOTES_ID)));
            response.moveToNext();
        }
        responseBundle.putStringArrayList("ids", array_ids);
        responseBundle.putStringArrayList("entries", array_entries);
        return responseBundle;
    }
    */
}
