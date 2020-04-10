package com.mobile.android.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.mobile.android.database.beans.Measure;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class WithingsDataSource {

    // Database fields
    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private String[] allColumns = { MySQLiteHelper.COLUMN_ID,
            MySQLiteHelper.COLUMN_ACCOUNT_ID,
            MySQLiteHelper.COLUMN_ACCOUNT_NAME,
            MySQLiteHelper.COLUMN_TIMESTAMP,
            MySQLiteHelper.COLUMN_WEIGHT,
            MySQLiteHelper.COLUMN_COMMENT,
            MySQLiteHelper.COLUMN_TYPE };

    public WithingsDataSource(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Measure findOrCreateMeasure(String accountId, String accountName, String timestamp, String weight, String comment, String type) {
        Measure measureByDate = findMeasureByDate(accountId, accountName, timestamp);
        if(measureByDate == null){
            return replaceMeasure(accountId, accountName, timestamp, weight, comment, type);
        } else {
            return measureByDate;
        }
    }

    public void createMeasure(String accountId, String accountName, String timestamp, String weight, String comment, String type) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_ACCOUNT_ID, accountId);
        values.put(MySQLiteHelper.COLUMN_ACCOUNT_NAME, accountName);
        values.put(MySQLiteHelper.COLUMN_TIMESTAMP, timestamp);
        values.put(MySQLiteHelper.COLUMN_WEIGHT, weight);
        values.put(MySQLiteHelper.COLUMN_COMMENT, comment);
        values.put(MySQLiteHelper.COLUMN_TYPE, type);
        database.insert(MySQLiteHelper.TABLE_MEASURES, null,
                values);
    }

    public Measure replaceMeasure(String accountId, String accountName, String timestamp, String weight, String comment, String type) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_ACCOUNT_ID, accountId);
        values.put(MySQLiteHelper.COLUMN_ACCOUNT_NAME, accountName);
        values.put(MySQLiteHelper.COLUMN_TIMESTAMP, timestamp);
        values.put(MySQLiteHelper.COLUMN_WEIGHT, weight);
        values.put(MySQLiteHelper.COLUMN_COMMENT, comment);
        values.put(MySQLiteHelper.COLUMN_TYPE, type);
        long insertOrReplaceId = database.replace(MySQLiteHelper.TABLE_MEASURES, null,
                values);
        Cursor cursor = database.query(MySQLiteHelper.TABLE_MEASURES,
                allColumns, MySQLiteHelper.COLUMN_ID + " = " + insertOrReplaceId, null,
                null, null, null);

        cursor.moveToFirst();
        Measure newMeasure = cursorToMeasure(cursor);
        cursor.close();
        return newMeasure;
    }

    public void deleteMeasure(Measure measure) {
        long id = measure.getId();
        database.delete(MySQLiteHelper.TABLE_MEASURES, MySQLiteHelper.COLUMN_ID
                + " = " + id, null);
    }

    public List<Measure> getAllMeasures(String accountId, String accountName) {
        List<Measure> measures = new ArrayList<Measure>();

        // Order by timestamp
        String orderBy = MySQLiteHelper.COLUMN_TIMESTAMP + " DESC";

        // Select temperatur by account_name and date
        String[] args = { accountId, accountName };
        String selection =  MySQLiteHelper.COLUMN_ACCOUNT_ID + " = ? AND " + MySQLiteHelper.COLUMN_ACCOUNT_NAME + " = ?";
        Cursor cursor = database.query(MySQLiteHelper.TABLE_MEASURES,
                allColumns, selection, args, null, null, orderBy);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Measure measure = cursorToMeasure(cursor);
            measures.add(measure);
            cursor.moveToNext();
        }
        cursor.close();
        return measures;
    }

    public Measure findMeasureByDate(String accountId, String accountName, String date) {
        Measure measure = null;

        String[] args = { accountId, accountName, date };
        String selection =  MySQLiteHelper.COLUMN_ACCOUNT_ID + " = ? AND " + MySQLiteHelper.COLUMN_ACCOUNT_NAME + " = ? AND " + MySQLiteHelper.COLUMN_TIMESTAMP + " = ?";
        Cursor cursor = database.query(MySQLiteHelper.TABLE_MEASURES,
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

    private Measure cursorToMeasure(Cursor cursor) {
        Measure measure = new Measure();
        measure.setId(cursor.getLong(0));
        measure.setAccountId(cursor.getString(1));
        measure.setAccountName(cursor.getString(2));
        measure.setTimestamp(cursor.getString(3));
        measure.setWeight(cursor.getString(4));
        measure.setComment(cursor.getString(5));
        measure.setType(cursor.getString(6));
        return measure;
    }
}