package com.example.restaurantapp.staff;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restaurantapp.R;
import com.example.restaurantapp.adapter.MenuAdapter;
import com.example.restaurantapp.database.DatabaseHelper;
import com.example.restaurantapp.model.MenuItem;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class ManageMenuFragment extends Fragment implements MenuAdapter.OnMenuItemActionClickListener {

    private DatabaseHelper dbHelper;
    private ArrayList<MenuItem> menuItems;
    private MenuAdapter adapter;
    private RecyclerView staffMenuRecyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_manage_menu, container, false);

        dbHelper = new DatabaseHelper(getContext());
        menuItems = new ArrayList<>();

        staffMenuRecyclerView = view.findViewById(R.id.staffMenuRecyclerView);
        staffMenuRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        loadMenuItemsFromDatabase();

        adapter = new MenuAdapter(menuItems, true, this);
        staffMenuRecyclerView.setAdapter(adapter);

        FloatingActionButton fab = view.findViewById(R.id.fabAddMenuItem);
        fab.setOnClickListener(v -> showAddMenuItemDialog(null));

        return view;
    }

    private void loadMenuItemsFromDatabase() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DatabaseHelper.TABLE_MENU_ITEMS, null, null, null, null, null, null);

        menuItems.clear();
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
    }

    private void showAddMenuItemDialog(final MenuItem menuItemToEdit) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_menu_item, null);

        final EditText nameEditText = dialogView.findViewById(R.id.edit_menu_item_name);
        final EditText descriptionEditText = dialogView.findViewById(R.id.edit_menu_item_description);
        final EditText priceEditText = dialogView.findViewById(R.id.edit_menu_item_price);

        builder.setView(dialogView);

        if (menuItemToEdit != null) {
            builder.setTitle("Edit Menu Item");
            nameEditText.setText(menuItemToEdit.getName());
            descriptionEditText.setText(menuItemToEdit.getDescription());
            priceEditText.setText(menuItemToEdit.getPrice());
        } else {
            builder.setTitle("Add New Menu Item");
        }

        builder.setPositiveButton("Save", (dialog, id) -> {
            String name = nameEditText.getText().toString();
            String description = descriptionEditText.getText().toString();
            String price = priceEditText.getText().toString();

            if (!name.isEmpty() && !description.isEmpty() && !price.isEmpty()) {
                if (menuItemToEdit != null) {
                    updateMenuItemInDatabase(menuItemToEdit, name, description, price);
                } else {
                    addMenuItemToDatabase(name, description, price);
                }
                refreshMenuList();
            }
        });
        builder.setNegativeButton("Cancel", (dialog, id) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void addMenuItemToDatabase(String name, String description, String price) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.KEY_MENU_NAME, name);
        values.put(DatabaseHelper.KEY_MENU_DESCRIPTION, description);
        values.put(DatabaseHelper.KEY_MENU_PRICE, price);
        values.put(DatabaseHelper.KEY_MENU_IMAGE_RES_ID, R.drawable.ic_launcher_background);
        db.insert(DatabaseHelper.TABLE_MENU_ITEMS, null, values);
    }

    private void updateMenuItemInDatabase(MenuItem itemToUpdate, String newName, String newDescription, String newPrice) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.KEY_MENU_NAME, newName);
        values.put(DatabaseHelper.KEY_MENU_DESCRIPTION, newDescription);
        values.put(DatabaseHelper.KEY_MENU_PRICE, newPrice);

        String selection = DatabaseHelper.KEY_MENU_ID + " = ?";
        String[] selectionArgs = { String.valueOf(itemToUpdate.getId()) };

        db.update(DatabaseHelper.TABLE_MENU_ITEMS, values, selection, selectionArgs);
    }

    private void deleteMenuItemFromDatabase(MenuItem itemToDelete) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String selection = DatabaseHelper.KEY_MENU_ID + " = ?";
        String[] selectionArgs = { String.valueOf(itemToDelete.getId()) };
        db.delete(DatabaseHelper.TABLE_MENU_ITEMS, selection, selectionArgs);
        refreshMenuList();
    }

    public void refreshMenuList() {
        loadMenuItemsFromDatabase();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onEditClick(MenuItem menuItem) {
        showAddMenuItemDialog(menuItem);
    }

    @Override
    public void onDeleteClick(MenuItem menuItem) {
        new AlertDialog.Builder(getContext())
                .setTitle("Delete Menu Item")
                .setMessage("Are you sure you want to delete '" + menuItem.getName() + "'?")
                .setPositiveButton("Delete", (dialog, which) -> deleteMenuItemFromDatabase(menuItem))
                .setNegativeButton("Cancel", null)
                .show();
    }
}
