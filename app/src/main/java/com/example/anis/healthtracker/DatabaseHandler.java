package com.example.anis.healthtracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "healthtracker";

    // heart rate table
    private static final String TABLE_HR = "heartratetable";
    private static final String KEY_ID = "id";
    private static final String KEY_HR = "hr";
    private static final String KEY_SYSTOLIC = "systolic";
    private static final String KEY_DIASTOLIC = "diastolic";
    private static final String KEY_DATE_TIME = "date";

    // steps table
    private static final String TABLE_STEPS = "stepstable";
    private static final String KEY_DATE = "date";
    private static final String KEY_STEPS = "steps";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // create hr table
        String createHrTable = "CREATE TABLE " + TABLE_HR + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KEY_HR + " INTEGER, "
                + KEY_SYSTOLIC + " INTEGER, "
                + KEY_DIASTOLIC + " INTEGER, "
                + KEY_DATE_TIME + " DATETIME"
                + ")";

        db.execSQL(createHrTable);

        // create steps table
        String createStepsTable = "CREATE TABLE " + TABLE_STEPS + "("
                + KEY_DATE + " DATE PRIMARY KEY, "
                + KEY_STEPS + " INTEGER"
                + ")";

        db.execSQL(createStepsTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HR);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STEPS);

        // Create tables again
        onCreate(db);
    }

    ////////////////////////
    // CRUD OPERATIONS
    ///////////////////////

    // Create
    void addHrValues(int hr, int systolic, int diastolic, Calendar cal) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_HR, hr);
        values.put(KEY_SYSTOLIC, systolic);
        values.put(KEY_DIASTOLIC, diastolic);
        values.put(KEY_DATE_TIME, cal.getTimeInMillis());

        db.insert(TABLE_HR, null, values);
        db.close();
    }

    void addStepsValues(Calendar cal, int steps) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_DATE, cal.getTimeInMillis());
        values.put(KEY_STEPS, steps);

        //db.insert(TABLE_STEPS, null, values);
        db.insertWithOnConflict(TABLE_STEPS, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }


    // Read

    public List<Integer> getHr() {

        List<Integer> hrList = new ArrayList<Integer>();

        String selectQuery = "SELECT " + KEY_HR + " FROM " + TABLE_HR
                + " ORDER BY " + KEY_DATE_TIME + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if(cursor.moveToFirst()) {
            do {
                Integer currHr = new Integer(cursor.getInt(0));

                hrList.add(currHr);
            }
            while(cursor.moveToNext());
        }

        return hrList;
    }

    public List<Integer> getSteps() {

        List<Integer> stepsList = new ArrayList<Integer>();

        String selectQuery = "SELECT " + KEY_STEPS + " FROM " + TABLE_STEPS
                + " ORDER BY " + KEY_DATE + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if(cursor.moveToFirst()) {
            do {
                Integer currStep = new Integer(cursor.getInt(0));

                stepsList.add(currStep);
            }
            while(cursor.moveToNext());
        }

        return stepsList;
    }

    // Update

    // Delete
}
