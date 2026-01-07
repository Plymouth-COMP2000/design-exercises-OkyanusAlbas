package com.example.restaurantapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.restaurantapp.model.Reservation;

import java.util.ArrayList;

public class ReservationRepository {

    private final DatabaseHelper dbHelper;

    public ReservationRepository(Context context) {
        this.dbHelper = new DatabaseHelper(context);
    }

    public ArrayList<Reservation> getAllReservations() {
        ArrayList<Reservation> reservations = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DatabaseHelper.TABLE_RESERVATIONS, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                long id = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_RES_ID));
                String date = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_RES_DATE));
                String time = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_RES_TIME));
                int guests = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_RES_GUESTS));
                Reservation res = new Reservation(date, time, guests);
                res.setId(id);
                reservations.add(res);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return reservations;
    }

    public long addReservation(String date, String time, int guests) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.KEY_RES_DATE, date);
        values.put(DatabaseHelper.KEY_RES_TIME, time);
        values.put(DatabaseHelper.KEY_RES_GUESTS, guests);
        return db.insert(DatabaseHelper.TABLE_RESERVATIONS, null, values);
    }

    public void updateReservation(Reservation reservation) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.KEY_RES_DATE, reservation.getDate());
        values.put(DatabaseHelper.KEY_RES_TIME, reservation.getTime());
        values.put(DatabaseHelper.KEY_RES_GUESTS, reservation.getNumberOfGuests());

        String selection = DatabaseHelper.KEY_RES_ID + " = ?";
        String[] selectionArgs = { String.valueOf(reservation.getId()) };

        db.update(DatabaseHelper.TABLE_RESERVATIONS, values, selection, selectionArgs);
    }

    public void deleteReservation(Reservation reservation) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String selection = DatabaseHelper.KEY_RES_ID + " = ?";
        String[] selectionArgs = { String.valueOf(reservation.getId()) };
        db.delete(DatabaseHelper.TABLE_RESERVATIONS, selection, selectionArgs);
    }
}
