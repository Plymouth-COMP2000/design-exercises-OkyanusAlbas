package com.example.restaurantapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        RecyclerView menuRecyclerView = findViewById(R.id.menuRecyclerView);
        menuRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Sample data for the menu - you would replace this with your actual data source
        ArrayList<MenuItem> menuItems = new ArrayList<>();
        // Add some sample menu items
        // menuItems.add(new MenuItem("Spaghetti Carbonara", "A classic Italian pasta dish.", "$15.99", R.drawable.ic_spaghetti));
        // menuItems.add(new MenuItem("Margherita Pizza", "Fresh tomatoes, mozzarella, and basil.", "$12.99", R.drawable.ic_pizza));

        // You'll need to create a custom adapter for the RecyclerView
        // MenuAdapter adapter = new MenuAdapter(menuItems);
        // menuRecyclerView.setAdapter(adapter);
    }
}
