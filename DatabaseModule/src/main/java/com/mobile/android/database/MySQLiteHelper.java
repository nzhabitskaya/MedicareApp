package com.mobile.android.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteHelper extends SQLiteOpenHelper {
    public static final String TABLE_MEASURES = "measures";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_ACCOUNT_ID = "account_id";
    public static final String COLUMN_ACCOUNT_NAME = "account_name";
    public static final String COLUMN_TIMESTAMP = "timestamp";
    public static final String COLUMN_WEIGHT = "weight";
    public static final String COLUMN_COMMENT = "comment";
    public static final String COLUMN_TYPE = "type";

    public static final String TABLE_TEMPERATUR = "temperatur";
    public static final String COLUMN_UNIT = "unit";
    public static final String COLUMN_POSITION = "position";
    public static final String COLUMN_TEMPERATUR = "temperatur";

    private static final String DATABASE_NAME = "ebabynotebook2.db";
    private static final int DATABASE_VERSION = 1;

    // Database creation sql statement
    private static final String TABLE_WITHINGS_CREATE = "create table "
            + TABLE_MEASURES + "(" + COLUMN_ID
            + " integer primary key autoincrement, "
            + COLUMN_ACCOUNT_ID + " text not null, "
            + COLUMN_ACCOUNT_NAME + " text not null, "
            + COLUMN_TIMESTAMP + " text not null, "
            + COLUMN_WEIGHT + " text not null, "
            + COLUMN_COMMENT + " text, "
            + COLUMN_TYPE + " text not null"
            + ")";
    private static final String TABLE_TEMPERATUR_CREATE = "create table "
            + TABLE_TEMPERATUR + "(" + COLUMN_ID
            + " integer primary key autoincrement, "
            + COLUMN_ACCOUNT_NAME + " text not null, "
            + COLUMN_UNIT + " text not null, "
            + COLUMN_TIMESTAMP + " text not null, "
            + COLUMN_TEMPERATUR + " text not null, "
            + COLUMN_POSITION + " text not null, "
            + COLUMN_COMMENT + " text, "
            + COLUMN_TYPE + " text not null"
            + ")";

    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(TABLE_WITHINGS_CREATE);
        database.execSQL(TABLE_TEMPERATUR_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(MySQLiteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MEASURES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TEMPERATUR);
        onCreate(db);
    }
}
