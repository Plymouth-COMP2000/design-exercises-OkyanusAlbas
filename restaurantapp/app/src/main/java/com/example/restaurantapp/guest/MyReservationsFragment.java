package com.example.restaurantapp.guest;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import com.example.restaurantapp.adapter.ReservationsAdapter;
import com.example.restaurantapp.database.DatabaseHelper;
import com.example.restaurantapp.model.Reservation;

import java.util.ArrayList;

public class MyReservationsFragment extends Fragment implements ReservationsAdapter.OnReservationActionClickListener {

    private DatabaseHelper dbHelper;
    private ArrayList<Reservation> reservations;
    private ReservationsAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_reservations, container, false);

        dbHelper = new DatabaseHelper(getContext());
        reservations = new ArrayList<>();

        RecyclerView reservationsRecyclerView = view.findViewById(R.id.reservationsRecyclerView);
        reservationsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        loadReservationsFromDatabase();

        // Updated constructor: show both Edit and Cancel buttons for guests
        adapter = new ReservationsAdapter(reservations, true, true, this);
        reservationsRecyclerView.setAdapter(adapter);

        return view;
    }

    private void loadReservationsFromDatabase() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DatabaseHelper.TABLE_RESERVATIONS, null, null, null, null, null, null);

        reservations.clear();
        if (cursor.moveToFirst()) {
            do {
                long id = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_RES_ID));
                String date = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_RES_DATE));
                String time = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_RES_TIME));
                int guests = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_RES_GUESTS));
                Reservation res = new Reservation(date, time, guests);
                res.setId(id);
                reservations.add(res);
            } while (cursor.moveToNext());
        }
        cursor.close();
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    private void showEditReservationDialog(final Reservation reservation) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.fragment_make_reservation, null);

        final EditText dateEditText = dialogView.findViewById(R.id.edit_reservation_date);
        final EditText timeEditText = dialogView.findViewById(R.id.edit_reservation_time);
        final EditText guestsEditText = dialogView.findViewById(R.id.edit_reservation_guests);
        dialogView.findViewById(R.id.btn_save_reservation).setVisibility(View.GONE);

        dateEditText.setText(reservation.getDate());
        timeEditText.setText(reservation.getTime());
        guestsEditText.setText(String.valueOf(reservation.getNumberOfGuests()));

        builder.setView(dialogView)
                .setTitle("Edit Reservation")
                .setPositiveButton("Save", (dialog, id) -> {
                    String newDate = dateEditText.getText().toString();
                    String newTime = timeEditText.getText().toString();
                    int newGuests = Integer.parseInt(guestsEditText.getText().toString());
                    updateReservationInDatabase(reservation, newDate, newTime, newGuests);
                })
                .setNegativeButton("Cancel", (dialog, id) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void updateReservationInDatabase(Reservation reservation, String newDate, String newTime, int newGuests) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.KEY_RES_DATE, newDate);
        values.put(DatabaseHelper.KEY_RES_TIME, newTime);
        values.put(DatabaseHelper.KEY_RES_GUESTS, newGuests);

        String selection = DatabaseHelper.KEY_RES_ID + " = ?";
        String[] selectionArgs = { String.valueOf(reservation.getId()) };

        db.update(DatabaseHelper.TABLE_RESERVATIONS, values, selection, selectionArgs);
        loadReservationsFromDatabase();
    }

    private void deleteReservationFromDatabase(Reservation reservation) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String selection = DatabaseHelper.KEY_RES_ID + " = ?";
        String[] selectionArgs = { String.valueOf(reservation.getId()) };
        db.delete(DatabaseHelper.TABLE_RESERVATIONS, selection, selectionArgs);
        loadReservationsFromDatabase();
    }

    @Override
    public void onEditClick(Reservation reservation) {
        showEditReservationDialog(reservation);
    }

    @Override
    public void onCancelClick(Reservation reservation) {
        new AlertDialog.Builder(getContext())
                .setTitle("Cancel Reservation")
                .setMessage("Are you sure you want to cancel this reservation?")
                .setPositiveButton("Yes, Cancel", (dialog, which) -> deleteReservationFromDatabase(reservation))
                .setNegativeButton("No", null)
                .show();
    }
}
