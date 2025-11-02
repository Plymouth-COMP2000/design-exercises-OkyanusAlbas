package com.example.restaurantapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class GuestHomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_home);

        Button btnViewMenu = findViewById(R.id.btnViewMenu);
        Button btnMakeBooking = findViewById(R.id.btnMakeBooking);
        Button btnMyReservations = findViewById(R.id.btnMyReservations);
        Button btnNotificationPreferences = findViewById(R.id.btnNotificationPreferences);

        btnViewMenu.setOnClickListener(v -> startActivity(new Intent(GuestHomeActivity.this, MenuActivity.class)));

        btnMakeBooking.setOnClickListener(v -> startActivity(new Intent(GuestHomeActivity.this, ReservationFormActivity.class)));

        btnMyReservations.setOnClickListener(v -> startActivity(new Intent(GuestHomeActivity.this, MyReservationsActivity.class)));

        btnNotificationPreferences.setOnClickListener(v -> startActivity(new Intent(GuestHomeActivity.this, NotificationPreferencesActivity.class)));
    }
}
