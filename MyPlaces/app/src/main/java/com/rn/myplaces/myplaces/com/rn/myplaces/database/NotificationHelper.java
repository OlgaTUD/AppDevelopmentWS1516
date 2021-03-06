package com.rn.myplaces.myplaces.com.rn.myplaces.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class NotificationHelper extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;
    private static NotificationHelper sInstance;

    // Database Name
    private static final String DATABASE_NAME = "mynotifications";

    // Contacts table name
    private static final String TABLE_NOTIFICATIONS = "notification";

    // Contacts Table Columns names
    private static final String NOTIFICATION_ID = "id";
    private static final String NOTIFICATION_NAME = "name";

    public static synchronized NotificationHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new NotificationHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    private NotificationHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_PLACES_TABLE =
                "CREATE TABLE " + TABLE_NOTIFICATIONS + "("
                        + NOTIFICATION_ID + " INTEGER PRIMARY KEY,"
                        + NOTIFICATION_NAME + " TEXT);";
        db.execSQL(CREATE_PLACES_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTIFICATIONS);

        // Create tables again
        onCreate(db);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    public void addNotification(Notification notif) {
        SQLiteDatabase db = this.getWritableDatabase();

        for(Notification p : this.getAllNotifications()){
            if (p.getText().equals(notif.getText())){
                return;
            }
        }

        ContentValues values = new ContentValues();
        values.put(NOTIFICATION_NAME, notif.getText()); //
        // Inserting Row
        db.insert(TABLE_NOTIFICATIONS, null, values);
        db.close(); // Closing database connection
    }

    public Notification getNotification(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NOTIFICATIONS, new String[] { NOTIFICATION_ID,
                        NOTIFICATION_NAME}, NOTIFICATION_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Notification not = new Notification(Integer.parseInt(
                cursor.getString(0)),
                cursor.getString(1)
        );
        return not;
    }

    public List<Notification> getAllNotifications() {
        List<Notification> notList = new ArrayList<Notification>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_NOTIFICATIONS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Notification not = new Notification();
                not.setId(Integer.parseInt(cursor.getString(0)));
                not.setText(cursor.getString(1));
                notList.add(not);
            } while (cursor.moveToNext());
        }

        return notList;
    }

    public int updateNotification(Notification not) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(NOTIFICATION_NAME, not.getText());

        // updating row
        return db.update(TABLE_NOTIFICATIONS, values, NOTIFICATION_ID + " = ?",
                new String[] { String.valueOf(not.getId()) });
    }

    public void deleteNotification(Notification not) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NOTIFICATIONS, NOTIFICATION_ID + " = ?",
                new String[]{String.valueOf(not.getId())});
        db.close();
    }

}
