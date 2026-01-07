package com.example.restaurantapp.staff;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
    private RecyclerView staffMenuRecyclerView;
    private TextView emptyTextView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_manage_menu, container, false);

        // Initialize the repository using the safe requireContext()
        menuRepository = new MenuRepository(requireContext());

        // Setup UI components
        staffMenuRecyclerView = view.findViewById(R.id.staffMenuRecyclerView);
        emptyTextView = view.findViewById(R.id.text_view_empty);
        staffMenuRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize the list and adapter
        menuItems = new ArrayList<>();
        adapter = new MenuAdapter(menuItems, true, this);
        staffMenuRecyclerView.setAdapter(adapter);

        FloatingActionButton fab = view.findViewById(R.id.fabAddMenuItem);
        fab.setOnClickListener(v -> showEditOrAddItemDialog(null));

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Refresh the list every time the user returns to this screen for up-to-date data
        refreshMenuList();
    }

    private void refreshMenuList() {
        // Clear the old list and load the fresh data from the repository
        menuItems.clear();
        menuItems.addAll(menuRepository.getAllMenuItems());
        adapter.notifyDataSetChanged();

        // Show an "empty" message if the list has no items, otherwise show the list
        if (menuItems.isEmpty()) {
            staffMenuRecyclerView.setVisibility(View.GONE);
            emptyTextView.setVisibility(View.VISIBLE);
        } else {
            staffMenuRecyclerView.setVisibility(View.VISIBLE);
            emptyTextView.setVisibility(View.GONE);
        }
    }

    private void showEditOrAddItemDialog(final MenuItem menuItemToEdit) {
        // Inflate the custom dialog layout
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_menu_item, null);

        final EditText nameEditText = dialogView.findViewById(R.id.edit_menu_item_name);
        final EditText descriptionEditText = dialogView.findViewById(R.id.edit_menu_item_description);
        final EditText priceEditText = dialogView.findViewById(R.id.edit_menu_item_price);

        builder.setView(dialogView);

        // Check if we are editing an existing item or adding a new one
        if (menuItemToEdit != null) {
            builder.setTitle("Edit Menu Item");
            nameEditText.setText(menuItemToEdit.getName());
            descriptionEditText.setText(menuItemToEdit.getDescription());
            priceEditText.setText(menuItemToEdit.getPrice());
        } else {
            builder.setTitle("Add New Menu Item");
        }

        builder.setPositiveButton("Save", (dialog, id) -> {
            // Get trimmed input from fields
            String name = nameEditText.getText().toString().trim();
            String description = descriptionEditText.getText().toString().trim();
            String price = priceEditText.getText().toString().trim();

            if (name.isEmpty() || description.isEmpty() || price.isEmpty()) {
                // Provide feedback if fields are empty
                Toast.makeText(getContext(), "All fields must be filled.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Update existing item or add a new one
            if (menuItemToEdit != null) {
                menuItemToEdit.setName(name);
                menuItemToEdit.setDescription(description);
                menuItemToEdit.setPrice(price);
                menuRepository.updateMenuItem(menuItemToEdit);
            } else {
                menuRepository.addMenuItem(name, description, price);
            }
            // Refresh the list to show the changes immediately
            refreshMenuList();
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
        // Show a confirmation dialog before deleting
        new AlertDialog.Builder(requireContext())
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
