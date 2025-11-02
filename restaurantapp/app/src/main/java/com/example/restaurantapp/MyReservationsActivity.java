package com.example.restaurantapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyReservationsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_reservations);

        RecyclerView reservationsRecyclerView = findViewById(R.id.reservationsRecyclerView);
        reservationsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Sample data for reservations
        ArrayList<Reservation> reservations = new ArrayList<>();
        // reservations.add(new Reservation("2024-12-25", "19:00", 4));
        // reservations.add(new Reservation("2025-01-10", "20:30", 2));

        // You'll need to create a custom adapter for the RecyclerView
        // ReservationsAdapter adapter = new ReservationsAdapter(reservations);
        // reservationsRecyclerView.setAdapter(adapter);
    }
}
