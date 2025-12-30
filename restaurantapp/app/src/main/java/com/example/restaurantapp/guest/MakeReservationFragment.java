package com.example.restaurantapp.guest;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.restaurantapp.R;
import com.example.restaurantapp.database.DatabaseHelper;

public class MakeReservationFragment extends Fragment {

    private DatabaseHelper dbHelper;
    private EditText dateEditText;
    private EditText timeEditText;
    private EditText guestsEditText;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_make_reservation, container, false);

        dbHelper = new DatabaseHelper(getContext());
        dateEditText = view.findViewById(R.id.edit_reservation_date);
        timeEditText = view.findViewById(R.id.edit_reservation_time);
        guestsEditText = view.findViewById(R.id.edit_reservation_guests);
        Button saveButton = view.findViewById(R.id.btn_save_reservation);

        saveButton.setOnClickListener(v -> saveReservation());

        return view;
    }

    private void saveReservation() {
        String date = dateEditText.getText().toString().trim();
        String time = timeEditText.getText().toString().trim();
        String guestsStr = guestsEditText.getText().toString().trim();

        if (date.isEmpty() || time.isEmpty() || guestsStr.isEmpty()) {
            Toast.makeText(getContext(), "Please fill out all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        int guests = Integer.parseInt(guestsStr);

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.KEY_RES_DATE, date);
        values.put(DatabaseHelper.KEY_RES_TIME, time);
        values.put(DatabaseHelper.KEY_RES_GUESTS, guests);

        long newRowId = db.insert(DatabaseHelper.TABLE_RESERVATIONS, null, values);

        if (newRowId != -1) {
            Toast.makeText(getContext(), "Reservation saved successfully!", Toast.LENGTH_SHORT).show();
            // Clear the fields
            dateEditText.setText("");
            timeEditText.setText("");
            guestsEditText.setText("");
        } else {
            Toast.makeText(getContext(), "Error saving reservation", Toast.LENGTH_SHORT).show();
        }
    }
}
