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

    //Spend
    public static final String SPEND_TABLE = "spendTable";
    public static final String SPEND_TAKE = "subtract";
    public static final String SPEND_AMOUNT = "amount";

    /**
     * Initialise the DB interface
     * @param context
     */
    public DBControl(Context context) {
        super(context, DATABASE_NAME , null, 1);
    }

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

        db.execSQL(
                "CREATE TABLE "+SPEND_TABLE+" (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "+SPEND_TAKE+" INTEGER, "+SPEND_AMOUNT+" FLOAT)"
        );

        //Set initial values - this will not persist after first use
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
        db.execSQL("DROP TABLE IF EXISTS "+SPEND_TABLE+"");
        onCreate(db);
    }

    //CUD methods

    /**
     * Updates the income table. Only one row is ever stored and updated.
     * @param updateBundle
     * @return
     */
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

    /**
     * Inserts a new commitment into the section table.
     * @param sectionName
     * @return
     */
    public boolean insertSection(Editable sectionName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues sectionValues = new ContentValues();
        sectionValues.put(SECTION_NAME, String.valueOf(sectionName));
        sectionValues.put(SECTION_PRIORITY, 1);
        sectionValues.put(SECTION_TOTAL, 0.00F);
        db.insert(SECTION_TABLE, null, sectionValues);
        return true;
    }

    /**
     * Updates commitment using id, replacing the section name.
     * @param id
     * @param sectionName
     * @return
     */
    public boolean updateSection(Integer id, Editable sectionName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SECTION_NAME, String.valueOf(sectionName));
        db.update(SECTION_TABLE, contentValues, "id = ?", new String[] {Integer.toString(id)});
        return true;
    }

    /**
     * Used to update commitment total and priority after retrieving aggregations from DB.
     * @param section
     * @param priority
     * @param total
     * @return
     */
    public boolean setSectionByItems(String section, Integer priority, Float total) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SECTION_PRIORITY, priority);
        contentValues.put(SECTION_TOTAL, total);
        db.update(SECTION_TABLE, contentValues, SECTION_NAME+" = ?", new String[] {section});
        return true;
    }

    /**
     * Deletes a commitment using the id.
     * @param id
     * @return
     */
    public boolean deleteSection(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(SECTION_TABLE, "id = ? ", new String[] {Integer.toString(id)});
        return true;
    }

    /**
     * Inserts an expense into the item table.
     * @param itemName
     * @param amount
     * @param frequency
     * @param priority
     * @param section
     * @return
     */
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

    /**
     * Updates an expense in the item table, using the ID.
     * @param updateBundle
     * @return
     */
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

    /**
     * Defaults expenses into the unsorted, commitments group, when their commitment has been deleted.
     * @param section
     * @return
     */
    public boolean unsortItems(String section) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues itemValues = new ContentValues();
        itemValues.put(ITEM_SECTION, "Unsorted");
        db.update(ITEM_TABLE, itemValues, ITEM_SECTION+" = ?", new String[] {section});
        return true;
    }

    /**
     * Updates the expenses section, when the commitment name has changed.
     * @param currentSection
     * @param newSection
     * @return
     */
    public boolean updateItemSection(String currentSection, Editable newSection) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues itemValues = new ContentValues();
        itemValues.put(ITEM_SECTION, String.valueOf(newSection));
        db.update(ITEM_TABLE, itemValues, ITEM_SECTION+" = ?", new String[] {currentSection});
        return true;
    }

    /**
     * Deletes an expense from the items table.
     * @param id
     * @return
     */
    public boolean deleteItem(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(ITEM_TABLE, "id = ? ", new String[] {Integer.toString(id)});
        return true;
    }

    /**
     * Inserts spending adjustment, either gain or loss. Spending is never updated.
     * @param take
     * @param amount
     * @return
     */
    public boolean insertSpend(Integer take, Float amount) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues spendValues = new ContentValues();
        spendValues.put(SPEND_TAKE, take);
        spendValues.put(SPEND_AMOUNT, amount);
        db.insert(SPEND_TABLE, null, spendValues);
        return true;
    }

    /**
     * Clears all spending rows. Used when Income has been saved.
     * @return
     */
    public boolean deleteSpend() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + SPEND_TABLE);
        return true;
    }

    //Predicate methods

    /**
     * Retrieves income data in a single Bundle.
     * @return
     */
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

    /**
     * Retrieves all commitments in a single Bundle.
     * @return
     */
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

    /**
     * Retrieves expenses, based on commitment, as a single Bundle.
     * @param section
     * @return
     */
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

    /**
     * Retrieves one expense, based on ID, as a Cursor.
     * @param identifier
     * @return
     */
    public Cursor getItemDataByIdentifier(Integer identifier) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor response = db.rawQuery("SELECT * FROM "+ITEM_TABLE+" WHERE id='"+identifier+"'", null);
        return response;
    }

    /**
     * Returns aggregate TOTAL for expenses, based on commitment.
     * @param section
     * @return
     */
    public Float getItemTotal(String section) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor response = db.rawQuery("SELECT TOTAL("+ITEM_AMOUNT+") FROM "+ITEM_TABLE+" WHERE "+ITEM_SECTION+"='"+section+"'", null);
        response.moveToFirst();

        return response.getFloat(0);
    }

    /**
     * Returns aggrgate TOTAL of ALL expenses in DB;
     * @return
     */
    public Float getAllItemSum() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor response = db.rawQuery("SELECT TOTAL("+ITEM_AMOUNT+") FROM "+ITEM_TABLE+"", null);
        response.moveToFirst();

        return response.getFloat(0);
    }

    /**
     * Returns the aggregate AVG of priority, based on commitment.
     * @param section
     * @return
     */
    public Integer getItemPriority(String section) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor response = db.rawQuery("SELECT AVG("+ITEM_PRIORITY+") FROM "+ITEM_TABLE+" WHERE "+ITEM_SECTION+"='"+section+"'", null);
        response.moveToFirst();

        return response.getInt(0);
    }

    /**
     * Returns the aggregate TOTAL of spending, as gained.
     * @return
     */
    public Float getSpendAddTotal() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor response = db.rawQuery("SELECT TOTAL("+SPEND_AMOUNT+") FROM "+SPEND_TABLE+" WHERE "+SPEND_TAKE+"='"+0+"'", null);
        response.moveToFirst();

        return response.getFloat(0);
    }

    /**
     * Returns the aggregate TOTAL of spending, as lost.
     * @return
     */
    public Float getSpendTakeTotal() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor response = db.rawQuery("SELECT TOTAL("+SPEND_AMOUNT+") FROM "+SPEND_TABLE+" WHERE "+SPEND_TAKE+"='"+1+"'", null);
        response.moveToFirst();

        return response.getFloat(0);
    }

}
