package com.example.restaurantapp.staff;

import android.app.AlertDialog;
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
import com.example.restaurantapp.database.ReservationRepository;
import com.example.restaurantapp.model.Reservation;

import java.util.ArrayList;

public class ViewReservationsFragment extends Fragment implements ReservationsAdapter.OnReservationActionClickListener {

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

        adapter = new ReservationsAdapter(reservations, false, true, this);
        reservationsRecyclerView.setAdapter(adapter);

        return view;
    }

    private void refreshReservationList() {
        reservations.clear();
        reservations.addAll(reservationRepository.getAllReservations());
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onEditClick(Reservation reservation) { 
        // Not used for staff
    }

    @Override
    public void onCancelClick(Reservation reservation) {
        new AlertDialog.Builder(getContext())
                .setTitle("Cancel Customer Reservation")
                .setMessage("Are you sure you want to cancel this reservation on " + reservation.getDate() + "?")
                .setPositiveButton("Yes, Cancel", (dialog, which) -> {
                    reservationRepository.deleteReservation(reservation);
                    refreshReservationList();
                })
                .setNegativeButton("No", null)
                .show();
    }
}
