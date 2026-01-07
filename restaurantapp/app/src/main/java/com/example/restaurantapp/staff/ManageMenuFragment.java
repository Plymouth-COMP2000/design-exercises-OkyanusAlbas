package com.example.restaurantapp.staff;

import android.app.AlertDialog;
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
import com.example.restaurantapp.database.MenuRepository;
import com.example.restaurantapp.model.MenuItem;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class ManageMenuFragment extends Fragment implements MenuAdapter.OnMenuItemActionClickListener {

    private MenuRepository menuRepository;
    private ArrayList<MenuItem> menuItems;
    private MenuAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_manage_menu, container, false);

        menuRepository = new MenuRepository(getContext());

        RecyclerView staffMenuRecyclerView = view.findViewById(R.id.staffMenuRecyclerView);
        staffMenuRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        menuItems = menuRepository.getAllMenuItems();

        adapter = new MenuAdapter(menuItems, true, this);
        staffMenuRecyclerView.setAdapter(adapter);

        FloatingActionButton fab = view.findViewById(R.id.fabAddMenuItem);
        fab.setOnClickListener(v -> showEditOrAddItemDialog(null));

        return view;
    }

    private void refreshMenuList() {
        menuItems.clear();
        menuItems.addAll(menuRepository.getAllMenuItems());
        adapter.notifyDataSetChanged();
    }

    private void showEditOrAddItemDialog(final MenuItem menuItem) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_menu_item, null);

        final EditText nameEditText = dialogView.findViewById(R.id.edit_menu_item_name);
        final EditText descriptionEditText = dialogView.findViewById(R.id.edit_menu_item_description);
        final EditText priceEditText = dialogView.findViewById(R.id.edit_menu_item_price);

        builder.setView(dialogView);

        if (menuItem != null) {
            builder.setTitle("Edit Menu Item");
            nameEditText.setText(menuItem.getName());
            descriptionEditText.setText(menuItem.getDescription());
            priceEditText.setText(menuItem.getPrice());
        } else {
            builder.setTitle("Add New Menu Item");
        }

        builder.setPositiveButton("Save", (dialog, id) -> {
            String name = nameEditText.getText().toString();
            String description = descriptionEditText.getText().toString();
            String price = priceEditText.getText().toString();

            if (!name.isEmpty() && !description.isEmpty() && !price.isEmpty()) {
                if (menuItem != null) {
                    menuItem.setName(name);
                    menuItem.setDescription(description);
                    menuItem.setPrice(price);
                    menuRepository.updateMenuItem(menuItem);
                } else {
                    menuRepository.addMenuItem(name, description, price);
                }
                refreshMenuList();
            }
        });
        builder.setNegativeButton("Cancel", (dialog, id) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onEditClick(MenuItem menuItem) {
        showEditOrAddItemDialog(menuItem);
    }

    @Override
    public void onDeleteClick(MenuItem menuItem) {
        new AlertDialog.Builder(getContext())
                .setTitle("Delete Menu Item")
                .setMessage("Are you sure you want to delete '" + menuItem.getName() + "'?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    menuRepository.deleteMenuItem(menuItem);
                    refreshMenuList();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}
