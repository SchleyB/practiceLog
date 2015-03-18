package com.gmail.slybarrack.practicelog.app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by natalie on 6/11/14.
 */
public class MySQLiteHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;

    private static final String DATABASE_NAME = "LogDB";

    private SQLiteDatabase db;
    int finalTimer;

    public MySQLiteHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_MYLOG_TABLE = "CREATE TABLE myLogs ( " + "_id INTEGER PRIMARY KEY  , " + "date TEXT, " + "notes TEXT, " + "timer TEXT )";
        db.execSQL(CREATE_MYLOG_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS myLogs");
        this.onCreate(db);
    }

    public static final String TABLE_MYLOG = "myLogs";
    public static final String KEY_ID = "_id";
    public static final String KEY_DATE = "date";
    public static final String KEY_NOTES = "notes";
    public static final String KEY_TIMER = "timer";

    public static String[] COLUMNS = {KEY_ID, KEY_DATE, KEY_NOTES, KEY_TIMER};

    public void addLog(MyLog myLogs){

        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_DATE, myLogs.getDate());
        values.put(KEY_NOTES, myLogs.getNotes());
        values.put(KEY_TIMER, myLogs.getTimer());

        db.insert(TABLE_MYLOG, null, values);

        db.close();
        }

    public MyLog getLog(int id){

        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(TABLE_MYLOG, COLUMNS, "_id = ?", new String[]{String.valueOf(id)}, null, null, null, null);

        MyLog myLog = null;

        if(cursor != null && cursor.moveToFirst()){
            do {
                myLog = new MyLog();
                myLog.setId(Integer.parseInt(cursor.getString(0)));
                myLog.setDate(cursor.getString(1));
                myLog.setNotes(cursor.getString(2));
                myLog.setTimer(cursor.getString(3));

            }while (cursor.moveToNext());
        }


        return myLog;

    }

    public Cursor getAllData()
    {

        String query = "SELECT * FROM " + TABLE_MYLOG;
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public ArrayList<Integer> getTimerData(){

        ArrayList<Integer> arrayInt = new ArrayList<Integer>();

        String query = "SELECT * FROM " + TABLE_MYLOG;

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        if(cursor != null && cursor.moveToFirst()){
            do {
                String timer = (cursor.getString(3));

                String array[] = timer.split(":");

                if(array.length == 2){
                    finalTimer = Integer.parseInt(array[0]) * 60 * 1000 + Integer.parseInt(array[1]) * 1000;
                }else{
                    finalTimer = Integer.parseInt(array[0]) * 60 * 60 * 1000 + Integer.parseInt(array[1]) * 60 * 1000 + Integer.parseInt(array[2]) * 1000;
                }

                arrayInt.add(finalTimer);

            }while (cursor.moveToNext());
        }

        return arrayInt;
    }

    public ArrayList<String> getDateData(){

        ArrayList<String> arrayString = new ArrayList<String>();

        String query = "SELECT * FROM " + TABLE_MYLOG;

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        if(cursor != null && cursor.moveToFirst()){
            do {
                String date = (cursor.getString(1));

                arrayString.add(date);

            }while (cursor.moveToNext());
        }

        return arrayString;
    }

    public void deleteLog(MyLog myLog){

        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_MYLOG, KEY_ID+ " = ?", new String[]{String.valueOf(myLog.getId())});

        db.close();

    }

    public int updateLog(MyLog myLog, String notes, String timer){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("date", myLog.getDate());
        values.put("notes", notes);
        values.put("timer", timer);

        int i = db.update(TABLE_MYLOG, values, KEY_ID+ " = ?", new String[]{String.valueOf(myLog.getId())});

        db.close();
        return i;
    }


}
