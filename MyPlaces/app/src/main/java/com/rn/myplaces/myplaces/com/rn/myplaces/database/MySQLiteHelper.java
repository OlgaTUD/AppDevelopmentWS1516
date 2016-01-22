package com.rn.myplaces.myplaces.com.rn.myplaces.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class MySQLiteHelper extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;
    private static MySQLiteHelper sInstance;

    // Database Name
    private static final String DATABASE_NAME = "myplaces";

    // Contacts table name
    private static final String TABLE_PLACES = "places";

    // Contacts Table Columns names
    private static final String PLACE_ID = "id";
    private static final String PLACE_NAME = "name";
    private static final String PLACE_CITY = "city";
    private static final String PLACE_ADRESS = "adress";
    private static final String PLACE_LAT = "lat";
    private static final String PLACE_LONGTITUDE = "longtitude";
    private static final String PLACE_IDENT = "identifikator";

    public static synchronized MySQLiteHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new MySQLiteHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    private MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_PLACES_TABLE =
                "CREATE TABLE " + TABLE_PLACES + "("
                + PLACE_ID + " INTEGER PRIMARY KEY,"
                + PLACE_NAME + " TEXT,"
                + PLACE_CITY + " TEXT,"
                + PLACE_ADRESS + " TEXT,"
                + PLACE_LAT + " TEXT,"
                + PLACE_LONGTITUDE + " TEXT,"
                + PLACE_IDENT +" TEXT);";

        db.execSQL(CREATE_PLACES_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLACES);

        // Create tables again
        onCreate(db);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    // Adding new place
    public void addPlace(Place place) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(PLACE_NAME, place.getName()); //
        values.put(PLACE_CITY, place.getCity()); //
        values.put(PLACE_ADRESS,place.getAdress());
        values.put(PLACE_LAT,place.getLat());
        values.put(PLACE_LONGTITUDE,place.getLong());
        values.put(PLACE_IDENT,place.getIdentifikator());
        // Inserting Row
        db.insert(TABLE_PLACES, null, values);
        db.close(); // Closing database connection
    }

    // Getting single place
    public Place getPlace(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_PLACES, new String[] { PLACE_ID,
                        PLACE_NAME, PLACE_CITY, PLACE_ADRESS, PLACE_LAT, PLACE_LONGTITUDE,PLACE_IDENT }, PLACE_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Place place = new Place(Integer.parseInt(
                cursor.getString(0)),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getString(4),
                cursor.getString(5),
                cursor.getString(6)
        );
        // return place
        return place;
    }

    // Getting All places
    public List<Place> getAllPlaces() {
        List<Place> placesList = new ArrayList<Place>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_PLACES;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Place place = new Place();
                place.setId(Integer.parseInt(cursor.getString(0)));
                place.setName(cursor.getString(1));
                place.setCity(cursor.getString(2));
                place.setAdress(cursor.getString(3));
                place.setLat(cursor.getString(4));
                place.setLongtitude(cursor.getString(5));
                place.setIdent(cursor.getString(6));
                // Adding place to list
                placesList.add(place);
            } while (cursor.moveToNext());
        }

        // return place list
        return placesList;
    }

    // Updating single place
    public int updatePlace(Place place) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(PLACE_NAME, place.getName());
        values.put(PLACE_CITY, place.getCity());
        values.put(PLACE_ADRESS, place.getAdress());
        values.put(PLACE_LAT,place.getLat());
        values.put(PLACE_LONGTITUDE,place.getLong());
        values.put(PLACE_IDENT,place.getIdentifikator());

        // updating row
        return db.update(TABLE_PLACES, values, PLACE_ID + " = ?",
                new String[] { String.valueOf(place.getId()) });
    }

    // Deleting single place
    public void deletePlace(Place place) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PLACES, PLACE_ID + " = ?",
                new String[]{String.valueOf(place.getId())});
        db.close();
    }


    // Getting contacts Count
    public int getPlacesCount() {
        String countQuery = "SELECT  * FROM " + TABLE_PLACES;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }
}
