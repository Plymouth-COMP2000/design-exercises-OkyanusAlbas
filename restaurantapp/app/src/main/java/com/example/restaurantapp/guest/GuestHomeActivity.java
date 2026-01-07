package com.example.restaurantapp.guest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.restaurantapp.LoginActivity;
import com.example.restaurantapp.NotificationsFragment;
import com.example.restaurantapp.R;
import com.google.android.material.navigation.NavigationBarView;

public class GuestHomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_home);

        NavigationBarView navigationBarView = findViewById(R.id.bottom_navigation);
        navigationBarView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            int itemId = item.getItemId();

            if (itemId == R.id.navigation_menu) {
                selectedFragment = new MenuFragment();
            } else if (itemId == R.id.navigation_my_reservations) {
                selectedFragment = new MyReservationsFragment();
            } else if (itemId == R.id.navigation_make_reservation) {
                selectedFragment = new MakeReservationFragment();
            } else if (itemId == R.id.navigation_notifications) {
                selectedFragment = new NotificationsFragment();
            } else if (itemId == R.id.nav_logout) {
                logoutUser();
                return true; // Don't select a fragment, just logout
            }

            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
            }
            return true;
        });

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MenuFragment()).commit();
        }
    }

    private void logoutUser() {
        // Clear user session
        SharedPreferences prefs = getSharedPreferences("user_session", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.apply();

        // Navigate to LoginActivity
        Intent intent = new Intent(GuestHomeActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
