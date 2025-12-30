package com.example.restaurantapp.staff;

import android.app.AlertDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class ViewReservationsFragment extends Fragment implements ReservationsAdapter.OnReservationActionClickListener {

    private DatabaseHelper dbHelper;
    private ArrayList<Reservation> reservations;
    private ReservationsAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // We can reuse the same layout as the guest's reservation list
        View view = inflater.inflate(R.layout.fragment_my_reservations, container, false);

        dbHelper = new DatabaseHelper(getContext());
        reservations = new ArrayList<>();

        RecyclerView reservationsRecyclerView = view.findViewById(R.id.reservationsRecyclerView);
        reservationsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        loadAllReservationsFromDatabase();

        // Use the flexible adapter: showEdit = false, showCancel = true
        adapter = new ReservationsAdapter(reservations, false, true, this);
        reservationsRecyclerView.setAdapter(adapter);

        return view;
    }

    private void loadAllReservationsFromDatabase() {
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
        if(adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    private void deleteReservationFromDatabase(Reservation reservation) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String selection = DatabaseHelper.KEY_RES_ID + " = ?";
        String[] selectionArgs = { String.valueOf(reservation.getId()) };
        db.delete(DatabaseHelper.TABLE_RESERVATIONS, selection, selectionArgs);
        loadAllReservationsFromDatabase(); // Refresh the list
    }

    // This is not needed for staff, but required by the interface
    @Override
    public void onEditClick(Reservation reservation) { 
        // Do nothing
    }

    @Override
    public void onCancelClick(Reservation reservation) {
        new AlertDialog.Builder(getContext())
                .setTitle("Cancel Customer Reservation")
                .setMessage("Are you sure you want to cancel this reservation on " + reservation.getDate() + "?")
                .setPositiveButton("Yes, Cancel", (dialog, which) -> deleteReservationFromDatabase(reservation))
                .setNegativeButton("No", null)
                .show();
    }
}
