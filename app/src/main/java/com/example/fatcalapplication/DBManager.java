package com.example.fatcalapplication;

import android.content.ContentValues;
import android.content.Context;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.SQLException;

public class DBManager {
    private MySQLiteOpenHelper myDBHelper;
    private SQLiteDatabase db;
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "steps.db";
    private final Context context;
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + DBStructure.tableEntry.TABLE_NAME + " (" +
                    DBStructure.tableEntry._ID + " INTEGER PRIMARY KEY," +
                    DBStructure.tableEntry.COLUMN_USER_ID + TEXT_TYPE + COMMA_SEP +
                    DBStructure.tableEntry.COLUMN_DATE + TEXT_TYPE + COMMA_SEP +
                    DBStructure.tableEntry.COLUMN_TIME + TEXT_TYPE + COMMA_SEP +
                    DBStructure.tableEntry.COLUMN_STEPS_TAKEN + TEXT_TYPE +
                    " );";
    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + DBStructure.tableEntry.TABLE_NAME;
    private String[] columns = {
            DBStructure.tableEntry.COLUMN_USER_ID,
            DBStructure.tableEntry.COLUMN_DATE,
            DBStructure.tableEntry.COLUMN_TIME,
            DBStructure.tableEntry.COLUMN_STEPS_TAKEN};

    public DBManager(Context ctx) {
        this.context = ctx;
        myDBHelper = new MySQLiteOpenHelper(context);
    }

    private static class MySQLiteOpenHelper extends SQLiteOpenHelper {
        public MySQLiteOpenHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(SQL_CREATE_ENTRIES);
        }
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // This database is only a cache for online data, so its upgrade policy is
            // to simply to discard the data and start over
            db.execSQL(SQL_DELETE_ENTRIES);
            onCreate(db);
        }
    }

    public DBManager open() throws SQLException {
        db = myDBHelper.getWritableDatabase();
        return this;
    }
    public void close() {
        myDBHelper.close();
    }

    public long insertStep(String userid, String stepsDate, String stepsTime, String steps) {
        ContentValues values = new ContentValues();
        values.put(DBStructure.tableEntry.COLUMN_USER_ID, userid);
        values.put(DBStructure.tableEntry.COLUMN_DATE, stepsDate);
        values.put(DBStructure.tableEntry.COLUMN_TIME, stepsTime);
        values.put(DBStructure.tableEntry.COLUMN_STEPS_TAKEN, steps);
        return db.insert(DBStructure.tableEntry.TABLE_NAME, null, values);
    }

    public Cursor getAllSteps() {
        return db.query(DBStructure.tableEntry.TABLE_NAME, columns, null, null, null, null, null);
    }

   /* public int deleteStep(String rowId) {
        String[] selectionArgs = { String.valueOf(rowId) };
        String selection = DBStructure.tableEntry.COLUMN_ID + " LIKE ?";
        return db.delete(DBStructure.tableEntry.TABLE_NAME, selection,selectionArgs );
    }*/




}
