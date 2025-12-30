package com.example.restaurantapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class NotificationsFragment extends Fragment {

    private static final String PREFS_NAME = "NotificationPrefs";
    private static final String KEY_PUSH_NOTIFICATIONS = "pushNotifications";
    private static final String KEY_EMAIL_NOTIFICATIONS = "emailNotifications";

    private Switch pushSwitch;
    private Switch emailSwitch;
    private SharedPreferences sharedPreferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);

        // Using getContext() is safer in fragments
        Context context = getContext();
        if (context == null) {
            // If the context is somehow null, we can't proceed. This prevents a crash.
            Toast.makeText(getActivity(), "Error loading notification settings", Toast.LENGTH_SHORT).show();
            return view; 
        }

        pushSwitch = view.findViewById(R.id.pushNotificationSwitch);
        emailSwitch = view.findViewById(R.id.emailNotificationSwitch);

        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        loadPreferences();

        pushSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> savePreference(KEY_PUSH_NOTIFICATIONS, isChecked));
        emailSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> savePreference(KEY_EMAIL_NOTIFICATIONS, isChecked));

        return view;
    }

    private void loadPreferences() {
        boolean pushEnabled = sharedPreferences.getBoolean(KEY_PUSH_NOTIFICATIONS, true); // Default to true
        boolean emailEnabled = sharedPreferences.getBoolean(KEY_EMAIL_NOTIFICATIONS, true); // Default to true

        pushSwitch.setChecked(pushEnabled);
        emailSwitch.setChecked(emailEnabled);
    }

    private void savePreference(String key, boolean value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }
}
