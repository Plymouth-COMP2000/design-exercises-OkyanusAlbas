package com.example.restaurantapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Info
    private static final String DATABASE_NAME = "restaurant.db";
    private static final int DATABASE_VERSION = 1;

    // Table Names
    public static final String TABLE_MENU_ITEMS = "menu_items";
    public static final String TABLE_RESERVATIONS = "reservations";

    // Menu Items Table Columns
    public static final String KEY_MENU_ID = "id";
    public static final String KEY_MENU_NAME = "name";
    public static final String KEY_MENU_DESCRIPTION = "description";
    public static final String KEY_MENU_PRICE = "price";
    public static final String KEY_MENU_IMAGE_RES_ID = "image_res_id";

    // Reservations Table Columns
    public static final String KEY_RES_ID = "id";
    public static final String KEY_RES_DATE = "date";
    public static final String KEY_RES_TIME = "time";
    public static final String KEY_RES_GUESTS = "guests";
    public static final String KEY_RES_USER_ID = "user_id"; // To associate reservation with a user

    // Create Table Statements
    private static final String CREATE_TABLE_MENU_ITEMS = "CREATE TABLE " + TABLE_MENU_ITEMS +
            "(" +
            KEY_MENU_ID + " INTEGER PRIMARY KEY," +
            KEY_MENU_NAME + " TEXT," +
            KEY_MENU_DESCRIPTION + " TEXT," +
            KEY_MENU_PRICE + " TEXT," +
            KEY_MENU_IMAGE_RES_ID + " INTEGER" +
            ")";

    private static final String CREATE_TABLE_RESERVATIONS = "CREATE TABLE " + TABLE_RESERVATIONS +
            "(" +
            KEY_RES_ID + " INTEGER PRIMARY KEY," +
            KEY_RES_DATE + " TEXT," +
            KEY_RES_TIME + " TEXT," +
            KEY_RES_GUESTS + " INTEGER," +
            KEY_RES_USER_ID + " INTEGER" +
            ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Called when the database is created for the first time.
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_MENU_ITEMS);
        db.execSQL(CREATE_TABLE_RESERVATIONS);
    }

    // Called when the database needs to be upgraded.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            // Simplest implementation is to drop all old tables and recreate them
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_MENU_ITEMS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_RESERVATIONS);
            onCreate(db);
        }
    }
}
