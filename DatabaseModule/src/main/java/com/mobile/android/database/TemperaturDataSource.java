package com.mobile.android.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.mobile.android.database.beans.Temperatur;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TemperaturDataSource {

    // Database fields
    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private String[] allColumns = { MySQLiteHelper.COLUMN_ID,
            MySQLiteHelper.COLUMN_ACCOUNT_NAME,
            MySQLiteHelper.COLUMN_UNIT,
            MySQLiteHelper.COLUMN_TIMESTAMP,
            MySQLiteHelper.COLUMN_TEMPERATUR,
            MySQLiteHelper.COLUMN_POSITION,
            MySQLiteHelper.COLUMN_COMMENT,
            MySQLiteHelper.COLUMN_TYPE };

    public TemperaturDataSource(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Temperatur findOrCreateTemperatur(String accountName, String timestamp, String unit, String temperatur, String position, String comment, String type) {
        Temperatur measureByDate = findTemperaturByAccountNameAndDate(accountName, timestamp);
        if(measureByDate == null){
            return replaceTemperatur(accountName, timestamp, unit, temperatur, position, comment, type);
        } else {
            return measureByDate;
        }
    }

    public void createTemperatur(String accountName, String timestamp, String unit, String temperatur, String position, String comment, String type) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_ACCOUNT_NAME, accountName);
        values.put(MySQLiteHelper.COLUMN_TIMESTAMP, timestamp);
        values.put(MySQLiteHelper.COLUMN_UNIT, unit);
        values.put(MySQLiteHelper.COLUMN_TEMPERATUR, temperatur);
        values.put(MySQLiteHelper.COLUMN_POSITION, position);
        values.put(MySQLiteHelper.COLUMN_COMMENT, comment);
        values.put(MySQLiteHelper.COLUMN_TYPE, type);
        database.insert(MySQLiteHelper.TABLE_TEMPERATUR, null,
                values);
    }

    public Temperatur replaceTemperatur(String accountName, String timestamp, String unit, String temperatur, String position, String comment, String type) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_ACCOUNT_NAME, accountName);
        values.put(MySQLiteHelper.COLUMN_TIMESTAMP, timestamp);
        values.put(MySQLiteHelper.COLUMN_UNIT, unit);
        values.put(MySQLiteHelper.COLUMN_TEMPERATUR, temperatur);
        values.put(MySQLiteHelper.COLUMN_POSITION, position);
        values.put(MySQLiteHelper.COLUMN_COMMENT, comment);
        values.put(MySQLiteHelper.COLUMN_TYPE, type);
        long insertOrReplaceId = database.replace(MySQLiteHelper.TABLE_TEMPERATUR, null,
                values);
        Cursor cursor = database.query(MySQLiteHelper.TABLE_TEMPERATUR,
                allColumns, MySQLiteHelper.COLUMN_ID + " = " + insertOrReplaceId, null,
                null, null, null);

        cursor.moveToFirst();
        Temperatur newMeasure = cursorToMeasure(cursor);
        cursor.close();
        return newMeasure;
    }

    public void deleteMeasure(Temperatur measure) {
        long id = measure.getId();
        database.delete(MySQLiteHelper.TABLE_TEMPERATUR, MySQLiteHelper.COLUMN_ID
                + " = " + id, null);
    }

    public List<Temperatur> getAllTemperatur(String accountName) {
        List<Temperatur> temperatur = new ArrayList<Temperatur>();

        // Order by timestamp
        String orderBy = MySQLiteHelper.COLUMN_TIMESTAMP + " DESC";

        // Select temperatur by account_name and date
        String[] args = { accountName };
        String selection =  MySQLiteHelper.COLUMN_ACCOUNT_NAME + " = ?";
        Cursor cursor = database.query(MySQLiteHelper.TABLE_TEMPERATUR,
                allColumns, selection, args, null, null, orderBy);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Temperatur measure = cursorToMeasure(cursor);
            temperatur.add(measure);
            cursor.moveToNext();
        }
        cursor.close();
        return temperatur;
    }

    public Temperatur findTemperaturByAccountNameAndDate(String accountName, String date) {
        Temperatur measure = null;

        // Select temperatur by account_name and date
        String[] args = { accountName, date };
        String selection =  MySQLiteHelper.COLUMN_ACCOUNT_NAME + " = ? AND " + MySQLiteHelper.COLUMN_TIMESTAMP + " = ?";
        Cursor cursor = database.query(MySQLiteHelper.TABLE_TEMPERATUR,
                allColumns, selection, args,
                null, null,  MySQLiteHelper.COLUMN_TIMESTAMP + " ASC");

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            measure = cursorToMeasure(cursor);
            cursor.moveToNext();
        }
        cursor.close();
        return measure;
    }

    private Temperatur cursorToMeasure(Cursor cursor) {
        Temperatur temperatur = new Temperatur();
        temperatur.setId(cursor.getLong(0));
        temperatur.setAccountName(cursor.getString(1));
        temperatur.setUnit(cursor.getString(2));
        temperatur.setTimestamp(cursor.getString(3));
        temperatur.setTemperatur(cursor.getString(4));
        temperatur.setPosition(cursor.getString(5));
        temperatur.setComment(cursor.getString(6));
        temperatur.setType(cursor.getString(7));
        return temperatur;
    }
}