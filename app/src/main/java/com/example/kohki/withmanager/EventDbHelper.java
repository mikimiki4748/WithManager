package com.example.kohki.withmanager;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by kohki on 16/09/05.
 */
public class EventDbHelper extends SQLiteOpenHelper {
    //---
    private static final String TEXT_TYPE = " TEXT";
    private static final String INT_TYPE = " INTEGER";
    private static final String COMMA_SEP = ",";

    private static final String SQL_CREATE_EVENT_ENTRIES =
            "CREATE TABLE " + EventContract.Event.TABLE_NAME + " (" +
                    EventContract.Event._ID + " INTEGER PRIMARY KEY," +
                    EventContract.Event.COL_TEAM        + INT_TYPE  + COMMA_SEP +
                    EventContract.Event.COL_NUM         + INT_TYPE  + COMMA_SEP +
                    EventContract.Event.COL_POINT       + INT_TYPE  + COMMA_SEP +
                    EventContract.Event.COL_SUCCESS     + INT_TYPE  + COMMA_SEP +
                    EventContract.Event.COL_EVENT       + TEXT_TYPE + COMMA_SEP +
                    EventContract.Event.COL_MOVIE_NAME  + TEXT_TYPE + COMMA_SEP +
                    EventContract.Event.COL_DATETIME    + TEXT_TYPE + COMMA_SEP +
                    EventContract.Event.COL_QUARTER_NUM + INT_TYPE  +" )";

    private static final String SQL_DELETE_EVENT_ENTRIES =
            "DROP TABLE IF EXISTS " + EventContract.Event.TABLE_NAME;

    //Gameのテーブル作成
    private static final String SQL_CREATE_GAME_ENTRIES =
            "CREATE TABLE " + EventContract.Game.TABLE_NAME + " (" +
                    EventContract.Game._ID + " INTEGER PRIMARY KEY," +
                    EventContract.Game.COL_DATE_TIME  + TEXT_TYPE + COMMA_SEP +
                    EventContract.Game.COL_GAME_NAME  + TEXT_TYPE + COMMA_SEP +
                    EventContract.Game.COL_GAME_NOTES + TEXT_TYPE  +" )";

    private static final String SQL_DELETE_GAME_ENTRIES =
            "DROP TABLE IF EXISTS " + EventContract.Game.TABLE_NAME;
    //---

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "EventLog.db";

    public EventDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_EVENT_ENTRIES);
        db.execSQL(SQL_CREATE_GAME_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_EVENT_ENTRIES);
        db.execSQL(SQL_DELETE_GAME_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public static HashMap<String,String> getRowFromID(Context context, int id){
        EventDbHelper mDbHelper = new EventDbHelper(context);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+ EventContract.Event.TABLE_NAME+" WHERE _id = ?", new String[]{String.valueOf(id)});
        HashMap<String,String> row = new HashMap<>();
        try {
            if (cursor.moveToNext()) {
                row.put(EventContract.Event.COL_TEAM,
                        cursor.getString(cursor.getColumnIndex(EventContract.Event.COL_TEAM)));
                row.put(EventContract.Event.COL_NUM,
                        cursor.getString(cursor.getColumnIndex(EventContract.Event.COL_NUM)));
                row.put(EventContract.Event.COL_POINT,
                        cursor.getString(cursor.getColumnIndex(EventContract.Event.COL_POINT)));
                row.put(EventContract.Event.COL_SUCCESS,
                        cursor.getString(cursor.getColumnIndex(EventContract.Event.COL_SUCCESS)));
                row.put(EventContract.Event.COL_EVENT,
                        cursor.getString(cursor.getColumnIndex(EventContract.Event.COL_EVENT)));
                row.put(EventContract.Event.COL_MOVIE_NAME,
                        cursor.getString(cursor.getColumnIndex(EventContract.Event.COL_MOVIE_NAME)));
                row.put(EventContract.Event.COL_DATETIME,
                        cursor.getString(cursor.getColumnIndex(EventContract.Event.COL_DATETIME)));
                row.put(EventContract.Event.COL_QUARTER_NUM,
                        cursor.getString(cursor.getColumnIndex(EventContract.Event.COL_QUARTER_NUM)));
            }
        } finally {
            cursor.close();
        }
        return row;
    }
    
    public static ArrayList<String> getRowFromSuccessShoot(SQLiteDatabase db, String game_start_time){
        Cursor cursor = db.rawQuery("SELECT * FROM "+ EventContract.Event.TABLE_NAME+" WHERE "+
                EventContract.Event.COL_SUCCESS+" = '1' and "+
                EventContract.Event.COL_EVENT+" = 'shoot' and "+
                EventContract.Event.COL_DATETIME+" = ?", new String[]{String.valueOf(game_start_time)});
        ArrayList column = new ArrayList();
        try {
            Integer[] row = new Integer[2];
            if (cursor.moveToNext()) {
                row[0] = cursor.getInt(cursor.getColumnIndex(EventContract.Event.COL_TEAM));
                row[1] = cursor.getInt(cursor.getColumnIndex(EventContract.Event.COL_POINT));
            }
        } finally {
            cursor.close();
        }
        return column;
    }
}