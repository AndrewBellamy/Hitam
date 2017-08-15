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
                        " "+INCOME_FREQ+" INTEGER, "+INCOME_STARTDATE+" DATE)"
        );

        db.execSQL(
                "CREATE TABLE "+SECTION_TABLE+" (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"+SECTION_NAME+" TEXT, "+SECTION_PRIORITY+" INTEGER, "+SECTION_TOTAL+" FLOAT)"
        );

        db.execSQL(
                "CREATE TABLE "+ITEM_TABLE+" (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "+ITEM_NAME+" TEXT, "+ITEM_AMOUNT+" FLOAT, "+ITEM_FREQ+" INTEGER," +
                        " "+ITEM_PRIORITY+" INTEGER,  "+ITEM_SECTION+" TEXT)"
        );

        ContentValues contentValues = new ContentValues();
        contentValues.put(SECTION_NAME, "Unsorted");
        contentValues.put(SECTION_PRIORITY, 1);
        contentValues.put(SECTION_TOTAL, 0.00F);
        db.insert(SECTION_TABLE, null, contentValues);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+INCOME_TABLE+"");
        db.execSQL("DROP TABLE IF EXISTS "+SECTION_TABLE+"");
        db.execSQL("DROP TABLE IF EXISTS "+ITEM_TABLE+"");
        onCreate(db);
    }

    //CUD methods

    /*
    public boolean insertNote(Editable entry, long entrylongdate) {
        SQLiteDatabase db = this.getWritableDatabase();
        Date entrydate = new Date(entrylongdate);
        ContentValues contentValues = new ContentValues();
        contentValues.put(NOTES_DATE, String.valueOf(entrydate));
        contentValues.put(NOTES_ENTRY, String.valueOf(entry));
        db.insert("notes", null, contentValues);
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

    public ArrayList<String> getSectionData() {
        ArrayList<String> array_sections = new ArrayList<String> ();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor response = db.rawQuery("SELECT * FROM "+SECTION_TABLE+"", null);
        response.moveToFirst();

        while(response.isAfterLast() == false) {
            array_sections.add(response.getString(response.getColumnIndex(SECTION_NAME)));
            Log.i("section name: ", String.valueOf(response.getColumnIndex(SECTION_NAME)));
            response.moveToNext();
        }
        return array_sections;
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
