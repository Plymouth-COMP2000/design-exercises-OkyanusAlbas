package com.example.restaurantapp.guest;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restaurantapp.R;
import com.example.restaurantapp.adapter.MenuAdapter;
import com.example.restaurantapp.database.DatabaseHelper;
import com.example.restaurantapp.model.MenuItem;

import java.util.ArrayList;

public class MenuFragment extends Fragment {

    private DatabaseHelper dbHelper;
    private ArrayList<MenuItem> menuItems;
    private MenuAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, container, false);

        dbHelper = new DatabaseHelper(getContext());
        menuItems = new ArrayList<>();

        RecyclerView menuRecyclerView = view.findViewById(R.id.menuRecyclerView);
        menuRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // This is a temporary way to add data. We'll build a proper staff-side feature for this later.
        addInitialData();
        loadMenuItemsFromDatabase();

        adapter = new MenuAdapter(menuItems);
        menuRecyclerView.setAdapter(adapter);

        return view;
    }

    private void addInitialData() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        // Clear the table first to avoid duplicate entries on each launch
        db.delete(DatabaseHelper.TABLE_MENU_ITEMS, null, null);

        ContentValues values1 = new ContentValues();
        values1.put(DatabaseHelper.KEY_MENU_NAME, "Spaghetti Carbonara");
        values1.put(DatabaseHelper.KEY_MENU_DESCRIPTION, "A classic Italian pasta dish.");
        values1.put(DatabaseHelper.KEY_MENU_PRICE, "$15.99");
        values1.put(DatabaseHelper.KEY_MENU_IMAGE_RES_ID, R.drawable.ic_launcher_background);

        ContentValues values2 = new ContentValues();
        values2.put(DatabaseHelper.KEY_MENU_NAME, "Margherita Pizza");
        values2.put(DatabaseHelper.KEY_MENU_DESCRIPTION, "Fresh tomatoes, mozzarella, and basil.");
        values2.put(DatabaseHelper.KEY_MENU_PRICE, "$12.99");
        values2.put(DatabaseHelper.KEY_MENU_IMAGE_RES_ID, R.drawable.ic_launcher_background);

        ContentValues values3 = new ContentValues();
        values3.put(DatabaseHelper.KEY_MENU_NAME, "Caesar Salad");
        values3.put(DatabaseHelper.KEY_MENU_DESCRIPTION, "Crisp romaine lettuce with Parmesan cheese, croutons, and Caesar dressing.");
        values3.put(DatabaseHelper.KEY_MENU_PRICE, "$9.99");
        values3.put(DatabaseHelper.KEY_MENU_IMAGE_RES_ID, R.drawable.ic_launcher_background);

        db.insert(DatabaseHelper.TABLE_MENU_ITEMS, null, values1);
        db.insert(DatabaseHelper.TABLE_MENU_ITEMS, null, values2);
        db.insert(DatabaseHelper.TABLE_MENU_ITEMS, null, values3);
    }

    private void loadMenuItemsFromDatabase() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DatabaseHelper.TABLE_MENU_ITEMS, null, null, null, null, null, null);

        menuItems.clear(); // Clear the list before loading from DB
        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_MENU_NAME));
                String description = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_MENU_DESCRIPTION));
                String price = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_MENU_PRICE));
                int imageResId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_MENU_IMAGE_RES_ID));
                menuItems.add(new MenuItem(name, description, price, imageResId));
            } while (cursor.moveToNext());
        }
        cursor.close();
    }
}
