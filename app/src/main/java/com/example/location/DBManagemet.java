package com.example.location;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DBManagemet extends SQLiteOpenHelper {


    private static final String DATABASE_NAME = "location_management";
    private static final int DATABASE_VERSION = 1;
    //table
    private static final String TABLE_location = "location";
    //columns

    private static String  ID = "id";
    private static final String ADDRESS="location_address";
    private static final  String LATITUDE = "latitude";
    private static final  String LONGITUDE = "longitude";
    public DBManagemet(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_POSTS_TABLE = "CREATE TABLE " + TABLE_location +
                "(" +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                ADDRESS + " varchar(200),\n" +
                LATITUDE + " varchar(200) ,\n" +
                LONGITUDE + " varchar(200) "+
                ")";
        db.execSQL(CREATE_POSTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {

            db.execSQL("DROP TABLE IF EXISTS " + TABLE_location);

            onCreate(db);
        }
    }

    boolean AddLocation(String address, String latitude, String longitude)
    {
        ContentValues contentValues =new ContentValues();
        contentValues.put(ADDRESS,address);
        contentValues.put(LATITUDE,latitude);
        contentValues.put(LONGITUDE,longitude);
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        long result = sqLiteDatabase.insert(TABLE_location, null, contentValues);
        if (result != -1) {

            return true;
        } else {

            Log.e("Insert Error", "Failed to insert data into the database");
            return false;
        }
    }

    boolean UpdateLocation(int id, String address, String latitude, String longitude)
    {

        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues =new ContentValues();
        contentValues.put(ADDRESS,address);
        contentValues.put(LATITUDE,latitude);
        contentValues.put(LONGITUDE,longitude);


        return sqLiteDatabase.update(TABLE_location,contentValues,ID + "=?",new String[]{String.valueOf(id)}) >0;
    }

    boolean DeleteLocation(int id)
    {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();

        SQLiteDatabase db = getWritableDatabase();
        int rowsDeleted = db.delete(TABLE_location, ID + "=?", new String[]{String.valueOf(id)});
        db.close();
        return rowsDeleted > 0;
    }
    @SuppressLint("Range")
    public List<Location> getAllLocations() {
        List<Location> locationList = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_location, null);
        if (cursor.moveToFirst()) {
            do {
                Location location = new Location();
                location.setId(cursor.getInt(cursor.getColumnIndex(ID)));
                location.setLat(cursor.getString(cursor.getColumnIndex(LATITUDE)));
                location.setLon(cursor.getString(cursor.getColumnIndex(LONGITUDE)));
                location.setAddrss(cursor.getString(cursor.getColumnIndex(ADDRESS)));

                locationList.add(location);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return locationList;
    }

    //checking if already entered the 50 locations
     boolean checkIfDataExistsInDatabase() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = null;
        boolean dataExists = false;

        try {
            String query = "SELECT id FROM location LIMIT 1";
            cursor = db.rawQuery(query, null);

            if (cursor.moveToFirst()) {
                // Data exists in the database

                dataExists = true;

            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return dataExists;
    }
    public void deleteAllLocations() {
        SQLiteDatabase db = getWritableDatabase();
        db.delete("location", null, null);
        db.close();
    }

    @SuppressLint("Range")
    public Location searchByAddress(String query) {
        Location location = new Location();
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        String selection = ADDRESS + " LIKE ?";
        String[] selectionArgs = { "%" + query + "%" }; // Add wildcards to match any part of the title

        Cursor cursor = sqLiteDatabase.query(TABLE_location, null, selection, selectionArgs, null, null, null);

        if (cursor.moveToFirst()) {
            do {

                location.setId(cursor.getInt(cursor.getColumnIndex(ID)));
                location.setAddrss(cursor.getString(cursor.getColumnIndex(ADDRESS)));
                location.setLat(cursor.getString(cursor.getColumnIndex(LATITUDE)));
                location.setLon(cursor.getString(cursor.getColumnIndex(LONGITUDE)));

            } while (cursor.moveToNext());
        }

        cursor.close();
        return location;
    }
}
