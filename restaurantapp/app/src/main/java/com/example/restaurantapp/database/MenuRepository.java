package com.example.restaurantapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.restaurantapp.R;
import com.example.restaurantapp.model.MenuItem;

import java.util.ArrayList;

public class MenuRepository {

    private final DatabaseHelper dbHelper;

    public MenuRepository(Context context) {
        this.dbHelper = new DatabaseHelper(context);
    }

    public ArrayList<MenuItem> getAllMenuItems() {
        ArrayList<MenuItem> menuItems = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DatabaseHelper.TABLE_MENU_ITEMS, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                long id = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_MENU_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_MENU_NAME));
                String description = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_MENU_DESCRIPTION));
                String price = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_MENU_PRICE));
                int imageResId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_MENU_IMAGE_RES_ID));
                MenuItem item = new MenuItem(name, description, price, imageResId);
                item.setId(id);
                menuItems.add(item);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return menuItems;
    }

    public void addMenuItem(String name, String description, String price) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.KEY_MENU_NAME, name);
        values.put(DatabaseHelper.KEY_MENU_DESCRIPTION, description);
        values.put(DatabaseHelper.KEY_MENU_PRICE, price);
        values.put(DatabaseHelper.KEY_MENU_IMAGE_RES_ID, R.drawable.ic_launcher_background);
        db.insert(DatabaseHelper.TABLE_MENU_ITEMS, null, values);
    }

    public void updateMenuItem(MenuItem item) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.KEY_MENU_NAME, item.getName());
        values.put(DatabaseHelper.KEY_MENU_DESCRIPTION, item.getDescription());
        values.put(DatabaseHelper.KEY_MENU_PRICE, item.getPrice());

        String selection = DatabaseHelper.KEY_MENU_ID + " = ?";
        String[] selectionArgs = { String.valueOf(item.getId()) };

        db.update(DatabaseHelper.TABLE_MENU_ITEMS, values, selection, selectionArgs);
    }

    public void deleteMenuItem(MenuItem item) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String selection = DatabaseHelper.KEY_MENU_ID + " = ?";
        String[] selectionArgs = { String.valueOf(item.getId()) };
        db.delete(DatabaseHelper.TABLE_MENU_ITEMS, selection, selectionArgs);
    }

    public void addInitialMenuItems() {
        if (getAllMenuItems().isEmpty()) {
            addMenuItem("Spaghetti Carbonara", "A classic Italian pasta dish.", "$15.99");
            addMenuItem("Margherita Pizza", "Fresh tomatoes, mozzarella, and basil.", "$12.99");
            addMenuItem("Caesar Salad", "Crisp romaine lettuce with Parmesan cheese, croutons, and Caesar dressing.", "$9.99");
        }
    }
}
