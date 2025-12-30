package com.example.restaurantapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restaurantapp.R;
import com.example.restaurantapp.model.MenuItem;

import java.util.ArrayList;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuViewHolder> {

    private final ArrayList<MenuItem> menuItems;
    private OnMenuItemActionClickListener actionClickListener;
    private boolean isStaffMode = false;

    // Interface for handling clicks on edit and delete icons
    public interface OnMenuItemActionClickListener {
        void onEditClick(MenuItem menuItem);
        void onDeleteClick(MenuItem menuItem);
    }

    public MenuAdapter(ArrayList<MenuItem> menuItems, boolean isStaffMode, OnMenuItemActionClickListener listener) {
        this.menuItems = menuItems;
        this.isStaffMode = isStaffMode;
        this.actionClickListener = listener;
    }

    // Overloaded constructor for guest view (no listener needed)
    public MenuAdapter(ArrayList<MenuItem> menuItems) {
        this.menuItems = menuItems;
        this.isStaffMode = false;
    }

    @NonNull
    @Override
    public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_menu, parent, false);
        return new MenuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuViewHolder holder, int position) {
        MenuItem menuItem = menuItems.get(position);
        holder.nameTextView.setText(menuItem.getName());
        holder.descriptionTextView.setText(menuItem.getDescription());
        holder.priceTextView.setText(menuItem.getPrice());
        holder.imageView.setImageResource(menuItem.getImageResource());

        if (isStaffMode) {
            holder.actionsLayout.setVisibility(View.VISIBLE);
            holder.editIcon.setOnClickListener(v -> {
                if (actionClickListener != null) {
                    actionClickListener.onEditClick(menuItem);
                }
            });
            holder.deleteIcon.setOnClickListener(v -> {
                if (actionClickListener != null) {
                    actionClickListener.onDeleteClick(menuItem);
                }
            });
        } else {
            holder.actionsLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return menuItems.size();
    }

    public static class MenuViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView nameTextView;
        TextView descriptionTextView;
        TextView priceTextView;
        View actionsLayout;
        ImageView editIcon;
        ImageView deleteIcon;

        public MenuViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.menuItemImageView);
            nameTextView = itemView.findViewById(R.id.menuItemNameTextView);
            descriptionTextView = itemView.findViewById(R.id.menuItemDescriptionTextView);
            priceTextView = itemView.findViewById(R.id.menuItemPriceTextView);
            actionsLayout = itemView.findViewById(R.id.actions_layout);
            editIcon = itemView.findViewById(R.id.edit_item_icon);
            deleteIcon = itemView.findViewById(R.id.delete_item_icon);
        }
    }
}
