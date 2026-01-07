package com.example.restaurantapp.guest;

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

import com.example.restaurantapp.NotificationHelper;
import com.example.restaurantapp.R;
import com.example.restaurantapp.database.ReservationRepository;

public class MakeReservationFragment extends Fragment {

    private ReservationRepository reservationRepository;
    private EditText dateEditText;
    private EditText timeEditText;
    private EditText guestsEditText;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_make_reservation, container, false);

        reservationRepository = new ReservationRepository(getContext());
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

        long newRowId = reservationRepository.addReservation(date, time, guests);

        if (newRowId != -1) {
            Toast.makeText(getContext(), "Reservation saved successfully!", Toast.LENGTH_SHORT).show();
            NotificationHelper.sendNotification(getContext(), "New Reservation", "A new reservation was made for " + guests + " guests on " + date);
            dateEditText.setText("");
            timeEditText.setText("");
            guestsEditText.setText("");
        } else {
            Toast.makeText(getContext(), "Error saving reservation", Toast.LENGTH_SHORT).show();
        }
    }
}
