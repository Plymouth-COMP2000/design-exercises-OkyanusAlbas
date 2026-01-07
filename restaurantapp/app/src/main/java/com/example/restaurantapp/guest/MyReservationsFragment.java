package com.example.restaurantapp.guest;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restaurantapp.NotificationHelper;
import com.example.restaurantapp.R;
import com.example.restaurantapp.adapter.ReservationsAdapter;
import com.example.restaurantapp.database.ReservationRepository;
import com.example.restaurantapp.model.Reservation;

import java.util.ArrayList;

public class MyReservationsFragment extends Fragment implements ReservationsAdapter.OnReservationActionClickListener {

    private ReservationRepository reservationRepository;
    private ArrayList<Reservation> reservations;
    private ReservationsAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_reservations, container, false);

        reservationRepository = new ReservationRepository(getContext());

        RecyclerView reservationsRecyclerView = view.findViewById(R.id.reservationsRecyclerView);
        reservationsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        reservations = reservationRepository.getAllReservations();

        adapter = new ReservationsAdapter(reservations, true, true, this);
        reservationsRecyclerView.setAdapter(adapter);

        return view;
    }

    private void refreshReservationList() {
        reservations.clear();
        reservations.addAll(reservationRepository.getAllReservations());
        adapter.notifyDataSetChanged();
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
                    reservation.setDate(dateEditText.getText().toString());
                    reservation.setTime(timeEditText.getText().toString());
                    reservation.setNumberOfGuests(Integer.parseInt(guestsEditText.getText().toString()));
                    reservationRepository.updateReservation(reservation);
                    refreshReservationList();
                    // Notify user of the change
                    NotificationHelper.sendNotification(getContext(), "Reservation Updated", "Your reservation for " + reservation.getDate() + " has been updated.");
                })
                .setNegativeButton("Cancel", (dialog, id) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
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
                .setPositiveButton("Yes, Cancel", (dialog, which) -> {
                    reservationRepository.deleteReservation(reservation);
                    refreshReservationList();
                    // Notify user of the cancellation
                    NotificationHelper.sendNotification(getContext(), "Reservation Cancelled", "Your reservation for " + reservation.getDate() + " has been cancelled.");
                })
                .setNegativeButton("No", null)
                .show();
    }
}
