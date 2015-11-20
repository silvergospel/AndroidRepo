package com.cecs453.my_maps;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Michael on 11/18/2015.
 */
public class LocationDB extends SQLiteOpenHelper {
    private static String DB_NAME = "locationsDB";
    private static int VERSION = 2;
    public static final String DB_TABLE = "locations";
    public static final String FIELD_ID = "id";
    public static final String FIELD_LAT = "lat";
    public static final String FIELD_LNG = "lng";
    public static final String FIELD_ZOOM = "zoom";


    private SQLiteDatabase db;

    public LocationDB(Context c){
        super(c, DB_NAME, null, VERSION);
        this.db = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String sql = "create table if not exists " + DB_TABLE + "(" +
                FIELD_ID + " integer primary key autoincrement, " +
                FIELD_LNG + " double not null, " +
                FIELD_LAT + " double not null, " +
                FIELD_ZOOM + " float not null);";
        db.execSQL(sql);
    }

    public long insertLocation (ContentValues contentValues){
        return db.insert(DB_TABLE, null, contentValues);

    }

    public int deleteAll(){
        return db.delete(DB_TABLE, null, null);
    }

    public Cursor getAllLocations(){
        return db.query(DB_TABLE, new String[]{FIELD_ID, FIELD_LAT, FIELD_LNG, FIELD_ZOOM}, null, null, null, null, null);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
